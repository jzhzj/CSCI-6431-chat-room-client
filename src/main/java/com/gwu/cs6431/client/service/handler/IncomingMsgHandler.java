package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.service.message.Message;

/**
 * An IncomingMsgHandler is used to handle messages received from the server.
 *
 * @author qijiuzhi
 */
public interface IncomingMsgHandler {
    void receive(Message msg);
}
