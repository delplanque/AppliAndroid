package com.example.jordan.booklibrairy.book;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Book {
    //protected ArrayList<String> author=new ArrayList<>();
    //protected ArrayList<String> lAuthor;
    protected String title;
    protected String isbn;
    protected String resume;
    protected String srcImage;
   /* protected String serie;*/
    protected String genre;
    protected String editeur;
    protected String annee;
    protected String commentaires;

public Book(){

}

    public Book(String title, String isbn, String srcImage, String resume, String editeur, String annee, String commentaires, String genre) {
        this.title=title;
        this.isbn=isbn;
        this.srcImage= srcImage ;
        this.resume=resume;
        this.editeur=editeur;
        this.annee=annee;
        this.commentaires=commentaires;
        this.genre=genre;

    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getResume(){ return this.resume; }

    public String getSrcImage(){
        return this.srcImage;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }



}