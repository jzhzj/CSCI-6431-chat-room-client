package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

import java.net.Socket;
import java.util.concurrent.Callable;

public class InvtHandler extends Handler implements Callable<Boolean> {

    public InvtHandler(Socket socket, String sourceUser, String targetUser) {
        this.socket = socket;
        Message msg = new Message(Message.StartLine.INVT);
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
        reply = new CourierImpl(socket).execute(msg);
        if (reply == null)
            return false;
        return reply.getStartLine().equals(Message.StartLine.RSP) && reply.getStatus().equals(Message.Status.Successful);
    }
}
