package com.gwu.cs6431.client.service.io.listener;

import com.gwu.cs6431.client.service.handler.IncomingHandlerFactory;
import com.gwu.cs6431.client.service.handler.IncomingMsgHandler;
import com.gwu.cs6431.client.service.message.Message;

/**
 * This class is used to handle received messages from Listener.
 *
 * @author qijiuzhi
 */
public class Task implements Runnable {
    private String rawMessage;

    public Task(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public void run() {
        System.out.println(rawMessage);
        Message msg = Message.genMessage(rawMessage);
        if (msg == null) {
            return;
        }
        handle(msg);
    }

    private void handle(Message msg) {

        IncomingMsgHandler handler = IncomingHandlerFactory.getHandler(msg.getStartLine());
        if (handler != null) {
            handler.receive(msg);
        }
    }
}
