package com.example.musikafspiller.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Sange {
    private final String artist;
    private final int time;
    private final String title;
    private final String genre;

    public Sange(String artist, int time, String title, String genre) {
        this.artist = artist;
        this.time = time;
        this.title = title;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }
}
