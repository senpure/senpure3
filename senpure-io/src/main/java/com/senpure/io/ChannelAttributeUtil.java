package com.senpure.io;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelAttributeUtil {
    private static String USER_ID = "userId";
    private static String TOKEN = "token";
    private static String USER_NAME = "userName";

    private static String OFFLINE_HANDLER = "offlineHandler";
    private static String CHANNEL_USER = "channelUser";

    private static String IP_AND_PORT = "ipAndPort";
    public static AttributeKey<Long> userIdKey = AttributeKey.valueOf(USER_ID);
    public static AttributeKey<Long> tokenKey = AttributeKey.valueOf(TOKEN);
    public static AttributeKey<String> userNameKey = AttributeKey.valueOf(USER_NAME);
    public static AttributeKey<String> ipAndPortKey = AttributeKey.valueOf(IP_AND_PORT);
    public static AttributeKey<ChannelPlayer> channelPlayerKey = AttributeKey.valueOf(CHANNEL_USER);
    public static AttributeKey<OffLineHandler> offlineHandlerKey = AttributeKey.valueOf(OFFLINE_HANDLER);

    public static void clear(ChannelHandlerContext ctx, AttributeKey<?> key) {

        ctx.channel().attr(key).set(null);

    }

    public static void clear(Channel channel, AttributeKey<?> key) {

        channel.attr(key).set(null);
    }


    public static Long getUserId(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        }

        return ctx.channel().attr(userIdKey).get();

    }


    public static void setToken(Channel channel, Long token) {
        channel.attr(tokenKey).set(token);

    }

    public static Long getToken(Channel channel) {

        return channel.attr(tokenKey).get();
    }

    public static void setUserId(Channel channel, Long playerId) {

        channel.attr(userIdKey).set(playerId);

    }

    public static void setUserId(ChannelHandlerContext ctx, Long playerId) {

        ctx.channel().attr(userIdKey).set(playerId);

    }

    public static void setUserName(Channel channel, String name) {

        channel.attr(userNameKey).set(name);

    }

    public static Long getUserId(Channel channel) {

        return channel.attr(userIdKey).get();

    }

    public static String getUserName(Channel channel) {

        return channel.attr(userNameKey).get();

    }


    public static String getIpAndPort(Channel channel) {

        return channel.attr(ipAndPortKey).get();

    }

    public static void setIpAndPort(Channel channel, String ipAndPort) {

        channel.attr(ipAndPortKey).set(ipAndPort);

    }

    public static void setOfflineHandler(Channel channel, OffLineHandler handler) {

        channel.attr(offlineHandlerKey).set(handler);

    }

    public static OffLineHandler getOfflineHandler(Channel channel) {

        return channel.attr(offlineHandlerKey).get();

    }

    public static void setChannelPlayer(Channel channel, ChannelPlayer player) {

        channel.attr(channelPlayerKey).set(player);
    }

    public static ChannelPlayer getChannelPlayer(Channel channel) {
        return channel.attr(channelPlayerKey).get();
    }

    private static void appenPlayerStr(Channel channel, StringBuilder sb) {
        ChannelPlayer player = ChannelAttributeUtil.getChannelPlayer(channel);
        if (player == null) {
            Long playerId = ChannelAttributeUtil.getUserId(channel);
            String name = ChannelAttributeUtil.getUserName(channel);
            if (playerId != null) {
                sb.append("[ID][").append(playerId).append("]");
            }
            if (name != null) {
                sb.append("[NICK][").append(name).append("]");
            }
        } else {
            sb.append("[ID][").append(player.getIdStr()).append("]");
            sb.append("[NICK][").append(player.getNick()).append("]");
        }

    }

    public static String getChannelLogStr(Channel channel) {
        StringBuilder sb = new StringBuilder();
        sb.append(channel);
        appenPlayerStr(channel, sb);
        return sb.toString();
    }

    public static String getChannelPlayerStr(Channel channel) {
        StringBuilder sb = new StringBuilder();
        appenPlayerStr(channel, sb);
        return sb.toString();
    }

    public static <T> T get(ChannelHandlerContext ctx, AttributeKey<T> key) {
        return ctx.channel().attr(key).get();
    }

    public static <T> void set(Channel channel, AttributeKey<T> key, T value) {

        channel.attr(key).set(value);
    }

    public static <T> T get(Channel channel, AttributeKey<T> key) {

        return channel.attr(key).get();

    }

    public static <T> void set(ChannelHandlerContext ctx, AttributeKey<T> key, T value) {

        ctx.channel().attr(key).set(value);
    }
}
