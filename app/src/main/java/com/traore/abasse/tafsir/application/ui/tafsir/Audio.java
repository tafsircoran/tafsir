package com.traore.abasse.tafsir.application.ui.tafsir;

public class Audio {

    private String titre,path,duration;

    public Audio(String titre, String path,String duration) {
        this.titre = titre;
        this.path = path;
        this.duration = duration;
    }

    public String getTitre() {
        return titre;
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }


}
