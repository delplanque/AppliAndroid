package com.example.jordan.booklibrairy.book;


/**
 * Created by jordan on 10/12/2016.
 */

import java.util.ArrayList;


public class ListAuteurs extends ArrayList<Auteur> {

    public void addAuteur(Auteur auteur){

        this.add(auteur);
    }

    public void removeAuteur(Auteur auteur){

        this.remove(auteur);
    }

}
