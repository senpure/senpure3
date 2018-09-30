package com.senpure.io;

import com.senpure.io.message.Client2GatewayMessage;


public class GatewayForwardMessageServer extends GatewayHandleMessageServer {

    private GatewayComponentServer gatewayComponentServer;


    public GatewayForwardMessageServer(GatewayComponentServer gatewayComponentServer) {
        super(0);
        this.gatewayComponentServer = gatewayComponentServer;
    }

    public void handleMessage(Client2GatewayMessage message) {

        gatewayComponentServer.channel(message.getPlayerId(), message.getToken()).writeAndFlush(message);
    }
}
