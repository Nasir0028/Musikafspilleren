package com.example.musikafspiller.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AfspillerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AfspillerApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Musikafspiller");
        stage.setScene(scene);

        /*Controller controller = fxmlLoader.getController();
        controller.loadState();

        stage.setOnCloseRequest(event -> {
            controller.saveState();
        });*/

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}