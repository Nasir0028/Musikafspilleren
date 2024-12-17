package com.example.musikafspiller.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Playlist {
    private final String navn;
    private final List<Sange> sangliste;

    public Playlist(String navn) {
        this.navn = navn;
        this.sangliste = new ArrayList<>();
    }

    public String getNavn() {
        return navn;
    }

    public List<Sange> getSangliste() {
        return sangliste;
    }

    public void tilknytSange(Sange sange) {
        sangliste.add(sange);
    }
}
