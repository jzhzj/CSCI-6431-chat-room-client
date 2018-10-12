package com.gwu.cs6431.service.io.listener;

import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.message.content.StartLine;

public class Task implements Runnable {
    String messageStr;
    Message msg;

    public Task(String messageStr) {
        this.messageStr = messageStr;
    }

    @Override
    public void run() {
        msg = Message.genMessage(messageStr);
        if (msg.getStartLine() != StartLine.INVT)
            return;
        handle();
    }

    private void handle() {
        // TODO prompt invitation in GUI
    }
}
