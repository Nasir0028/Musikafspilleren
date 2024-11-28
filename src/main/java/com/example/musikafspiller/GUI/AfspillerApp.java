package com.example.musikafspiller.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AfspillerApp {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}