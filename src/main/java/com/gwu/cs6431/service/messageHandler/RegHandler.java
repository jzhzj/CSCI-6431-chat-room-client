package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.CourierImpl;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;
import com.gwu.cs6431.service.message.content.Status;

import java.net.Socket;

public class RegHandler extends Handler implements Executable {

    public RegHandler(Socket socket, String userID, String passwd) {
        this.socket = socket;
        Message msg = new Message(StartLine.REG);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    @Override
    public boolean execute() {
        reply = new CourierImpl(socket).execute(msg);
        if (reply == null)
            return false;
        return reply.getStartLine() == StartLine.REG && reply.getStatus() == Status.Successful;
    }
}
