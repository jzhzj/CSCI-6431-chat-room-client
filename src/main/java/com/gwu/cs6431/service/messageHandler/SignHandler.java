package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;

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
        System.out.println("SignHandler::Execute");
        reply = new CourierImpl(socket).execute(msg);
        System.out.println("got reply");
        if (reply == null)
            return false;
        return reply.getStartLine() == Message.StartLine.SIGN && reply.getStatus() == Message.Status.Successful;
    }
}
