package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;

import java.io.IOException;
import java.net.Socket;

abstract class Handler {
    // Status, UserID, Password, SourceUser, TargetUser, SessionID
    Message msg;
    Message reply;
    Socket socket;

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
