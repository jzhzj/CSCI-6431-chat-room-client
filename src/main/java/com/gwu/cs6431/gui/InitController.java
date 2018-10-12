package com.gwu.cs6431.gui;

import com.gwu.cs6431.service.constant.ClientProps;
import com.gwu.cs6431.service.messageHandler.Executable;
import com.gwu.cs6431.service.messageHandler.SignHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class InitController implements Initializable {
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField userIdTxt;
    @FXML
    private TextField passwdTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInButton.setOnAction(event -> {
//            Socket socket;
//            try {
//                socket = new Socket(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT);
//            } catch (IOException e) {
//                // TODO prompt alert
//                Stage stage = (Stage) signInButton.getScene().getWindow();
//                stage.close();
//                return;
//            }


            // In order to test code, do not pass a socket connected with the server
            // Instead, new a socket
            Executable signHandler = new SignHandler(new Socket(), userIdTxt.getText(), passwdTxt.getText());
            if (signHandler.execute()) {
                Stage initStage = (Stage) signInButton.getScene().getWindow();
                initStage.close();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/gwu/cs6431/gui/Main.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        signUpButton.setOnAction(event -> {
            // TODO
        });

        cancelButton.setOnAction(event -> {
            Platform.exit();
        });
    }
}