package com.senpure.io.handler;


import com.senpure.io.message.CSAskHandleMessage;
import com.senpure.io.message.SCAskHandleMessage;
import com.senpure.io.protocol.Message;

public interface RealityAskMessageHandler<T extends Message> extends RealityMessageHandler<T> {


   SCAskHandleMessage ask(CSAskHandleMessage message);

}
