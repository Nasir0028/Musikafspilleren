package com.example.musikafspiller.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TableView<Playlist> tablePlaylist;

    @FXML
    private TableColumn<Playlist, String> navnColumn;

    @FXML
    private TextField playlistInput;

    @FXML
    private TableView<Sange> songsTable;

    @FXML
    private TableColumn<Sange, String> titleColumn;

    @FXML
    private TableColumn<Sange, String> artistColumn;

    @FXML
    private TableColumn<Sange, String> timeColumn;

    @FXML
    private TextField sangNavnInput;

    @FXML
    private TextField kunstnerInput;

    @FXML
    private TableView<Sange> sangePåPlaylist;

    @FXML
    private TableColumn<Sange, String> sangNavnColumn;

    @FXML
    private TableColumn<Sange, String> kunstnerColumn;

    private ObservableList<Playlist> playList;
    private ObservableList<Sange> songsList;
    private ObservableList<Sange> sangeData;

    private MediaPlayer currentMediaPlayer;

    public void initialize() {
        // Initialize playlist table
        playList = FXCollections.observableArrayList();
        tablePlaylist.setItems(playList);
        navnColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNavn()));

        // Initialize songs table
        songsList = FXCollections.observableArrayList();
        songsTable.setItems(songsList);
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        artistColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist()));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTime())));

        // Initialize songs in playlist table
        sangeData = FXCollections.observableArrayList();
        sangePåPlaylist.setItems(sangeData);
        sangNavnColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        kunstnerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist()));

        // Add listener for playlist selection
        tablePlaylist.getSelectionModel().selectedItemProperty().addListener((obs, oldPlaylist, newPlaylist) -> {
            if (newPlaylist != null) {
                sangeData.setAll(newPlaylist.getSangliste());
            } else {
                sangeData.clear();
            }
        });
    }

    @FXML
    void tilføjPlaylist(ActionEvent event) {
        String playlistName = playlistInput.getText().trim();
        if (!playlistName.isEmpty()) {
            playList.add(new Playlist(playlistName));
            playlistInput.clear();
        } else {
            showAlert("Fejl", "Playlist navn kan ikke være tomt.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void tilføjSangPlaylist(ActionEvent event) {
        Playlist selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            String sangNavn = sangNavnInput.getText().trim();
            String kunstner = kunstnerInput.getText().trim();

            if (!sangNavn.isEmpty() && !kunstner.isEmpty()) {
                Sange newSong = new Sange(kunstner, 0, sangNavn, "Ukendt Genre");
                selectedPlaylist.tilknytSange(newSong);
                sangeData.add(newSong);
                sangNavnInput.clear();
                kunstnerInput.clear();
            } else {
                showAlert("Fejl", "Sang navn og kunstner skal udfyldes.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Fejl", "Vælg en playlist først.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleAddSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Songs");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                try {
                    String titel = file.getName();
                    String kunstner = "Ukendt Kunstner";
                    String genre = "Ukendt Genre";
                    int varighed = 0;

                    songsList.add(new Sange(kunstner, varighed, titel, genre));

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

    public void play() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.play();
        }
    }

    public void pause() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.pause();
        }
    }

    public void handlePlayPause() {
        if (currentMediaPlayer != null) {
            if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                pause();
            } else {
                play();
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



}

