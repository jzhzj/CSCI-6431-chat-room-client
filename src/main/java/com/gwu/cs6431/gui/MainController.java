package com.gwu.cs6431.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button quitButton;
    @FXML
    private Button inviteButton;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quitButton.setOnAction(event -> quitBtnAction());

        inviteButton.setOnAction(event -> invtBtnAction());
    }

    private void quitBtnAction() {
        System.out.println("quit");
    }

    private void invtBtnAction() {
        System.out.println("invite");
    }

    private void sendBtnAction() {

    }
}
