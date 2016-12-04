package com.example.jordan.booklibrairy.book;

import java.util.ArrayList;
import java.util.List;

public class Livre {

    protected int id;
    protected String titre;
    protected String isbn;
   // protected ArrayList<Auteur> lAuteurs =new ArrayList<Auteur>();
    // constructors
    public Livre() {
    }

    public Livre(String titre, String isbn) {
        this.titre = titre;
        this.isbn = isbn;
    }

    public Livre(int id, String titre, String isbn) {
        this.id = id;
        this.titre = titre;
        this.isbn = isbn;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /*public void setListAuteur(Auteur auteur) {
        this.lAuteurs.add(auteur);
    }*/

    // getters
    public long getId() {
        return this.id;
    }

  /*  public ArrayList<Auteur> getListAuteurs() {
        return this.lAuteurs;
    }*/

    public String getTitre() {
        return this.titre;
    }

    public String getIsbn() {
        return this.isbn;
    }
}