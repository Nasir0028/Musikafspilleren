package com.example.musikafspiller.GUI;

import java.io.Serializable;

public class Sange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String artist;
    private String title;
    private int time;
    private String filePath;


    public Sange(String artist, int time, String title, String filePath) {
        this.artist = artist;
        this.time = time;
        this.title = title;
        this.filePath = filePath;
    }


    public String getArtist() {
        return artist;
    }

    public int getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }


    public String getFormattedTime() {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    // toString method
    @Override
    public String toString() {
        return artist + ", " + getFormattedTime() + " " + title;
    }
}
