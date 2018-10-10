package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;
import com.gwu.cs6431.service.message.content.Status;

import java.net.Socket;

public class RspHandler extends Handler implements Executable, Sendable {
    public RspHandler(Socket socket, String sourceUser, String targetUser) {
        this.socket = socket;
        Message msg = new Message(StartLine.RSP);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    @Override
    public boolean execute() {
        if (msg.getStatus() == null)
            return accept();
        // TODO DO NOT CLOSE socket since it will be use for Session
        return false;
    }

    @Override
    public void send() {
        if (msg.getStatus() == null)
            refuse();
    }

    public boolean accept() {
        msg.setStatus(Status.Accepted);
        return execute();
    }

    public void refuse() {
        msg.setStatus(Status.Refused);
        send();
    }
}
