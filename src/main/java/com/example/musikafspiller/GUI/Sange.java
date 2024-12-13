package com.example.musikafspiller.GUI;

import java.io.Serializable;

public class Sange implements Serializable {
    private String artist;
    private String title;
    private String genre;
    int time;


    Sange(String artist, int time, String title, String genre) {
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

    public String toString() {
        return artist + ", " + time + " " + title + " " + genre;
    }
}