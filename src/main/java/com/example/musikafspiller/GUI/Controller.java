package com.example.musikafspiller.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private Button addSong;
Button addSongbutton = new Button("Add Song");

    @FXML
    private Text artistPlay;
Button artistPlaybutton = new Button("Artist Play");
    @FXML
    private Button btnDown;
Button btnDownbutton = new Button("Down");
    @FXML
    private Button btnUp;
Button btnUpbutton = new Button("Up");
    @FXML
    private Button deletePlay;
Button deletePlaybutton = new Button("Delete Play");

    @FXML
    private Button deleteSong;
    Button deleteButton = new Button("Delete...");

    @FXML
    private Button editPlay;

    Button editPlaybutton = new Button("Edit Play");


    @FXML
    private Button editSong;
     Button editButton2 = new Button("Edit...");
    @FXML
    private Button newPlay;
 Button newButton = new Button("New...");
    @FXML
    private Button removeSong;
Button removeButton = new Button("Remove...");
    @FXML
    private Button searchBtn;
Button searchButton = new Button("Search...");
    @FXML
    private Button songOnPlay;
Button songOnButton = new Button("On Play...");
    @FXML
    private Text songPlay;
Button songOnButton2 = new Button("On Play...");
}
