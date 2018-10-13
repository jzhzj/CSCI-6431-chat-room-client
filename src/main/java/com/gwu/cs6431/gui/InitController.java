package com.gwu.cs6431.gui;

import com.gwu.cs6431.service.constant.ClientProps;
import com.gwu.cs6431.service.messageHandler.Executable;
import com.gwu.cs6431.service.messageHandler.RegHandler;
import com.gwu.cs6431.service.messageHandler.SignHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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
    private PasswordField passwd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInButton.setOnAction(event -> {
            Socket socket = newSocket();
            SignHandler signHandler = new SignHandler(socket, userIdTxt.getText(), passwd.getText());
            if (signHandler.execute()) {
                changeStage();
            } else {
                prompt(Alert.AlertType.ERROR, "Failed", signHandler.getServerFeedback(), "Please try again.");
            }
            signHandler.close();
        });

        signUpButton.setOnAction(event -> {
            if (!checkAccount(userIdTxt.getText(), passwd.getText())) {
                prompt(Alert.AlertType.ERROR, "Wrong Format!"
                        , "You can only use letters and numbers as your User ID and Password."
                        , "User ID should starts with a letter. The length should between 5 to 10." + System.lineSeparator()
                                + "The length of Password should be between 6 to 13.");
                return;
            }
            Socket socket = newSocket();
            RegHandler regHandler = new RegHandler(socket, userIdTxt.getText(), passwd.getText());
            if (regHandler.execute()) {
                prompt(Alert.AlertType.INFORMATION, "Success!", regHandler.getServerFeedback()
                        , "Please remember your User ID and Password :)");
                changeStage();
            } else {
                prompt(Alert.AlertType.ERROR, "Failed", regHandler.getServerFeedback(), "Please try again.");
            }
            regHandler.close();
        });

        cancelButton.setOnAction(event -> Platform.exit());
    }

    private boolean checkAccount(String id, String pd) {
        String idRegex = "^[a-zA-Z][\\w]{4,9}";
        String pdRegex = "[a-zA-Z\\d]{6,13}";
        return id.matches(idRegex) && pd.matches(pdRegex);
    }

    private void changeStage() {
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

    private Socket newSocket() {
        Socket socket;
        try {
            socket = new Socket(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT);
        } catch (IOException e) {
            prompt(Alert.AlertType.ERROR, "Connection Error!"
                    , "Can not connect to server", "Please check your network.");
            Platform.exit();
            return null;
        }
        return socket;
    }

    public static void prompt(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
