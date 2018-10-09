package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

public class InvtHandler extends Handler implements Sendable {
    public InvtHandler(String sourceUser, String targetUser) {
        Message msg = new Message(StartLine.INVT);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    @Override
    public void send() {

    }

    public void send(String txt) {
        setTxt(txt);
        send();
    }

    private void setTxt(String txt) {
        msg.setTxt(txt);
    }
}
