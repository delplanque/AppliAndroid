package com.example.jordan.booklibrairy;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Book {
    protected String author;
    protected String title;
    protected String isbn;
    protected String resume;


    public Book(String author, String title, String isbn) {
        super();
        this.author=author;
        this.title=title;
        this.isbn=isbn;
        this.resume="voici le resume du livre";  //sera initialiser par la suite
    }

    public String getResume(){ return this.resume; }
    public String getAuthor(){
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
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