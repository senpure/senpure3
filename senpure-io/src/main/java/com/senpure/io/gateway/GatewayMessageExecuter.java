package com.senpure.io.gateway;

import com.senpure.base.util.IDGenerator;
import com.senpure.base.util.NameThreadFactory;
import com.senpure.io.ChannelAttributeUtil;
import com.senpure.io.bean.HandleMessage;
import com.senpure.io.message.*;
import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class GatewayMessageExecuter {
    protected static Logger logger = LoggerFactory.getLogger(GatewayMessageExecuter.class);
    private ScheduledExecutorService service;
    private int csLoginMessageId = 1;
    private int csLogoutMessageId = 2;

    private int scLoginMessageId = 3;
    private int scLogoutMessageId = 4;
    private int regServerInstanceMessageId = new SCRegServerHandleMessageMessage().getMessageId();
    private int scrRelationUserGatewayMessageId = new SCRelationUserGatewayMessage().getMessageId();
    private int scBreakUserGatewayMessageId = new SCBreakUserGatewayMessage().getMessageId();
    private int csBreakUserGatewayMessageId = new CSBreakUserGatewayMessage().getMessageId();


    private int scAskMessageId = new SCAskHandleMessage().getMessageId();

    private ConcurrentMap<Long, Channel> prepLoginChannels = new ConcurrentHashMap<>(2048);

    private ConcurrentMap<Long, Channel> userClientChannel = new ConcurrentHashMap<>(32768);
    private ConcurrentMap<Long, Channel> tokenChannel = new ConcurrentHashMap<>(32768);
    private ConcurrentMap<String, ServerManager> serverInstanceMap = new ConcurrentHashMap<>(128);

    private ConcurrentMap<Integer, ServerManager> messageHandleMap = new ConcurrentHashMap<>(2048);
    private ConcurrentMap<Integer, HandleMessageManager> handleMessageManagerMap = new ConcurrentHashMap<>(2048);

    private ConcurrentMap<Long, AskMessage> askMap = new ConcurrentHashMap<>();


    protected IDGenerator idGenerator = new IDGenerator(0, 0);
    protected ConcurrentHashMap<Long, WaitRelationTask> waitRelationMap = new ConcurrentHashMap(16);
    protected ConcurrentHashMap<Long, WaitAskTask> waitAskMap = new ConcurrentHashMap(16);

    public GatewayMessageExecuter() {

        service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2,
                new NameThreadFactory("gateway-executor"));

    }

    public GatewayMessageExecuter(ScheduledExecutorService service) {
        this.service = service;
    }

    public void channelActive(Channel channel) {
        Long token = idGenerator.nextId();
        ChannelAttributeUtil.setToken(channel, token);
        tokenChannel.putIfAbsent(token, channel);
        logger.debug(" {} 绑定 token {}", channel, token);
    }

    //将客户端消息转发给具体的服务器
    public void execute(final Channel channel, final Client2GatewayMessage message) {

        service.execute(() -> {
            logger.info("messageId {} data {}", message.getMessageId(), message.getData()[0]);
            //登录
            if (message.getMessageId() == csLoginMessageId) {
                prepLoginChannels.putIfAbsent(ChannelAttributeUtil.getToken(channel), channel);
            }
            message.setToken(ChannelAttributeUtil.getToken(channel));
            //转发到具体的子服务器
            Long userId = ChannelAttributeUtil.getUserId(channel);
            if (userId != null) {
                message.setUserId(userId);
            }
            HandleMessageManager handleMessageManager = handleMessageManagerMap.get(message.getMessageId());
            if (handleMessageManager == null) {
                logger.warn("没有找到消息的接收服务器{}", message.getMessageId());
                return;
            }
            handleMessageManager.execute(message);

        });
    }

    public Channel getChannel(Client2GatewayMessage message) {
        ServerManager serverManager = messageHandleMap.get(message.getMessageId());
        if (serverManager != null) {
            return serverManager.channel(message.getUserId(), message.getToken());
        } else {
            logger.warn("没有找到处理[{}] 的服务器", message.getMessageId());
            logger.info(" messageHandleMap {}", messageHandleMap);
            logger.info("serverInstanceMap {}", serverInstanceMap);
        }
        return null;
    }

    //处理服务器发过来的消息
    public void execute(Channel channel, final Server2GatewayMessage message) {
        if (message.getMessageId() == regServerInstanceMessageId) {
            regServerInstance(channel, message);
            return;
        } else if (message.getMessageId() == scrRelationUserGatewayMessageId) {

            relationMessage(channel, message);
            return;
        } else if (message.getMessageId() == scAskMessageId) {
            askMessage(channel, message);
            return;
        }
        if (message.getMessageId() == scLoginMessageId) {
            long userId = message.getUserIds()[0];
            Channel clientChannel = prepLoginChannels.remove(message.getToken());
            if (clientChannel != null) {
                ChannelAttributeUtil.setUserId(clientChannel, userId);
                userClientChannel.putIfAbsent(userId, clientChannel);
            }
        }

        if (message.getUserIds().length == 0) {
            Channel clientChannel = tokenChannel.get(message.getToken());
            if (clientChannel == null) {
                logger.warn("没有找到channel{}", message.getToken());
            } else {
                clientChannel.writeAndFlush(message);
            }
        } else {
            for (Long userId : message.getUserIds()) {
                Channel clientChannel = userClientChannel.get(userId);
                if (clientChannel == null) {
                    logger.warn("没有找到玩家{}", userId);
                } else {
                    clientChannel.writeAndFlush(message);
                }
            }
        }

    }

    public void askMessage(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCAskHandleMessage message = new SCAskHandleMessage();
        readMessage(message, server2GatewayMessage);
        WaitAskTask waitAskTask = waitAskMap.get(message.getToken());
        if (waitAskTask != null) {

            if (message.isHandle()) {
                String serverName = ChannelAttributeUtil.getServerName(channel);
                String serverKey = ChannelAttributeUtil.getServerKey(channel);
                ServerManager serverManager = serverInstanceMap.get(serverName);
                for (ServerChannelManager useChannelManager : serverManager.getUseChannelManagers()) {
                    if (useChannelManager.getServerKey().equalsIgnoreCase(serverKey)) {
                        waitAskTask.answer(serverManager,useChannelManager, true);
                        return;
                    }
                }
                waitAskTask.answer(null,null, false);
            } else {
                waitAskTask.answer(null,null, false);
            }

            // waitAskTask.answer(channel, message.isHandle());
        }

    }

    private void readMessage(Message message, Server2GatewayMessage server2GatewayMessage) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(server2GatewayMessage.getData());
        message.read(buf, buf.writerIndex());
    }

    /**
     * 处具体服务器返回的关联用户信息
     *
     * @param channel
     * @param server2GatewayMessage
     */
    public void relationMessage(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCRelationUserGatewayMessage message = new SCRelationUserGatewayMessage();
        readMessage(message, server2GatewayMessage);
        WaitRelationTask waitRelationTask = waitRelationMap.get(message.getRelationToken());
        if (waitRelationTask != null) {
            waitRelationTask.setRelationTime(System.currentTimeMillis());
            waitRelationTask.setRelation(true);
        } else {
            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
            breakUserGatewayMessage.setToken(message.getToken());
            breakUserGatewayMessage.setUserId(message.getUserId());
            Client2GatewayMessage toMessage = new Client2GatewayMessage();
            toMessage.setMessageId(breakUserGatewayMessage.getMessageId());
            ByteBuf bf = Unpooled.buffer();
            message.write(bf);
            toMessage.setData(bf.array());
            channel.writeAndFlush(toMessage);

        }

    }

    public synchronized void regServerInstance(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCRegServerHandleMessageMessage message = new SCRegServerHandleMessageMessage();
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(server2GatewayMessage.getData());
        logger.info("writerIndex {} readerIndex {} ", buf.writerIndex(), buf.readerIndex());
        message.read(buf, buf.writerIndex());
        List<HandleMessage> handleMessages = message.getMessages();
        String serverKey = message.getServerName() + message.getIpAndFirstPort();
        ChannelAttributeUtil.setServerName(channel, message.getServerName());
        ChannelAttributeUtil.setServerKey(channel, serverKey);
        logger.info("服务注册:{}:{} [{}]", message.getServerName(), message.getIpAndFirstPort(), message.getReadableServerName());
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

//        for (HandleMessage handleMessage : handleMessages) {
//            HandleMessageManager handleMessageManager = null;
//            if (handleMessage.isServerShare()) {
//                handleMessageManager = handleMessageManagerMap.get(handleMessage.getHandleMessageId());
//                if (handleMessage == null) {
//                    List<ServerManager> serverManagers = new ArrayList<>();
//                      handleMessageManager = new HandleMessageManager(handleMessage.getMessageType());
//                }
//
//            } else {
//                 handleMessageManager = new HandleMessageManager(handleMessage.getMessageType());
//            }
//            handleMessageManager.setNumStart(handleMessage.getNumStart());
//            handleMessageManager.setNumEnd(handleMessage.getNumEnd());
//            handleMessageManager.setMessageType(handleMessage.getMessageType());
//            handleMessageManager.setValueType(handleMessage.getValueType());
//        }
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
                if (handleMessage.getHandleMessageId() == id.intValue()) {
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
                handleMessageManager = new HandleMessageManager(handleMessage.getHandleMessageId(), true, false, this);
                handleMessageManagerMap.put(handleMessage.getHandleMessageId(), handleMessageManager);
            }
            handleMessageManager.addServerManager(handleMessage.getHandleMessageId(), serverManager);

        }
    }

    public void addAskMessage(AskMessage askMessage) {
        askMap.put(askMessage.getToken(), askMessage);
    }

    public void sendMessage(ServerChannelManager serverChannelManager, Message message) {
        Client2GatewayMessage toMessage = new Client2GatewayMessage();
        toMessage.setMessageId(message.getMessageId());
        ByteBuf bf = Unpooled.buffer();
        message.write(bf);
        toMessage.setData(bf.array());
        serverChannelManager.sendMessage(toMessage);
    }

    private void checkWaitRelationTask() {
        List<Long> tokens = new ArrayList<>();
        for (Map.Entry<Long, WaitRelationTask> entry : waitRelationMap.entrySet()) {
            WaitRelationTask task = entry.getValue();
            if (task.check()) {
                tokens.add(entry.getKey());
                service.execute(() -> task.sendMessage());
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

    private void checkWaitAskTask() {
        List<Long> tokens = new ArrayList<>();
        for (Map.Entry<Long, WaitAskTask> entry : waitAskMap.entrySet()) {
            WaitAskTask waitAskTask = entry.getValue();
            if (waitAskTask.getServerChannelManager() != null) {
                tokens.add(entry.getKey());

                waitAskTask.sendMessage();
            }
            else {

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
}
