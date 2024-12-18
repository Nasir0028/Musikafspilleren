package com.example.musikafspiller.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
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
        Sange s = songsTable.getSelectionModel().getSelectedItem();
        p.tilknytSange(s);
        sangeData.add(s);
    }

    @FXML
    void handleClearPlaylist(ActionEvent event) {
        int selectedIndex = tablePlaylist.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tablePlaylist.getItems().remove(selectedIndex);
        }
    }

    @FXML
    void handleClearSange(ActionEvent event) {
        int selectedIndex = songsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            songsTable.getItems().remove(selectedIndex);
        }
    }

    @FXML
    void handleClearSangePlaylist(ActionEvent event) {
        int selectedIndex = sangePåPlaylist.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            sangePåPlaylist.getItems().remove(selectedIndex);
        }
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

    @FXML
    void handlePlayPause(ActionEvent event) {
        if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            currentMediaPlayer.pause();
        } else {
            currentMediaPlayer.play();
        }
    }

    @FXML
    public void handleAddSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Songs");


        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                try {

                    Media media = new Media(file.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);


                    final String[] title = {file.getName()};
                    final String[] artist = {"?"};
                    final int[] timeInSeconds = {0};


                    media.getMetadata().addListener((MapChangeListener<? super String, ? super Object>) (change) -> {
                        media.getMetadata().forEach((key, value) -> {
                            if (key.equals("title")) {
                                title[0] = value.toString();
                            } else if (key.equals("artist")) {
                                artist[0] = value.toString();
                            }
                        });
                    });


                    mediaPlayer.setOnReady(() -> {
                        Duration duration = media.getDuration();
                        timeInSeconds[0] = (int) duration.toSeconds();


                        int minutes = timeInSeconds[0] / 60;
                        int seconds = timeInSeconds[0] % 60;
                        String formattedTime = String.format("%d:%02d", minutes, seconds);


                        songsList.add(new Sange(artist[0], timeInSeconds[0], title[0]));
                        System.out.println("Added song: " + title[0] + ", Artist: " + artist[0] + ", Duration: " + formattedTime);


                        if (currentMediaPlayer == null) {
                            currentMediaPlayer = mediaPlayer;
                        }
                    });

                } catch (Exception e) {
                    System.err.println("Error adding file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }




    }
}
