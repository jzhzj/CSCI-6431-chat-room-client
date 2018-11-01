package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.io.courier.Courier;
import com.gwu.cs6431.service.message.Message;

import java.io.IOException;
import java.net.Socket;

abstract class AbstractHandler {
    // Status, UserID, Password, SourceUser, TargetUser, SessionID
    Message msg;
    Message reply;
    Socket socket;
    Courier courier;

    public abstract void close() throws IOException;
}
