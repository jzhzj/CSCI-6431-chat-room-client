package com.gwu.cs6431.gui;

import com.gwu.cs6431.service.io.SocketFactory;
import com.gwu.cs6431.service.io.listener.Listener;
import com.gwu.cs6431.service.messageHandler.InvtHandler;
import com.gwu.cs6431.service.session.Session;
import com.gwu.cs6431.service.session.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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

    private AnchorPane curSessionPane;

    private boolean initialized;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quitButton.setOnAction(event -> quitBtnAction());

        inviteButton.setOnAction(event -> invtBtnAction());

        // set dialogArea autoscroll
        dialogArea.textProperty().addListener((observable, oldValue, newValue) -> dialogArea.setScrollTop(Double.MAX_VALUE));

        closeButton.setOnAction(event -> closeBtnAction());

        sendButton.setOnAction(event -> sendBtnAction());

        // TODO uncomment this later
//        Listener.getInstance().setMainController(this);
//        new Thread(Listener.getInstance()).start();
    }

    /**
     * TODO close window
     */
    private void quitBtnAction() {
        vBox.getChildren().forEach(node -> ((Session) node.getUserData()).close());
        vBox.getChildren().remove(0, vBox.getChildren().size());
        // TODO close window
    }

    /**
     * TODO Still testing
     */
    private void invtBtnAction() {
        if (!initialized) {
            KeyCombination kc = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);
            sendButton.getScene().getAccelerators().put(kc, () -> sendBtnAction());
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
            if (result2.isPresent() && !result2.get().equals("")) {
                // TODO revise socket later
                new Thread(new InvtHandler(this, null, User.getClientUser().getUserID(), targetUser, result2.get())).start();
            } else {
                // TODO revise socket later
                new Thread(new InvtHandler(this, null, User.getClientUser().getUserID(), targetUser)).start();
            }
        });
    }

    /**
     * Defines what happens when the close button is clicked.
     * If the current Session Pane is not null, then this session will be closed.
     */
    private void closeBtnAction() {
        if (curSessionPane == null)
            return;
        changeDialog((Session) curSessionPane.getUserData(), null);
        vBox.getChildren().remove(curSessionPane);
        ((Session) curSessionPane.getUserData()).close();
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
        session.addListener(evt -> listenerFire(evt));
        Label name = new Label(session.getTargetUser().getUserID());
        name.setFont(Font.font("Optima", 16));
        name.setLayoutX(15);
        name.setLayoutY(20);
        Line line = new Line(0, 70, 200, 70);
        line.setStroke(Paint.valueOf("#DADADA"));
        AnchorPane ap = new AnchorPane(name, line);
        ap.setUserData(session);
        ap.setPrefHeight(70);
        ap.setOnMouseClicked(event -> sessionPaneOnMouseClicked(event));
        vBox.getChildren().add(ap);
    }

    /**
     * handles when a session pane is clicked
     */
    private void sessionPaneOnMouseClicked(MouseEvent event) {
        Session oldSesson = null;
        if (curSessionPane != null) {
            curSessionPane.setStyle("-fx-background-color: #F4F4F4");
            oldSesson = (Session) curSessionPane.getUserData();

        }
        curSessionPane = (AnchorPane) event.getSource();
        curSessionPane.setStyle("-fx-background-color: #D2D2D2");
        changeDialog(oldSesson, (Session) curSessionPane.getUserData());
    }

    /**
     * Used to change the Label for the ongoing session, the history, and the input textArea
     */
    private void changeDialog(Session oldSession, Session newSession) {
        if (newSession == null) {
            if (oldSession != null)
                oldSession.setInputCache(inputArea.getText());
            nameLabel.setText("");
            inputArea.setText("");
            dialogArea.setText("");
        } else {
            if (oldSession != null)
                oldSession.setInputCache(inputArea.getText());
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
}
