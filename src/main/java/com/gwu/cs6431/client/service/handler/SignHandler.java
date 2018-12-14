package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.gui.InitController;
import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.io.courier.CourierImpl;
import com.gwu.cs6431.client.service.message.Message;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles SIGN message initiated by the client, and handles SIGN message returned by the server.
 * To handles message initiated by the client, use public constructor to get an instance of this class.
 * To handles message returned by the server, user the IncomingHandlerFactory to get an instance of this class.
 *
 * @author qijiuzhi
 */
public class SignHandler extends AbstractHandler implements InitMsgHandler, IncomingMsgHandler {
    public SignHandler(Socket socket, String userID, String passwd) {
        this.courier = new CourierImpl(socket);
        Message msg = new Message(Message.StartLine.SIGN);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    SignHandler(Message msg) {
        this.msg = msg;
    }

    @Override
    public void receive(Message msg) {
        switch (msg.getStatus()) {
            case Successful:
                initController.signIn();
                break;
            case Failed:
                InitController.promptAlert(Alert.AlertType.ERROR, "Failed", msg.getTxt(), "Please try again.");
            default:
        }
    }

    @Override
    public void send() {
        try {
            courier.send(msg);
        } catch (IOException | MessageNotCompletedException e) {
            e.printStackTrace();
        }
    }
}
