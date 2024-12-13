package com.example.musikafspiller.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Controller {

    @FXML
    private TableView<Sange> songsTable;

    @FXML
    private TableColumn<Sange, String> titleColumn;

    @FXML
    private TableColumn<Sange, String> artistColumn;

    @FXML
    private TableColumn<Sange, String> genreColumn;

    @FXML
    private TableColumn<Sange, Integer> timeColumn;

    private ObservableList<Sange> songsList; // Backing data for the TableView
    private MediaPlayer currentMediaPlayer; // Media player for audio playback

    public void initialize() {
        // Initialize columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titel"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("kunstner"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("varighed"));

        // Initialize ObservableList and bind it to the TableView
        songsList = FXCollections.observableArrayList();
        songsTable.setItems(songsList);
    }

    public void play(){
        currentMediaPlayer.play();
    }
public void pause(){
        currentMediaPlayer.pause();
}

public void handlePlayPause(){
        play();
        pause();
}

    @FXML
    public void handleAddSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Songs");

        // Allow only audio files to be selected
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav")
        );

        // Allow multiple file selection
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                try {
                    // Default placeholders for song details
                    String titel = file.getName(); // Use file name as the title
                    String kunstner = "Ukendt Kunstner"; // Placeholder artist name
                    String genre = "Ukendt Genre"; // Placeholder genre
                    int varighed = 0; // Placeholder duration

                    // Add the song to the ObservableList
                    songsList.add(new Sange(kunstner, varighed, titel, genre));
                    System.out.println("Added song: " + titel);

                    // Prepare the MediaPlayer for the first song
                    if (currentMediaPlayer == null) {
                        Media media = new Media(file.toURI().toString());
                        currentMediaPlayer = new MediaPlayer(media);
                    }
                } catch (Exception e) {
                    System.err.println("Error adding file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }
}
