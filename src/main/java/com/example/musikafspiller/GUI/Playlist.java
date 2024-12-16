package com.example.musikafspiller.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable {
    private static final long serialVersionUID = 2L;

    private int id;
    private String navn;
    private List<Sange> sangliste = new ArrayList<Sange>();

    public Playlist(String navn) {
        this.navn = navn;
    }

    public List<Sange> getSangliste() {
        return sangliste;
    }

    public void tilknytSange(Sange sang) {
        sangliste.add(sang);
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        String al = "";
        for (Sange s : sangliste) al += " "+s;
        return id+" "+navn+" "+ al;
    }
}
