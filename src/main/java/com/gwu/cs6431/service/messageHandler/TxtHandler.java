package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.Courier;
import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

import java.net.Socket;

public class TxtHandler extends Handler implements Sendable {
    Courier courier;

    public TxtHandler(Socket socket) {
        this.socket = socket;
        courier = new CourierImpl(socket);
    }

    @Override
    public void send() {
        if (msg == null)
            throw new RuntimeException("Do Not send nothing! TxtHandler::send()");
        courier.send(msg);
    }

    public void send(String txt) {
        msg = new Message(Message.StartLine.TXT);
        msg.setTxt(txt);
        send();
        msg = null;
    }
}
