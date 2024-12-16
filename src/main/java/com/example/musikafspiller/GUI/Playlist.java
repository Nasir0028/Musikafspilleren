package com.example.musikafspiller.GUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable {
    private static final long serialVersionUID = 2L;

    private int id;
    private String navn;
    private List<Sange> sangliste = new ArrayList<Sange>();

    public Playlist(int id, String navn) {
        this.id = id;
        this.navn = navn;
    }

    public void tilknytSange(Sange sang) {
        sangliste.add(sang);
    }

    @Override
    public String toString() {
        String al = "";
        for (Sange s : sangliste) al += " "+s;
        return id+" "+navn+" "+ al;
    }
}
