package com.example.musikafspiller.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private Button addSong;
    Button addSongbutton = new Button("Add Song");

    @FXML
    private ImageView albumCover;

    @FXML
    private Text artistPlay;
    Button artistPlaybutton = new Button("Artist Play");

    @FXML
    private Button backwards;

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
    private Button forward;

    @FXML
    private Slider musicPlayer;

    @FXML
    private Button muteBtn;

    @FXML
    private Button newPlay;
    Button newButton = new Button("New...");

    @FXML
    private Button playBtn;

    @FXML
    private TableView<?> playlist;

    @FXML
    private Button removeSong;
    Button removeButton = new Button("Remove...");

    @FXML
    private Button repeatPlaylist;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;
    Button searchButton = new Button("Search...");

    @FXML
    private Button shuffle;

    @FXML
    private Button songOnPlay;
    Button songOnButton = new Button("On Play...");

    @FXML
    private Text songPlay;
    Button songOnButton2 = new Button("On Play...");

    @FXML
    private Text songTimer;

    @FXML
    private TableView<Sange> songs;
    private ObservableList<Sange> sange;
    public void initialize() {
        sange = FXCollections.observableArrayList();
        songs.setItems(sange);
    }

    @FXML
    private ListView<?> songsOnPlaylist;
    private ObservableList<?>

    @FXML
    private Slider soundDrag;
}
