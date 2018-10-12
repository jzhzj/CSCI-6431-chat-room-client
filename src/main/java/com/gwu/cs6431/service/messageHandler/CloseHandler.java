package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

import java.net.Socket;

public class CloseHandler extends Handler implements Sendable {

    public CloseHandler(Socket socket, String sessionID, String sourceUser, String targetUser) {
        this.socket = socket;
        Message msg = new Message(StartLine.CLOSE);
        msg.setSessionID(sessionID);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    @Override
    public void send() {
        new CourierImpl(socket).send(msg);
    }
}
