package com.senpure.io.gateway;

import com.senpure.base.util.IDGenerator;
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
import java.util.concurrent.*;


public class GatewayMessageExecuter {
    private static Logger logger = LoggerFactory.getLogger(GatewayMessageExecuter.class);
    private ExecutorService service;
    private int csLoginMessageId = 1;
    private int csLogoutMessageId = 2;

    private int scLoginMessageId = 3;
    private int scLogoutMessageId = 4;

    private int regServerInstanceMessageId = new SCRegServerHandleMessageMessage().getMessageId();

    private int scrRelationUserGatewayMessageId = new SCRelationUserGatewayMessage().getMessageId();

    private int scBreakUserGatewayMessageId = new SCBreakUserGatewayMessage().getMessageId();
    private int csBreakUserGatewayMessageId = new CSBreakUserGatewayMessage().getMessageId();
    private Message askMessage = new SCAskHandleMessage();


    private int askMessageId = askMessage.getMessageId();



    private ConcurrentMap<Long, Channel> prepLoginChannels = new ConcurrentHashMap<>(2048);

    private ConcurrentMap<Long, Channel> userClientChannel = new ConcurrentHashMap<>(32768);
    private ConcurrentMap<Long, Channel> tokenChannel = new ConcurrentHashMap<>(32768);
    private ConcurrentMap<String, ServerManager> serverInstanceMap = new ConcurrentHashMap<>(128);

    private ConcurrentMap<Integer, ServerManager> messageHandleMap = new ConcurrentHashMap<>(2048);
    private ConcurrentMap<Integer, GatewayHandleMessageServer> handleMessageMap = new ConcurrentHashMap<>(2048);

    private ConcurrentMap<Long, AskMessage> askMap = new ConcurrentHashMap<>();

    protected IDGenerator idGenerator = new IDGenerator(0, 0);
    protected ConcurrentHashMap<Long, CountDownLatch> waitRelationMap = new ConcurrentHashMap(16);

    public GatewayMessageExecuter() {
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public GatewayMessageExecuter(ExecutorService service) {
        this.service = service;
    }

    public void channelActive(Channel channel) {
        Long token = idGenerator.nextId();
        ChannelAttributeUtil.setToken(channel, token);
        tokenChannel.putIfAbsent(token, channel);
        logger.debug(" {} 绑定 token {}",channel,token);
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
            Channel serverChannel = getChannel(message);
            if (serverChannel == null) {
                logger.info("没有找到消息的接收服务器{}", message.getMessageId());
                return;
            }
            serverChannel.writeAndFlush(message);
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
        } else if (message.getMessageId() == askMessageId) {
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
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(server2GatewayMessage.getData());
        message.read(buf, buf.writerIndex());
        if (message.isHandle()) {

        }
    }

    public void relationMessage(Channel channel, Server2GatewayMessage server2GatewayMessage) {
        SCRelationUserGatewayMessage message = new SCRelationUserGatewayMessage();
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(server2GatewayMessage.getData());
        message.read(buf, buf.writerIndex());
        CountDownLatch countDownLatch = waitRelationMap.get(message.getOnceToken());
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
        else {
            CSBreakUserGatewayMessage breakUserGatewayMessage = new CSBreakUserGatewayMessage();
            breakUserGatewayMessage.setToken(message.getToken());
            breakUserGatewayMessage.setUserId(message.getUserId());
            Client2GatewayMessage toMessage = new Client2GatewayMessage();
            toMessage.setMessageId(csBreakUserGatewayMessageId);
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
        logger.info("writerIndex {} readerIndex {} ",buf.writerIndex(),buf.readerIndex());
        message.read(buf, buf.writerIndex());
        List<HandleMessage> handleMessages = message.getMessages();
        String serverKey = message.getServerName() + message.getIpAndFirstPort();
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

        for (HandleMessage handleMessage : handleMessages) {
            GatewayHandleMessageServer handleMessageServer = null;
            if (handleMessage.isServerShare()) {
                handleMessageServer = handleMessageMap.get(handleMessage.getHandleMessageId());
                if (handleMessage == null) {
                    List<ServerManager> serverManagers = new ArrayList<>();
                      handleMessageServer = new GatewayHandleMessageServer(handleMessage.getMessageType());
                }

            } else {
                 handleMessageServer = new GatewayHandleMessageServer(handleMessage.getMessageType());
            }
            handleMessageServer.setNumStart(handleMessage.getNumStart());
            handleMessageServer.setNumEnd(handleMessage.getNumEnd());
            handleMessageServer.setMessageType(handleMessage.getMessageType());
            handleMessageServer.setValueType(handleMessage.getValueType());
        }
        //如果同一个服务有新的消息处理，旧得实例停止接收新的连接
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
        ServerChannelManager serverChannelManager = serverManager.getChannelServer(serverKey);
        serverChannelManager.addChannel(channel);
        serverManager.checkChannelServer(serverKey, serverChannelManager);

    }

    public void addAskMessage(AskMessage askMessage) {
        askMap.put(askMessage.getToken(), askMessage);
    }



}
