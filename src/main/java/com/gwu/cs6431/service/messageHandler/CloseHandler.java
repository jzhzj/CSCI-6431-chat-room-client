package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

public class CloseHandler extends Handler implements Sendable {

    public CloseHandler(String sessionID, String sourceUser, String targetUser) {
        Message msg = new Message(StartLine.CLOSE);
        msg.setSessionID(sessionID);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    @Override
    public void send() {

    }
}
