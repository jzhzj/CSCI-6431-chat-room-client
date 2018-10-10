package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

import java.net.Socket;

public class TxtHandler extends Handler implements Sendable {
    public TxtHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send() {
        if (msg == null)
            return;

    }

    public void send(String txt) {
        Message msg = new Message(StartLine.TXT);
        this.msg = msg;
        send();
        msg = null;
    }
}
