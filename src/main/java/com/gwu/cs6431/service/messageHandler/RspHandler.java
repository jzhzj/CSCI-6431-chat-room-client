package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

import java.net.Socket;

public class RspHandler extends Handler implements Executable, Sendable {
    public RspHandler(Socket socket, String sourceUser, String targetUser) {
        this.socket = socket;
        Message msg = new Message(Message.StartLine.RSP);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    @Override
    public boolean execute() {
        if (msg.getStatus() == null)
            return accept();
        // TODO DO NOT CLOSE socket since it will be use for Session
        reply = new CourierImpl(socket).execute(msg);
        if (reply == null)
            return false;
        return reply.getStartLine() == Message.StartLine.RSP && reply.getStatus() == Message.Status.Successful;
    }

    @Override
    public void send() {
        if (msg.getStatus() == null)
            refuse();
    }

    public boolean accept() {
        msg.setStatus(Message.Status.Accepted);
        return execute();
    }

    public void refuse() {
        msg.setStatus(Message.Status.Refused);
        send();
    }
}
