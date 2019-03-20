package com.senpure.io.gateway;

import com.senpure.base.util.Assert;
import com.senpure.base.util.IDGenerator;
import com.senpure.base.util.NameThreadFactory;
import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.Constant;
import com.senpure.io.ServerProperties;
import com.senpure.io.bean.HandleMessage;
import com.senpure.io.message.*;
import com.senpure.io.protocol.Message;
import com.senpure.io.support.MessageIdReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class GatewayMessageExecuter {
    protected static Logger logger = LoggerFactory.getLogger(GatewayMessageExecuter.class);
    private ScheduledExecutorService service;
    private int serviceRefCount = 0;
    private int csLoginMessageId = 0;

    private ServerProperties.Gateway gateway;

    private int scLoginMessageId = 0;

    private int csHeartMessageId = CSHeartMessage.MESSAGE_ID;

    private ConcurrentMap<Long, Channel> prepLoginChannels = new ConcurrentHashMap<>(2048);

    private ConcurrentMap<Long, Channel> userClientChannel = new ConcurrentHashMap<>(32768);
    private ConcurrentMap<Long, Channel> tokenChannel = new ConcurrentHashMap<>(32768);
    public ConcurrentMap<String, ServerManager> serverInstanceMap = new ConcurrentHashMap<>(128);

    public ConcurrentMap<Integer, ServerManager> messageHandleMap = new ConcurrentHashMap<>(2048);
    public ConcurrentMap<Integer, HandleMessageManager> handleMessageManagerMap = new ConcurrentHashMap<>(2048);


    protected IDGenerator idGenerator;
    protected ConcurrentHashMap<Long, WaitRelationTask> waitRelationMap = new ConcurrentHashMap(16);
    protected ConcurrentHashMap<Long, WaitAskTask> waitAskMap = new ConcurrentHashMap(16);

    private Map<Integer, SGInnerHandler> sgHandlerMap = new HashMap<>();

    private boolean init = false;

    public GatewayMessageExecuter() {
        this(Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2,
                new NameThreadFactory("gateway-executor")), new IDGenerator(0, 0));
    }

    public GatewayMessageExecuter(ScheduledExecutorService service, IDGenerator idGenerator) {
        this.service = service;
        this.idGenerator = idGenerator;
        // init();
        // startCheck();

    }

    /**
     * 引用计数+1
     */
    public void retainService() {
        serviceRefCount++;
    }

    public void releaseService() {
        serviceRefCount--;

    }

    public void releaseAndTryShutdownService() {
        serviceRefCount--;
        if (serviceRefCount <= 0) {
            service.shutdown();
        }
    }

    public void shutdownService() {
        if (serviceRefCount <= 0) {
            service.shutdown();
        } else {
            logger.warn("server 持有引用{}，请先释放后关闭", serviceRefCount);
        }
    }


    public void report() {
        logger.info("csLoginMessageId: {} scLoginMessageId:{} ", csLoginMessageId, scLoginMessageId);
    }

    public void channelActive(Channel channel) {
        Long token = idGenerator.nextId();
        ChannelAttributeUtil.setToken(channel, token);
        tokenChannel.putIfAbsent(token, channel);
        logger.debug("{} 绑定 token {}", channel, token);
    }

    //将客户端消息转发给具体的服务器
    public void execute(final Channel channel, final Client2GatewayMessage message) {
        service.execute(() -> {

            if (message.getMessageId() == csLoginMessageId) {
                prepLoginChannels.put(ChannelAttributeUtil.getToken(channel), channel);
            } else if (message.getMessageId() == csHeartMessageId) {
                SCHeartMessage heartMessage = new SCHeartMessage();
                sendMessage2Client(heartMessage, ChannelAttributeUtil.getToken(channel));
                return;
            }
            Long userId = ChannelAttributeUtil.getUserId(channel);
            if (userId != null) {
                message.setUserId(userId);
            }
            //登录
            message.setToken(ChannelAttributeUtil.getToken(channel));
            //转发到具体的子服务器
            HandleMessageManager handleMessageManager = handleMessageManagerMap.get(message.getMessageId());
            if (handleMessageManager == null) {
                logger.warn("没有找到消息的接收服务器{}", message.getMessageId());
                SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();
                errorMessage.setType(Constant.ERROR_NOT_FOUND_SERVER);
                errorMessage.setId(message.getMessageId());
                errorMessage.setMessage("没有服务器处理" + MessageIdReader.read(message.getMessageId()));
                sendMessage2Client(errorMessage, message.getToken());
                return;
            }
            try {
                handleMessageManager.execute(message);
            } catch (Exception e) {
                logger.error("转发消息出错 " + message.getMessageId(), e);
                SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();
                errorMessage.setType(Constant.ERROR_SERVER_ERROR);
                errorMessage.setId(message.getMessageId());
                errorMessage.setMessage(MessageIdReader.read(message.getMessageId()) + "," + e.getMessage());
                sendMessage2Client(errorMessage, message.getToken());
            }
        });
    }

    protected void sendMessage2Client(Message message, Long token) {
        Channel clientChannel = tokenChannel.get(token);
        if (clientChannel == null) {
            logger.warn("没有找到channel token {}", token);
        } else {
            Server2GatewayMessage m = new Server2GatewayMessage();
            ByteBuf buf = Unpooled.buffer(message.getSerializedSize());
            message.write(buf);
            byte data[] = new byte[message.getSerializedSize()];
            buf.readBytes(data);
            m.setToken(token);
            m.setData(data);
            m.setMessageId(message.getMessageId());
            clientChannel.writeAndFlush(m);
        }
    }

    public void init() {
        if (init) {
            logger.warn("messageExecuter 已经初始化");
            return;
        }
        Assert.notNull(gateway, "gateway 配置文件不能为空");
        init = true;
        Assert.isTrue(csLoginMessageId > 0 && scLoginMessageId > 0, "登录消息为设置");
        sgHandlerMap.put(SCRegServerHandleMessageMessage.MESSAGE_ID, this::regServerInstance);
        sgHandlerMap.put(SCRelationUserGatewayMessage.MESSAGE_ID, this::relationMessage);
        sgHandlerMap.put(SCAskHandleMessage.MESSAGE_ID, this::askMessage);
        sgHandlerMap.put(SCIdNameMessage.MESSAGE_ID, (channel, server2GatewayMessage) -> idNameMessage(server2GatewayMessage));
        sgHandlerMap.put(SCHeartMessage.MESSAGE_ID, (channel, server2GatewayMessage) -> true);
        sgHandlerMap.put(scLoginMessageId, (channel, server2GatewayMessage) -> loginMessage(server2GatewayMessage));
        sgHandlerMap.put(SCBreakUserGatewayMessage.MESSAGE_ID, this::breakRelationMessage);
        sgHandlerMap.put(SCKickOffMessage.MESSAGE_ID, (channel, server2GatewayMessage) -> kickOffMessage(server2GatewayMessage));

        startCheck();
    }

    //处理服务器发过来的消息
    public void execute(Channel channel, final Server2GatewayMessage message) {
        service.execute(() -> {
            try {
                SGInnerHandler handler = sgHandlerMap.get(message.getMessageId());
                if (handler != null) {
                    boolean over = handler.execute(channel, message);
                    if (over) {
                        return;
                    }
                }
                if (message.getUserIds().length == 0) {
                    Channel clientChannel = tokenChannel.get(message.getToken());
                    if (clientChannel == null) {
                        logger.warn("没有找到channel token:{}", message.getToken());
                    } else {
                        clientChannel.writeAndFlush(message);
                    }
                } else {
                    for (Long userId : message.getUserIds()) {
                        //全消息
                        if (userId == 0L) {
                            for (Map.Entry<Long, Channel> entry : userClientChannel.entrySet()) {
                                entry.getValue().writeAndFlush(message);
                            }
                            break;
                        } else {
                            Channel clientChannel = userClientChannel.get(userId);
                            if (clientChannel == null) {
                                logger.warn("没有找到用户 :{}", userId);
                            } else {
                                clientChannel.writeAndFlush(message);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("处理服务器到网关的消息出错", e);
            }
        });
    }

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }


    private void userOffline(Channel channel, Long token, Long userId) {
        for (Map.Entry<String, ServerManager> entry : serverInstanceMap.entrySet()) {
            ServerManager serverManager = entry.getValue();
            serverManager.breakUserGateway(channel, token, userId);
        }
    }

    /**
     * 登陆服务不用取消关联
     *
     * @param channel
     * @param token
     * @param userId
     */
    private void userChange(Channel channel, Long token, Long userId) {
        for (Map.Entry<String, ServerManager> entry : serverInstanceMap.entrySet()) {
            ServerManager serverManager = entry.getValue();
            if (serverManager.getHandleIds().contains(csLoginMessageId)) {
                logger.info("切换账号 {} 不用取消关联", serverManager.getServerName());
            } else {
                serverManager.breakUserGateway(channel, token, userId);
            }
        }
    }


    public void clientOffline(Channel channel) {
        service.execute(() -> {
            Long token = ChannelAttributeUtil.getToken(channel);
            Long userId = ChannelAttributeUtil.getUserId(channel);
            userId = userId == null ? 0 : userId;
            tokenChannel.remove(token);
            userOffline(channel, token, userId);
        });
    }

    public void readMessage(Message message, Server2GatewayMessage server2GatewayMessage) {
        ByteBuf buf = Unpooled.buffer(server2GatewayMessage.getData().length);
        buf.writeBytes(server2GatewayMessage.getData());
        message.read(buf, buf.writerIndex());
    }


    //todo 一个服务只允许一个ask id
    private synchronized boolean regServerInstance(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCRegServerHandleMessageMessage message = new SCRegServerHandleMessageMessage();
        ByteBuf buf = Unpooled.buffer(server2GatewayMessage.getData().length);
        buf.writeBytes(server2GatewayMessage.getData());
        // logger.info("writerIndex {} readerIndex {} ", buf.writerIndex(), buf.readerIndex());
        message.read(buf, buf.writerIndex());
        List<HandleMessage> handleMessages = message.getMessages();
        String serverKey = message.getServerKey();
        ChannelAttributeUtil.setRemoteServerName(channel, message.getServerName());
        ChannelAttributeUtil.setRemoteServerKey(channel, serverKey);
        logger.info("服务注册:{}:{} [{}]", message.getServerName(), message.getServerKey(), message.getReadableServerName());
        for (HandleMessage handleMessage : handleMessages) {
            logger.info("{}", handleMessage);
        }
        ServerManager serverManager = serverInstanceMap.get(message.getServerName());
        if (serverManager == null) {
            serverManager = new ServerManager(this);
            serverInstanceMap.put(message.getServerName(), serverManager);
            for (HandleMessage handleMessage : handleMessages) {
                serverManager.markHandleId(handleMessage.getHandleMessageId());
                messageHandleMap.putIfAbsent(handleMessage.getHandleMessageId(), serverManager);
            }
            serverManager.setServerName(message.getServerName());
        }
        //如果同一个服务处理消息id不一致，旧得实例停止接收新的连接
        for (HandleMessage handleMessage : handleMessages) {
            if (!serverManager.handleId(handleMessage.getHandleMessageId())) {
                logger.info("{} 处理了新的消息{}[{}] ，旧的服务器停止接收新的请求分发", message.getServerName(), handleMessage.getHandleMessageId(), handleMessage.getMessageClasses());
                serverManager.prepStopOldInstance();
                for (HandleMessage hm : handleMessages) {
                    serverManager.markHandleId(hm.getHandleMessageId());
                }
                break;
            }
        }
        for (Integer id : serverManager.getHandleIds()) {
            boolean discard = true;
            for (HandleMessage handleMessage : handleMessages) {
                if (handleMessage.getHandleMessageId() == id) {
                    discard = false;
                    break;
                }
            }
            if (discard) {
                logger.info("{} 丢弃了消息{} ，旧的服务器停止接收新的请求分发", message.getServerName(), id);
                serverManager.prepStopOldInstance();
                for (HandleMessage hm : handleMessages) {
                    serverManager.markHandleId(hm.getHandleMessageId());
                }
                break;
            }
        }
        ServerChannelManager serverChannelManager = serverManager.getChannelServer(serverKey);
        serverChannelManager.addChannel(channel);
        serverManager.checkChannelServer(serverKey, serverChannelManager);
        for (HandleMessage handleMessage : handleMessages) {
            HandleMessageManager handleMessageManager = handleMessageManagerMap.get(handleMessage.getHandleMessageId());
            if (handleMessageManager == null) {
                handleMessageManager = new HandleMessageManager(handleMessage.getHandleMessageId(), handleMessage.isDirect(), handleMessage.isServerShare(), this);
                handleMessageManagerMap.put(handleMessage.getHandleMessageId(), handleMessageManager);
            }
            handleMessageManager.addServerManager(handleMessage.getHandleMessageId(), serverManager);
        }
        return true;
    }


    public void sendMessage(ServerChannelManager serverChannelManager, Message message) {
        Client2GatewayMessage toMessage = new Client2GatewayMessage();
        toMessage.setMessageId(message.getMessageId());
        ByteBuf buf = Unpooled.buffer(message.getSerializedSize());
        message.write(buf);
        byte[] data = new byte[message.getSerializedSize()];
        buf.readBytes(data);
        toMessage.setData(data);
        serverChannelManager.sendMessage(toMessage);
    }


    private boolean idNameMessage(Server2GatewayMessage server2GatewayMessage) {
        SCIdNameMessage message = new SCIdNameMessage();
        readMessage(message, server2GatewayMessage);
        MessageIdReader.relation(message.getIdNames());
        return true;
    }


    /**
     * 处具体服务器返回的关联用户信息
     *
     * @param channel
     * @param server2GatewayMessage
     */
    private boolean relationMessage(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCRelationUserGatewayMessage message = new SCRelationUserGatewayMessage();
        readMessage(message, server2GatewayMessage);
        WaitRelationTask waitRelationTask = waitRelationMap.get(message.getRelationToken());
        if (waitRelationTask != null) {
            if (message.getRelationToken() == waitRelationTask.getRelationToken().longValue()) {
                waitRelationTask.setRelationTime(System.currentTimeMillis());
                waitRelationTask.setRelation(true);
            }
        } else {
            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
            breakUserGatewayMessage.setToken(message.getToken());
            breakUserGatewayMessage.setUserId(message.getUserId());
            breakUserGatewayMessage.setRelationToken(message.getRelationToken());
            Client2GatewayMessage toMessage = new Client2GatewayMessage();
            toMessage.setMessageId(breakUserGatewayMessage.getMessageId());
            ByteBuf buf = Unpooled.buffer();
            buf.ensureWritable(breakUserGatewayMessage.getSerializedSize());
            byte[] data = new byte[breakUserGatewayMessage.getSerializedSize()];
            message.write(buf);
            buf.readBytes(data);
            toMessage.setData(data);
            channel.writeAndFlush(toMessage);
        }
        return true;
    }

    private boolean breakRelationMessage(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCBreakUserGatewayMessage message = new SCBreakUserGatewayMessage();
        readMessage(message, server2GatewayMessage);
        long tempUserId = server2GatewayMessage.getMessageId();
        Channel userChannel = null;
        if (tempUserId > 0) {
            userChannel = userClientChannel.get(tempUserId);
        }
        if (userChannel == null) {
            userChannel = tokenChannel.get(server2GatewayMessage.getToken());
        }
        if (userChannel != null) {
            Long userId = ChannelAttributeUtil.getUserId(userChannel);
            userId = userId == null ? 0 : userId;
            Long token = ChannelAttributeUtil.getToken(userChannel);
            String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
            ServerManager serverManager = serverInstanceMap.get(serverName);
            if (serverManager != null) {
                serverManager.breakUserGateway(userChannel, token, userId);
            }
        }
        return true;
    }

    private void checkWaitRelationTask() {
        List<Long> tokens = new ArrayList<>();
        for (Map.Entry<Long, WaitRelationTask> entry : waitRelationMap.entrySet()) {
            WaitRelationTask task = entry.getValue();
            if (task.check()) {
                tokens.add(entry.getKey());
                service.execute(task::sendMessage);
            } else {
                if (task.cancel()) {
                    tokens.add(entry.getKey());
                    service.execute(() -> task.sendCancelMessage(this));
                }
            }
        }
        for (Long token : tokens) {
            waitRelationMap.remove(token);
        }
    }

    private boolean loginMessage(Server2GatewayMessage message) {
        long userId = message.getUserIds()[0];
        Channel clientChannel = prepLoginChannels.remove(message.getToken());
        if (clientChannel != null) {
            Long oldUserId = ChannelAttributeUtil.getUserId(clientChannel);
            if (oldUserId != null) {
                if (oldUserId == userId) {
                    logger.info("{}重复登陆 {} 不做额外的处理", clientChannel, userId);
                } else {
                    logger.info("{}切换账号{}  -》  {} ", clientChannel, oldUserId, userId);
                    Long token = ChannelAttributeUtil.getToken(clientChannel);
                    userChange(clientChannel, token, oldUserId);
                }
            }
            ChannelAttributeUtil.setUserId(clientChannel, userId);
            userClientChannel.put(userId, clientChannel);
        } else {
            logger.warn("登录成功 userId:{} channel缺失 token{}", userId, message.getToken());
        }
        return false;
    }

    private boolean kickOffMessage(Server2GatewayMessage server2GatewayMessage) {
        SCKickOffMessage message = new SCKickOffMessage();
        readMessage(message, server2GatewayMessage);
        long tempUserId = message.getUserId();
        Channel userChannel = null;
        if (tempUserId > 0) {
            userChannel = userClientChannel.get(tempUserId);
        }
        if (userChannel == null) {
            userChannel = tokenChannel.get(message.getToken());
        }
        if (userChannel != null) {
            logger.info("{} token:{} uerId:{} 踢下线", userChannel, ChannelAttributeUtil.getToken(userChannel), ChannelAttributeUtil.getUserId(userChannel));
            userChannel.close();
        }
        else {
            logger.info("{} 踢下线失败，找不到channel",message.toString());
        }
        return true;
    }

    private boolean askMessage(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCAskHandleMessage message = new SCAskHandleMessage();
        readMessage(message, server2GatewayMessage);
        WaitAskTask waitAskTask = waitAskMap.get(message.getToken());
        if (waitAskTask != null) {
            if (message.isHandle()) {
                String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
                String serverKey = ChannelAttributeUtil.getRemoteServerKey(channel);
                logger.debug("{} {} 可以处理 {} 值位 {} 的请求", serverName, serverKey,
                        MessageIdReader.read(waitAskTask.getFromMessageId()), waitAskTask.getValue());
                ServerManager serverManager = serverInstanceMap.get(serverName);
                for (ServerChannelManager useChannelManager : serverManager.getUseChannelManagers()) {
                    if (useChannelManager.getServerKey().equalsIgnoreCase(serverKey)) {
                        waitAskTask.answer(serverManager, useChannelManager, true);
                        return true;
                    }
                }
                waitAskTask.answer(null, null, false);
            } else {
                if (logger.isDebugEnabled()) {
                    String serverName = ChannelAttributeUtil.getRemoteServerName(channel);
                    String serverKey = ChannelAttributeUtil.getRemoteServerKey(channel);
                    logger.debug("{} {} 无法处理 {} 值位 {} 的请求", serverName, serverKey,
                            MessageIdReader.read(waitAskTask.getFromMessageId()), waitAskTask.getValue());
                }
                waitAskTask.answer(null, null, false);
            }
        }
        return true;
    }

    private void checkWaitAskTask() {
        List<Long> tokens = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, WaitAskTask> entry : waitAskMap.entrySet()) {
            WaitAskTask waitAskTask = entry.getValue();
            if (waitAskTask.getServerChannelManager() != null) {
                tokens.add(entry.getKey());
                waitAskTask.sendMessage();
            } else {
                //超时
                if (now - waitAskTask.getStartTime() > waitAskTask.getMaxDelay()) {
                    logger.debug("没有服务器处理 {} 值位{} 的请求 询问服务器 {} 应答服务器 {}",
                            MessageIdReader.read(waitAskTask.getFromMessageId()), waitAskTask.getValue(),
                            waitAskTask.getAskTimes(), waitAskTask.getAnswerTimes());
                    tokens.add(entry.getKey());
                    SCInnerErrorMessage errorMessage = new SCInnerErrorMessage();
                    errorMessage.setType(Constant.ERROR_NOT_HANDLE_REQUEST);
                    errorMessage.setId(waitAskTask.getFromMessageId());
                    errorMessage.setMessage(MessageIdReader.read(waitAskTask.getFromMessageId()));
                    errorMessage.setValue(waitAskTask.getValue());
                    sendMessage2Client(errorMessage, waitAskTask.getMessage().getToken());
                }
            }
        }
        for (Long token : tokens) {
            waitAskMap.remove(token);
        }
    }

    private void startCheck() {
        service.scheduleWithFixedDelay(() -> {
            checkWaitRelationTask();
            checkWaitAskTask();
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void destroy() {
        service.shutdown();
    }


    public int getCsLoginMessageId() {
        return csLoginMessageId;
    }

    public void setCsLoginMessageId(int csLoginMessageId) {
        this.csLoginMessageId = csLoginMessageId;
    }

    public int getScLoginMessageId() {
        return scLoginMessageId;
    }

    public void setScLoginMessageId(int scLoginMessageId) {
        this.scLoginMessageId = scLoginMessageId;
    }

    public ServerProperties.Gateway getGateway() {
        return gateway;
    }

    public void setGateway(ServerProperties.Gateway gateway) {
        this.gateway = gateway;
    }

    private interface SGInnerHandler {
        boolean execute(Channel channel, Server2GatewayMessage server2GatewayMessage);

    }


}
