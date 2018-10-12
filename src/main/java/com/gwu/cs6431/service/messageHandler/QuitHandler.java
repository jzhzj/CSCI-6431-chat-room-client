package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

import java.net.Socket;

public class QuitHandler extends Handler implements Sendable {
    public QuitHandler(Socket socket, String userID, String passwd) {
        this.socket = socket;
        Message msg = new Message(StartLine.QUIT);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    @Override
    public void send() {
        new CourierImpl(socket).send(msg);
    }
}
