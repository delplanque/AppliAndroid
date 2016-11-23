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
    protected String Author;
    protected String title;
    protected String isbn;
    protected String resume;
   /* protected String serie;
    protected String genre;
    protected String editeur;
    protected String annee;*/

    public Book(String author, String title, String isbn) {
        super();
        this.Author=author;
        this.title=title;
        this.isbn=isbn;
        this.resume="voici le resume du livre";  //sera initialiser par la suite
    }

    public String getResume(){ return this.resume; }
    public String getAuthor(){
        return this.Author;
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