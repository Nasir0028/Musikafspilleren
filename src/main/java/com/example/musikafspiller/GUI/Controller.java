package com.example.musikafspiller.GUI;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    private MediaPlayer player;

    @FXML
    private ImageView albumCover;

    @FXML
    private Text artistName;
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
    private Button deletePlaylist;
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
    private Slider soundDrag; // Volume slider

    @FXML
    private Button muteBtn;

    @FXML
    private Button newPlay;
    Button newButton = new Button("New...");

    public void play() {
        currentMediaPlayer.play();
    }

    public void pause() {
        currentMediaPlayer.pause();
    }

    public void handlePlayPause() {
        play();
        pause();

        if (currentMediaPlayer == null) {
            
        }
    }

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

    @FXML
    private ListView<?> songsOnPlaylist;

    private MediaPlayer currentMediaPlayer; // Store the currently playing MediaPlayer

    public void initialize() {
        // Initialize song list
        sange = FXCollections.observableArrayList();
        //songs.setItems(sange);

        // Initialize volume slider
        if (soundDrag != null) {
            soundDrag.setMin(0);
            soundDrag.setMax(100);
            soundDrag.setValue(0);

            // Listener to adjust volume
            soundDrag.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.setVolume(newValue.doubleValue() / 100);
                }
            });
        }
    }

    public void handleAddSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Song");

        // Allow only audio files to be selected
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );

        // Allow multiple file selection
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                try {
                    // Convert file path to URI and create a Media object
                    Media media = new Media(file.toURI().toString());

                    // Create a MediaPlayer for the media
                    currentMediaPlayer = new MediaPlayer(media);

                    System.out.println("Added: " + file.getName());

                    // Automatically start playing the first selected file
                    currentMediaPlayer.play();
                    System.out.println("Now Playing: " + file.getName());

                    // Optionally, set a listener to handle playback completion
                    currentMediaPlayer.setOnEndOfMedia(() -> {
                        System.out.println("Finished playing: " + file.getName());
                    });

                    // Set the initial volume for the current media
                    if (soundDrag != null) {
                        currentMediaPlayer.setVolume(soundDrag.getValue() / 100);
                    }
                } catch (Exception e) {
                    System.err.println("Error adding file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }
}
