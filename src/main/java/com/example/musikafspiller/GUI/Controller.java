package com.example.musikafspiller.GUI;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Slider soundDrag;

    @FXML
    private Slider musicSlider;

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
    private int currentSongIndex = -1; // Holder styr på den aktuelle sangs position

    private ObservableList<Sange> sangeData;

    private ChangeListener<Duration> currentTimeListener;

    public Controller() {
        playList = FXCollections.observableArrayList();
        songsList = FXCollections.observableArrayList();
    }

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

        sangePåPlaylist.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Sange selectedSong = sangePåPlaylist.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    playSong(selectedSong);
                }
            }
        });

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

        if (musicSlider != null) {
            musicSlider.setMin(0);
            musicSlider.setValue(0);

            musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (musicSlider.isValueChanging() && currentMediaPlayer != null) {
                    Duration seekTime = Duration.seconds(newValue.doubleValue());
                    currentMediaPlayer.seek(seekTime);
                }
            });
        }
    }

    @FXML
    void handleMute(ActionEvent event) {
        if (currentMediaPlayer != null) {
            if (currentMediaPlayer.isMute()) {
                currentMediaPlayer.setMute(false);
        }
            else {
                currentMediaPlayer.setMute(true);}
        }
        else {
            System.out.println("No song is currently loaded.");
        }
    }

    @FXML
    void handlePlayPause(ActionEvent event) {
        Sange selectedSong = sangePåPlaylist.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            if (currentMediaPlayer != null && currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                currentMediaPlayer.pause();
            } else {
                playSong(selectedSong);
            }
        } else {
            System.out.println("No song selected in the playlist.");
        }
    }

    private void handleNextSong() {
        if (currentSongIndex < songsList.size() - 1) {
            playSongAtIndex(currentSongIndex + 1);
        } else {
            System.out.println("End of playlist.");
        }
    }

    private void playSong(Sange song) {
        try {
            String songPath = song.getFilePath();

            if (currentMediaPlayer != null && currentMediaPlayer.getMedia().getSource().equals(new File(songPath).toURI().toString())) {
                currentMediaPlayer.play();
                return;
            }

            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
                if (currentTimeListener != null) {
                    currentMediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
                }
            }

            Media media = new Media(new File(songPath).toURI().toString());
            currentMediaPlayer = new MediaPlayer(media);

            currentMediaPlayer.setOnReady(() -> {
                musicSlider.setMax(currentMediaPlayer.getMedia().getDuration().toSeconds());
                musicSlider.setValue(0); // Reset slider to the start
            });

            currentTimeListener = (observable, oldValue, newValue) -> {
                Platform.runLater(() -> {
                    if (!musicSlider.isValueChanging()) {
                        musicSlider.setValue(newValue.toSeconds());
                    }
                });
            };
            currentMediaPlayer.currentTimeProperty().addListener(currentTimeListener);

            musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (musicSlider.isValueChanging() && currentMediaPlayer != null) {
                    Duration seekTime = Duration.seconds(newValue.doubleValue());
                    currentMediaPlayer.seek(seekTime);
                }
            });

            currentMediaPlayer.setOnEndOfMedia(() -> handleNextSong());

            currentMediaPlayer.play();
            System.out.println("Playing: " + song.getTitle());
        } catch (Exception e) {
            System.err.println("Error playing song: " + song.getTitle());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackwardsSange(ActionEvent event) {
        if (!songsList.isEmpty() && currentSongIndex > 0) {
            currentSongIndex--;
            playSongAtIndex(currentSongIndex);
        } else {
            System.out.println("Der er ingen tidligere sang.");
        }
    }

    @FXML
    void handleForwardSange(ActionEvent event) {
        if (!songsList.isEmpty() && currentSongIndex < songsList.size() - 1) {
            currentSongIndex++;
            playSongAtIndex(currentSongIndex);
        } else {
            System.out.println("Der er ingen næste sang.");
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
                            if (key.equalsIgnoreCase("title")) {
                                title[0] = value.toString();
                            } else if (key.equalsIgnoreCase("artist")) {
                                artist[0] = value.toString();
                            }
                        });
                    });

                    mediaPlayer.setOnReady(() -> {
                        Duration duration = media.getDuration();
                        timeInSeconds[0] = (int) duration.toSeconds();

                        Sange newSong = new Sange(artist[0], timeInSeconds[0], title[0], file.getAbsolutePath());
                        songsList.add(newSong);

                        System.out.println("Added song: " + newSong.getTitle() + ", Artist: " + newSong.getArtist() + ", Duration: " + newSong.getFormattedTime());

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

    private void playSongAtIndex(int index) {
        if (index >= 0 && index < songsList.size()) {
            Sange selectedSong = songsList.get(index);
            String songPath = selectedSong.getFilePath(); // Antag, at Sange-klassen har en metode til at få filstien

            try {
                // Stop den nuværende sang, hvis en afspilles
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                }

                // Opret en ny MediaPlayer til den valgte sang
                Media media = new Media(new File(songPath).toURI().toString());
                currentMediaPlayer = new MediaPlayer(media);

                currentMediaPlayer.play();
                System.out.println("Afspiller: " + selectedSong.getTitle());
            } catch (Exception e) {
                System.err.println("Fejl under afspilning af sang: " + selectedSong.getTitle());
                e.printStackTrace();
            }
        }
    }
}
