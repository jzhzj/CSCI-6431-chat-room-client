package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

public class QuitHandler extends Handler implements Sendable {
    public QuitHandler(String userID, String passwd) {
        Message msg = new Message(StartLine.QUIT);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    @Override
    public void send() {

    }
}
