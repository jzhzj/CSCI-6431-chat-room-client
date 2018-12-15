package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.io.courier.CourierImpl;
import com.gwu.cs6431.client.service.message.Message;
import com.gwu.cs6431.client.service.session.Session;
import com.gwu.cs6431.client.service.session.User;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles outgoing and incoming TXT message.
 *
 * @author qijiuzhi
 */
public class TxtHandler extends AbstractHandler implements InitMsgHandler, IncomingMsgHandler {

    public TxtHandler(Socket socket) {
        this.courier = new CourierImpl(socket);
        courier = new CourierImpl(socket);
    }

    TxtHandler() {
    }

    @Override
    public void receive(Message msg) {
        Session session = Session.getSession(msg.getSessionID());
        session.appendHistory(msg.getSourceUser(), msg.getTxt());
    }

    @Override
    public void send() {
        if (msg == null) {
            return;
        }
        try {
            courier.send(msg);
        } catch (IOException | MessageNotCompletedException e) {
            e.printStackTrace();
        }
    }

    public void send(String targetUser, String sessionId, String txt) {
        msg = new Message(Message.StartLine.TXT);
        msg.setSourceUser(User.getClientUser().getUserID());
        msg.setTargetUser(targetUser);
        msg.setSessionID(sessionId);
        msg.setTxt(txt);
        send();
        msg = null;
    }
}
