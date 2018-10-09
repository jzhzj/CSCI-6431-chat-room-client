package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

public class TxtHandler extends Handler implements Sendable {
    public TxtHandler(String txt) {
        Message msg = new Message(StartLine.TXT);
        msg.setTxt(txt);
        this.msg = msg;
    }

    @Override
    public void send() {

    }
}
