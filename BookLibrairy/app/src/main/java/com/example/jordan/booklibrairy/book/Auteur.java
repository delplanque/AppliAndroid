package com.example.jordan.booklibrairy.book;


public class Auteur {

    long id;
    String nom_auteur;
    String isbn;

    // constructors
    public Auteur() {

    }

    public Auteur(String nom_auteur, String isbn) {
        this.isbn=isbn;
        this.nom_auteur = nom_auteur;
    }


    // setter
    public void setId(long id) {
        this.id = id;
    }

    public void setNomAuteur(String nom_auteur) {
        this.nom_auteur = nom_auteur;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    // getter
    public long getId() {
        return this.id;
    }

    public String getNomAuteur() {
        return this.nom_auteur;
    }

    public String getIsbn(){
        return this.isbn;
    }
}
