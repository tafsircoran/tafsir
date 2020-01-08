package com.traore.abasse.tafsir.application.ui.tools;

public class Tool {

    private String prenom,nom,ville,pays,mail;
    private String telephone,commentaire;

    public Tool(String prenom, String nom, String ville, String pays, String mail, String telephone,String commentaire) {
        this.prenom = prenom;
        this.nom = nom;
        this.ville = ville;
        this.pays = pays;
        this.mail = mail;
        this.telephone = telephone;
        this.commentaire = commentaire;
    }


    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getVille() {
        return ville;
    }

    public String getPays() {
        return pays;
    }

    public String getMail() {
        return mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getCommentaire() {
        return commentaire;
    }
}
