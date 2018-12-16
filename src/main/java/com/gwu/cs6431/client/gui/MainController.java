package com.gwu.cs6431.client.gui;

import com.gwu.cs6431.client.service.handler.*;
import com.gwu.cs6431.client.service.io.SocketFactory;
import com.gwu.cs6431.client.service.message.Message;
import com.gwu.cs6431.client.service.session.Session;
import com.gwu.cs6431.client.service.session.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller that controls the main stage.
 *
 * @author qijiuzhi
 */
public class MainController extends Controller implements Initializable {
    @FXML
    private Button quitButton;
    @FXML
    private Button inviteButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button sendButton;
    @FXML
    private Label nameLabel;
    @FXML
    private VBox vBox;
    @FXML
    private TextArea dialogArea;
    @FXML
    private TextArea inputArea;
    @FXML
    private Text nameTxt;

    private AnchorPane curSessionPane;

    private boolean initialized;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTxt.setText(User.getClientUser().getUserID());

        quitButton.setOnAction(event -> quitBtnAction());

        inviteButton.setOnAction(event -> invtBtnAction());

        // set dialogArea autoscroll
        dialogArea.textProperty().addListener((observable, oldValue, newValue) -> dialogArea.setScrollTop(Double.MAX_VALUE));

        closeButton.setOnAction(event -> closeBtnAction());

        sendButton.setOnAction(event -> sendBtnAction());

        AbstractHandler.setMainController(this);
    }


    private void quitBtnAction() {
        QuitHandler quitHandler = new QuitHandler(getConstSocket()
                , User.getClientUser().getUserID()
                , User.getClientUser().getPasswd());
        quitHandler.send();

        // close the socket
        SocketFactory.close();
        //  close window
        Platform.exit();
    }


    private void invtBtnAction() {
        // This is used to setup the shortcuts. Shortcuts can only be set once.
        if (!initialized) {
            KeyCombination kc = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);
            sendButton.getScene().getAccelerators().put(kc, this::sendBtnAction);
            initialized = true;
        }


        TextInputDialog invtDialog = new TextInputDialog();
        invtDialog.setTitle("Send an invitation");
        invtDialog.setHeaderText("Who do you wanna chat with?");
        invtDialog.setContentText("User ID: ");
        Optional<String> result1 = invtDialog.showAndWait();
        result1.ifPresent(targetUser -> {
            TextInputDialog saySthDialog = new TextInputDialog();
            saySthDialog.setTitle("Any thing to say?");
            saySthDialog.setHeaderText("You can send the reason for the invitation. If not, just press the cancel.");
            saySthDialog.setContentText("Reason: ");
            Optional<String> result2 = saySthDialog.showAndWait();
            if (result2.isPresent() && !"".equals(result2.get())) {
                new InvtHandler(SocketFactory.getConstSocket(), User.getClientUser().getUserID(), targetUser, result2.get()).send();
            } else {
                new InvtHandler(SocketFactory.getConstSocket(), User.getClientUser().getUserID(), targetUser).send();
            }
        });
    }

    public void incomingInvitation(Message msg) {
        String txt = msg.getTxt() == null ? null : msg.getSourceUser() + ": " + msg.getTxt();
        boolean accepted = MainController.promptINVT(Alert.AlertType.CONFIRMATION, "New Invitation", "You have an invitation from: " + msg.getSourceUser(), txt);
        if (accepted) {
            new RspHandler(SocketFactory.getConstSocket(), msg.getSourceUser(), msg.getTargetUser()).accept();
        } else {
            new RspHandler(SocketFactory.getConstSocket(), msg.getSourceUser(), msg.getTargetUser()).refuse();
        }
    }

    /**
     * Defines what happens when the close button is clicked.
     * If the current Session Pane is not null, then this session will be closed.
     */
    private void closeBtnAction() {
        if (curSessionPane == null) {
            return;
        }

        // get the session
        Session curSession = (Session) curSessionPane.getUserData();
        // change the dialog to null
        changeDialog(curSession, null);
        // remove the session pane
        vBox.getChildren().remove(curSessionPane);
        // send close message to target user
        new CloseHandler(SocketFactory.getConstSocket(),
                curSession.getSessionID(),
                curSession.getSourceUser().getUserID(),
                curSession.getTargetUser().getUserID()).send();
        // close the session
        curSession.close();

        curSessionPane = null;
    }

    /**
     * send the text in the inputArea to the corresponding session.
     */
    private void sendBtnAction() {
        if (curSessionPane != null) {
            String input = inputArea.getText();
            inputArea.clear();
            ((Session) curSessionPane.getUserData()).send(input);
        }
    }

    /**
     * creates a new Session pane
     */
    public void createSessionPane(Session session) {
        session.addListener(this::listenerFire);
        Label name = new Label(session.getTargetUser().getUserID());
        name.setFont(Font.font("Optima", 16));
        name.setLayoutX(15);
        name.setLayoutY(20);
        Line line = new Line(0, 70, 200, 70);
        line.setStroke(Paint.valueOf("#DADADA"));
        AnchorPane ap = new AnchorPane(name, line);
        ap.setUserData(session);
        ap.setPrefHeight(70);
        ap.setOnMouseClicked(this::sessionPaneOnMouseClicked);
        vBox.getChildren().add(ap);
        changeDialog(null, session);
        curSessionPane = ap;
    }

    /**
     * handles when a session pane is clicked
     */
    private void sessionPaneOnMouseClicked(MouseEvent event) {
        Session oldSession = null;
        if (curSessionPane != null) {
            curSessionPane.setStyle("-fx-background-color: #F4F4F4");
            oldSession = (Session) curSessionPane.getUserData();

        }
        curSessionPane = (AnchorPane) event.getSource();
        curSessionPane.setStyle("-fx-background-color: #D2D2D2");
        changeDialog(oldSession, (Session) curSessionPane.getUserData());
    }

    /**
     * Used to change the Label for the ongoing session, the history, and the input textArea
     */
    private void changeDialog(Session oldSession, Session newSession) {
        if (newSession == null) {
            if (oldSession != null) {
                oldSession.setInputCache(inputArea.getText());
            }
            nameLabel.setText("");
            inputArea.setText("");
            dialogArea.setText("");
        } else {
            if (oldSession != null) {
                oldSession.setInputCache(inputArea.getText());
            }
            nameLabel.setText(newSession.getTargetUser().getUserID());
            inputArea.setText(newSession.getInputCache());
            dialogArea.setText(newSession.getHistory());
            dialogArea.appendText("");
        }
    }

    /**
     * defines what will the dialogArea do when a session receive a text from a remote user.
     * If the current sessionPane is not equals to the session who gets new text,
     * then nothing happens.
     * If the current SessionPane gets new text, then update the dialogArea.
     */
    private void listenerFire(PropertyChangeEvent evt) {
        // if curSession equals to the session which get new text.
        // Then set dialogArea text
        if (((Session) curSessionPane.getUserData()).getSessionID().equals(evt.getPropertyName())) {
            dialogArea.setText((String) evt.getNewValue());
            dialogArea.appendText("");
        }
    }

    public void closeByOtherUser(String sessionId) {
        AnchorPane sessionPane;

        // iterate the vBox to get the session that equals to Param session
        for (int i = 0; i < vBox.getChildren().size(); i++) {

            // get sessionPane by index
            sessionPane = (AnchorPane) vBox.getChildren().get(i);

            // get session of the sessionPane
            Session tmpSession = (Session) sessionPane.getUserData();

            // if the current sessionId equals to the Param sessionId
            if (tmpSession.getSessionID().equals(sessionId)) {
                // if this sessionPane equals to the curSessionPane
                if (curSessionPane.equals(sessionPane)) {
                    // set the dialog to null
                    changeDialog(tmpSession, null);
                }
                // remove the session pane
                vBox.getChildren().remove(sessionPane);
            }
        }
    }
}
