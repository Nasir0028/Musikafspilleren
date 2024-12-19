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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.List;

public class Controller {

    private enum RepeatMode {
        NONE,
        SINGLE_SONG,
        PLAYLIST
    }

    private RepeatMode currentRepeatMode = RepeatMode.NONE;

    @FXML
    private Text artistName;

    @FXML
    private Text songPlay;

    @FXML
    private Text songCurrentTimer;

    @FXML
    private Text songEndTimer;

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

    private ObservableList<Sange> songsList;
    private MediaPlayer currentMediaPlayer = null; // Media player for audio playback
    private int currentSongIndex = -1; // Holder styr på den aktuelle sangs position

    private ObservableList<Sange> sangeData;

    private ChangeListener<Duration> currentTimeListener;

    private String formatDuration(Duration duration) {
        if (duration == null || duration.isUnknown()) {
            return "00:00";
        }
        int totalSeconds = (int) Math.floor(duration.toSeconds());
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        String formattedTime = (hours > 0) ?
                String.format("%02d:%02d:%02d", hours, minutes, seconds) :
                String.format("%02d:%02d", minutes, seconds);
        return formattedTime;
    }

    public Controller() {
        playList = FXCollections.observableArrayList();
        songsList = FXCollections.observableArrayList();
    }

    public void initialize() {
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTitle())));
        artistColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getArtist())));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTime())));

        navnColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getNavn())));

        playList = FXCollections.observableArrayList();
        tablePlaylist.setItems(playList);

        sangNavnColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTitle())));
        kunstnerColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getArtist())));

        sangeData = FXCollections.observableArrayList();
        sangePåPlaylist.setItems(sangeData);

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

        if (soundDrag != null) {
            soundDrag.setMin(0);
            soundDrag.setMax(100);
            soundDrag.setValue(0);

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

        setupMediaPlayerListeners();
    }

    private void setupMediaPlayerListeners() {
        if (currentMediaPlayer == null) return;

        currentMediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                String formattedCurrentTime = formatDuration(newValue);
                System.out.println("Updating Current Time: " + formattedCurrentTime);
                songCurrentTimer.setText(formattedCurrentTime);
            });
        });

        currentMediaPlayer.setOnReady(() -> {
            Duration totalDuration = currentMediaPlayer.getMedia().getDuration();
            if (totalDuration != null && !totalDuration.isUnknown()) {
                Platform.runLater(() -> {
                    songEndTimer.setText(formatDuration(totalDuration));
                    musicSlider.setMax(totalDuration.toSeconds());
                });
            } else {
                System.err.println("Media duration is unknown.");
            }
        });

        currentMediaPlayer.getMedia().durationProperty().addListener((obs, oldDur, newDur) -> {
            if (newDur != null && !newDur.isUnknown()) {
                Platform.runLater(() -> {
                    songEndTimer.setText(formatDuration(newDur));
                    musicSlider.setMax(newDur.toSeconds());
                });
            }
        });
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
            System.out.println("Ingen sang er loaded.");
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
            System.out.println("Ingen sang valgt på playlisten.");
        }
    }

    private void handleNextSong() {
        if (currentSongIndex < sangeData.size() - 1) {
            currentSongIndex++;
            playSongAtIndex(currentSongIndex);
        } else {
            if (currentRepeatMode == RepeatMode.PLAYLIST) {
                currentSongIndex = 0;
                playSongAtIndex(currentSongIndex);
            } else {
                System.out.println("Playlisten er slut.");
            }
        }
    }

    @FXML
    void handleShufflePlaylist(ActionEvent event) {
        if (sangeData.size() > 1) {
            java.util.Collections.shuffle(sangeData);
            currentSongIndex = -1;
            System.out.println("Playlist shuffled.");
        } else {
            System.out.println("Ikke nok sange til at shuffle.");
        }
    }

    @FXML
    void handleRepeatPlaylist(ActionEvent event) {
        // Toggle the repeat mode
        switch (currentRepeatMode) {
            case NONE:
                currentRepeatMode = RepeatMode.SINGLE_SONG;  // Set to repeat the current song
                System.out.println("Repeat mode: Single Song");
                break;

            case SINGLE_SONG:
                currentRepeatMode = RepeatMode.PLAYLIST;  // Set to repeat the playlist
                System.out.println("Repeat mode: Playlist");
                break;

            case PLAYLIST:
                currentRepeatMode = RepeatMode.NONE;  // Disable repeat
                System.out.println("Repeat mode: None");
                break;
        }

        updateRepeatBehavior(); // Update behavior based on the current repeat mode
    }

    private void updateRepeatBehavior() {
        if (currentRepeatMode == RepeatMode.SINGLE_SONG) {
            if (currentMediaPlayer != null) {
                // Repeat the current song
                currentMediaPlayer.setOnEndOfMedia(() -> {
                    currentMediaPlayer.seek(Duration.ZERO);
                    currentMediaPlayer.play();
                    System.out.println("Repeating current song.");
                });
            }
        } else if (currentRepeatMode == RepeatMode.PLAYLIST) {
            if (currentMediaPlayer != null) {
                // Repeat the entire playlist
                currentMediaPlayer.setOnEndOfMedia(() -> {
                    if (currentSongIndex >= sangeData.size() - 1) {
                        currentSongIndex = 0; // Go back to the first song
                    } else {
                        currentSongIndex++; // Go to the next song
                    }
                    playSongAtIndex(currentSongIndex); // Play the next song
                    System.out.println("Repeating playlist.");
                });
            }
        } else {
            if (currentMediaPlayer != null) {
                // No repeat
                currentMediaPlayer.setOnEndOfMedia(() -> {
                    System.out.println("Playlist finished, no repeat.");
                });
            }
        }
    }

    private void playSong(Sange song) {
        try {
            String songPath = song.getFilePath();

            songPlay.setText(song.getTitle());
            artistName.setText(song.getArtist());

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

            songCurrentTimer.setText("00:00");
            songEndTimer.setText("00:00");
            musicSlider.setValue(0);

            Media media = new Media(new File(songPath).toURI().toString());
            currentMediaPlayer = new MediaPlayer(media);

            currentTimeListener = (observable, oldValue, newValue) -> {
                Platform.runLater(() -> {
                    String formattedCurrentTime = formatDuration(newValue);
                    songCurrentTimer.setText(formattedCurrentTime);
                    if (!musicSlider.isValueChanging()) {
                        musicSlider.setValue(newValue.toSeconds());
                    }
                });
            };
            currentMediaPlayer.currentTimeProperty().addListener(currentTimeListener);

            currentMediaPlayer.setOnReady(() -> {
                Duration songDuration = currentMediaPlayer.getMedia().getDuration();
                double songDurationInSeconds = songDuration.toSeconds();

                musicSlider.setMax(songDurationInSeconds);
                musicSlider.setValue(0);
                songPlay.setText(song.getTitle());
                artistName.setText(song.getArtist());

                String formattedEndTime = formatDuration(songDuration);
                songEndTimer.setText(formattedEndTime);
            });

            currentMediaPlayer.setOnEndOfMedia(() -> handleNextSong());

            musicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (musicSlider.isValueChanging() && currentMediaPlayer != null) {
                    Duration seekTime = Duration.seconds(newValue.doubleValue());
                    currentMediaPlayer.seek(seekTime);
                }
            });

            currentMediaPlayer.setOnStopped(() -> {
                songCurrentTimer.setText("00:00");
                songEndTimer.setText("00:00");
                musicSlider.setValue(0);
            });

            currentMediaPlayer.play();
            System.out.println("Playing: " + song.getTitle());

        } catch (Exception e) {
            System.err.println("Error playing song: " + song.getTitle());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackwardsSange(ActionEvent event) {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            playSongAtIndex(currentSongIndex);

            Sange selectedSong = sangeData.get(currentSongIndex);
            if (selectedSong != null) {
                songPlay.setText(selectedSong.getTitle());
                artistName.setText(selectedSong.getArtist());
            }

        } else {
            System.out.println("Der er ingen tidligere sang.");
        }
    }

    @FXML
    void handleForwardSange(ActionEvent event) {
        if (currentSongIndex < sangeData.size() - 1) {
            currentSongIndex++;
            playSongAtIndex(currentSongIndex);

            Sange selectedSong = sangeData.get(currentSongIndex);
            if (selectedSong != null) {
                songPlay.setText(selectedSong.getTitle());
                artistName.setText(selectedSong.getArtist());
            }

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
        if (index >= 0 && index < sangeData.size()) {
            Sange selectedSong = sangeData.get(index);
            playSong(selectedSong);
        }
    }
}
