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

    public String toString(){

        String allname = "";
        int i = 0;
        for(Auteur auteur : this){

            if(i != 0){
                allname += " , ";
            }
            allname += auteur.getNomAuteur();
            i=1;

        }
        return allname;
    }


}
