package com.gwu.cs6431.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/gwu/cs6431/client/gui/Init.fxml"));
        Stage stage = new Stage(StageStyle.UNIFIED);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
