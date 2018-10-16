package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.Courier;
import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

import java.io.IOException;
import java.net.Socket;

public class TxtHandler extends Handler implements Sendable {
    private Courier courier;

    public TxtHandler(Socket socket) {
        this.socket = socket;
        courier = new CourierImpl(socket);
    }

    @Override
    public void send() {
        if (msg == null)
            return;
        try {
            courier.send(msg);
        } catch (IOException e) {
            // TODO
        }
    }

    public void send(String txt) {
        msg = new Message(Message.StartLine.TXT);
        msg.setTxt(txt);
        send();
        msg = null;
    }
}
