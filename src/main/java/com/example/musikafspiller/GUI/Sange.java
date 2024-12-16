package com.example.musikafspiller.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sange implements Serializable {
    private static final long serialVersionUID = 1L;

    private String artist;
    private String title;
    int time;


    Sange(String artist, int time, String title, String genre) {
        this.artist = artist;
        this.time = time;
        this.title = title;
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

    public String toString() {
        return artist + ", " + time + " " + title;
    }
}