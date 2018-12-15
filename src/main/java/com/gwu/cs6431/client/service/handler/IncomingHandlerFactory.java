package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.service.message.Message;

public class IncomingHandlerFactory {
    public static IncomingMsgHandler getHandler(Message.StartLine startLine) {
        switch (startLine) {
            case INVT:
                return new InvtHandler();
            case REG:
                return new RegHandler();
            case SIGN:
                return new SignHandler();
            case RSP:
                return new RspHandler();
            case CLOSE:
                return new CloseHandler();
            case TXT:
                return new TxtHandler();
            default:
                return null;
        }
    }
}
