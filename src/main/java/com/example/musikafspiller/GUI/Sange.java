package com.example.musikafspiller.GUI;

import java.io.Serializable;

public class Sange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String artist;
    private String title;
    private int time;

    // Konstruktør
    public Sange(String artist, int time, String title) {
        this.artist = artist;
        this.time = time;
        this.title = title;
    }

    // Getters
    public String getArtist() {
        return artist;
    }

    public int getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    // toString-metode
    @Override
    public String toString() {
        return artist + ", " + time + " sekunder, " + title;
    }
}
