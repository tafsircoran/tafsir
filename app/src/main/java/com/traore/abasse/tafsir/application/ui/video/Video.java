package com.traore.abasse.tafsir.application.ui.video;

class Video {


    private String titre,path,imagepath;

    public Video (){}

    public Video(String titre, String path,String imagepath) {
        this.titre = titre;
        this.path = path;
        this.imagepath = imagepath;
    }


    public String getTitre() {
        return titre;
    }

    public String getPath() {
        return path;
    }

    public String getImagepath() {
        return imagepath;
    }
}
