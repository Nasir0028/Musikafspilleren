package com.example.musikafspiller.GUI;

import java.io.Serializable;

public class Sange implements Serializable {
    private String kunstner;
    private String titel;
    private String genre;
    int varighed;


    Sange(String kunstner, int varighed, String titel, String genre) {
        this.kunstner = kunstner;
        this.varighed = varighed;
        this.titel = titel;
        this.genre = genre;
    }

    public String getKunstner() {
        return kunstner;
    }

    public int getVarighed() {
        return varighed;
    }

    public String getTitel() {
        return titel;
    }

    public String getGenre() {
        return genre;
    }

    public String toString() {
        return kunstner + ", " + varighed + " " + titel + " " + genre;
    }
}