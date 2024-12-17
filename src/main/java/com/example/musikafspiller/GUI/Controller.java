package com.example.musikafspiller.GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private ObservableList<Playlist> playList;
    @FXML
    private TableView<Playlist> tablePlaylist = new TableView<>(playList);

    @FXML
    private TableColumn<Playlist, String> navnColumn;

    @FXML
    private TextField playlistInput;

    @FXML
    private TableView<Sange> sangePåPlaylist;

    @FXML
    void tilføjPlaylist(ActionEvent event) {
        playList.add(new Playlist(playlistInput.getText()));
    }

    //!!!!!!!!! Erik skal hjælpe !!!!!!!!!!!!
    @FXML
    void tilføjSangPlaylist(ActionEvent event) {
        tablePlaylist.getSelectionModel().selectedItemProperty().addListener((obs, oldPlaylist, newPlaylist) -> {
            if (newPlaylist != null) {
                sangeData.setAll(newPlaylist.getSangliste());
            }
            else {
                sangeData.clear();
            }
        });

        Playlist p = tablePlaylist.getSelectionModel().getSelectedItem();
        if (p != null) {
            Sange s = new Sange(sangNavnColumn.getText(), kunstnerColumn.getText());
            p.tilknytSange(s);
            sangeData.add(s);
        }

        sangNavnColumn.clear();
        kunstnerColumn.clear();


        
        /*Sange s = songsTable.getSelectionModel().getSelectedItem();
        Playlist p = tablePlaylist.getSelectionModel().getSelectedItem();
        }*/
    }



    @FXML
    private TableView<Sange> songsTable;

    @FXML
    private TableColumn<Sange, String> titleColumn;

    @FXML
    private TableColumn<Sange, String> artistColumn;

    @FXML
    private TableColumn<Sange, String> timeColumn;

    @FXML
    private TableColumn<Sange, String> sangNavnColumn;

    @FXML
    private TableColumn<Sange, String> kunstnerColumn;

    private ObservableList<Sange> songsList; // Backing data for the TableView
    private MediaPlayer currentMediaPlayer = null; // Media player for audio playback

    private ObservableList<Sange> sangeData;

    public void initialize() {
        // Initialize sange columns
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTitle())));
        artistColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getArtist())));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTime())));

        // playlist column
        navnColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getNavn())));

        playList = FXCollections.observableArrayList();
        tablePlaylist.setItems(playList);

        sangNavnColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTitle())));
        kunstnerColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getArtist())));

        sangeData = FXCollections.observableArrayList();
        sangePåPlaylist.setItems(sangeData);


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
