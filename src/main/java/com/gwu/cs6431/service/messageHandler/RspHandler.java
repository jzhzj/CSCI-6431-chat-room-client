package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;
import com.gwu.cs6431.service.message.content.Status;

public class RspHandler extends Handler implements Executable {
    public RspHandler(String sourceUser, String targetUser) {
        Message msg = new Message(StartLine.RSP);
    }

    @Override
    public boolean execute() {
        return false;
    }

    public boolean accept() {
        msg.setStatus(Status.Accepted);
        return execute();
    }

    public void refuse() {
        msg.setStatus(Status.Refused);
        execute();
    }
}
