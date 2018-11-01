package com.gwu.cs6431.service.messageHandler;

import com.gwu.cs6431.gui.MainController;
import com.gwu.cs6431.service.message.Message;
import com.gwu.cs6431.service.session.Session;
import com.gwu.cs6431.service.session.User;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.Socket;

public class InvtAbstractHandler extends AbstractHandler implements Runnable {
    private MainController mainController;

    public InvtAbstractHandler(MainController mainController, Socket socket, String sourceUser, String targetUser) {
        this.mainController = mainController;
        this.socket = socket;
        Message msg = new Message(Message.StartLine.INVT);
        msg.setSourceUser(sourceUser);
        msg.setTargetUser(targetUser);
        this.msg = msg;
    }

    public InvtAbstractHandler(MainController mainController, Socket socket, String sourceUser, String targetUser, String txt) {
        this(mainController, socket, sourceUser, targetUser);
        msg.setTxt(txt);
    }

    @Override
    public void run() {
//        reply = new CourierImpl(socket).execute(msg);
//        if (reply == null) {
//            try {
//                courier.close();
//            } catch (IOException e) {
//                // TODO
//            }
//        } else {
//            if (Message.StartLine.RSP.equals(reply.getStartLine())) {
//                if (Message.Status.Accepted.equals(reply.getStatus())) {
//                    Platform.runLater(() -> {
//                        mainController.createSessionPane(
//                                new Session(reply.getSessionID(), socket, msg.getSourceUser(), msg.getTargetUser()));
//                        MainController.promptAlert(Alert.AlertType.CONFIRMATION, "Invitation accepted!"
//                                , "Your invitation to " + msg.getTargetUser() + " is accepted"
//                                , "Start to chat!");
//                    });
//                }
//                if (Message.Status.Refused.equals(reply.getStatus())) {
//                    Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.NONE, "Invitation Refused"
//                            , "Your invitation to " + msg.getTargetUser() + " is refused.", null));
//                }
//                if (Message.Status.Failed.equals(reply.getStatus())) {
//                    Platform.runLater(() -> MainController.promptAlert(Alert.AlertType.ERROR, "Connection failure"
//                            , "Invitation successful, but connection failed"
//                            , "Your invitation to " + msg.getTargetUser() + " is accepted, but the server failed to establish your connection. Please try it later"));
//                }
//            } else {
//                // TODO
//                try {
//                    courier.close();
//                } catch (IOException e) {
//                    // TODO
//                }
//            }
//        }

        // TODO code below is used for test, delete it later, and uncomment code above
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("cannot sleep");
        }
        Platform.runLater(() -> {
            Session session = new Session("1234", socket, User.getClientUser().getUserID(), msg.getTargetUser());
            mainController.createSessionPane(
                    session);
            MainController.promptAlert(Alert.AlertType.CONFIRMATION, "Invitation accepted!"
                    , "Your invitation to " + msg.getTargetUser() + " is accepted"
                    , "Start to chat!");
        });
    }

    @Override
    public void close() throws IOException {

    }
}
