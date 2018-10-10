package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.service.message.Message;

import java.net.Socket;

abstract class Handler {
    // Status, UserID, Password, SourceUser, TargetUser, SessionID
    Message msg;
    Socket socket;
}
