package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.constant.ClientProps;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class InvtHandler extends Handler implements Callable<Boolean> {

    public InvtHandler(Socket socket, String sourceUser, String targetUser) {
        this.socket = socket;
        Message msg = new Message(StartLine.INVT);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    public InvtHandler(Socket socket, String sourceUser, String targetUser, String txt) {
        this(socket, sourceUser, targetUser);
        msg.setTxt(txt);
    }

    @Override
    public Boolean call() throws Exception {
        try {
            // TODO DO NOT CLOSE this socket since it will be used for the Session
            socket = new Socket(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT);
        } catch (IOException e) {
            // TODO
        }
        return null;
    }
}
