package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

import java.io.IOException;
import java.net.Socket;

public class QuitHandler extends Handler implements Sendable {
    public QuitHandler(Socket socket, String userID, String passwd) {
        this.socket = socket;
        Message msg = new Message(Message.StartLine.QUIT);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    @Override
    public void send() {
        try {
            new CourierImpl(socket).send(msg);
        } catch (IOException e) {
            // TODO
        }
    }
}
