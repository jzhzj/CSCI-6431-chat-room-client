package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.gui.InitController;
import com.gwu.cs6431.client.gui.MainController;
import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.io.courier.CourierImpl;
import com.gwu.cs6431.client.service.message.Message;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.Socket;

/**
 * Handles REG message initiated by the client, and handles REG message returned by the server.
 * To handles message initiated by the client, use public constructor to get an instance of this class.
 * To handles message returned by the server, user the IncomingHandlerFactory to get an instance of this class.
 *
 * @author qijiuzhi
 */
public class RegHandler extends AbstractHandler implements InitMsgHandler, IncomingMsgHandler {
    public RegHandler(Socket socket, String userID, String passwd) {
        this.courier = new CourierImpl(socket);
        Message msg = new Message(Message.StartLine.REG);
        msg.setUserID(userID);
        msg.setPasswd(passwd);
        this.msg = msg;
    }

    RegHandler() {
    }

    @Override
    public void receive(Message msg) {
        switch (msg.getStatus()) {
            case Successful:
                Platform.runLater(() -> InitController.promptAlert(Alert.AlertType.CONFIRMATION, "Succeed", msg.getTxt(), "Please don't forget your user Id and password"));
                break;
            case Failed:
                Platform.runLater(() -> InitController.promptAlert(Alert.AlertType.ERROR, "Failed", msg.getTxt(), "Please try again."));
                break;
            default:
        }
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
}
