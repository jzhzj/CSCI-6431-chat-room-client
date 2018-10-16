package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

import java.io.IOException;
import java.net.Socket;

public class SignHandler extends Handler implements Executable {
    public SignHandler(Socket socket, String userID, String passwd) {
        this.socket = socket;
        Message msg = new Message(Message.StartLine.SIGN);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    @Override
    public boolean execute() {
        System.out.println(msg);
        courier = new CourierImpl(socket);
        reply = courier.execute(msg);
        if (reply == null)
            return false;
        return reply.getStartLine().equals(Message.StartLine.SIGN) && reply.getStatus().equals(Message.Status.Successful);
    }

    @Override
    public String getServerFeedback() {
        if (reply == null)
            return "Failed";
        else
            return reply.getTxt();
    }

    @Override
    public void close() throws IOException {
        courier.close();
    }
}
