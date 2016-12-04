package com.example.jordan.booklibrairy.book;


public class Auteur {

    int id;
    String nom_auteur;

    // constructors
    public Auteur() {

    }

    public Auteur(String nom_auteur) {
        this.nom_auteur = nom_auteur;
    }

    public Auteur(int id, String nom_auteur) {
        this.id = id;
        this.nom_auteur = nom_auteur;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setNomAuteur(String nom_auteur) {
        this.nom_auteur = nom_auteur;
    }

    // getter
    public int getId() {
        return this.id;
    }

    public String getNomAuteur() {
        return this.nom_auteur;
    }
}
