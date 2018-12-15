package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.service.message.Message;

public interface IncomingMsgHandler {
    void receive(Message msg);
}
