package com.example.jordan.booklibrairy.bookSql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.bookSql.MaBaseSQLite;

import java.util.ArrayList;

public class BookBDD {
    protected ArrayList<Book> Lbooks=new ArrayList<>();
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "librairy.db";

    private static final String TABLE_LIVRES = "table_livres";

    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_ISBN = "ISBN";
    private static final int NUM_COL_ISBN = 1;
    private static final String COL_TITRE = "Titre";
    private static final int NUM_COL_TITRE = 2;
    private static final String COL_AUTEUR = "Auteur";
    private static final int NUM_COL_AUTEUR = 3;
    private static final String COL_RESUME = "Resume";
    private static final int NUM_COL_RESUME = 4;
    private static final String COL_IMAGE = "Image";
    private static final int NUM_COL_IMAGE = 5;

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public BookBDD(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertLivre(Book livre){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_ISBN, livre.getIsbn());
        values.put(COL_TITRE, livre.getTitle());
        values.put(COL_AUTEUR, livre.getAuthor());
        values.put(COL_RESUME, livre.getResume());
        values.put(COL_IMAGE, livre.getSrcImage());

        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_LIVRES, null, values);
    }

    public int updateLivre(int id, Book livre){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_ISBN, livre.getIsbn());
        values.put(COL_TITRE, livre.getTitle());
        values.put(COL_AUTEUR, livre.getAuthor());
        values.put(COL_RESUME, livre.getResume());
        values.put(COL_IMAGE, livre.getSrcImage());
        return bdd.update(TABLE_LIVRES, values, COL_ID + " = " +id, null);
    }

    public int removeLivreWithID(int id){
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_LIVRES, COL_ID + " = " +id, null);
    }

    public Book getLivreWithTitre(String titre){
        //Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_LIVRES, new String[] {COL_ID, COL_ISBN, COL_TITRE ,COL_AUTEUR ,COL_RESUME ,COL_IMAGE}, COL_TITRE + " LIKE \"" + titre +"\"", null, null, null, null);
        return cursorToLivre(c);
    }

    public ArrayList<Book> getAllBooks(){

        Cursor c = bdd.rawQuery("select *  from "+TABLE_LIVRES,null);
        while (c.moveToNext()) {
            String aut =  c.getString(NUM_COL_AUTEUR);
            String ti  =  c.getString(NUM_COL_TITRE);
            String is  =  c.getString(NUM_COL_ISBN);
            String res  =  c.getString(NUM_COL_RESUME);
            String img  =  c.getString(NUM_COL_IMAGE);
            Book b=new Book(aut,ti,is,img,res);
            Lbooks.add(b);

        }

        c.close();
        return Lbooks;
    }
    //Cette méthode permet de convertir un cursor en un livre
    private Book cursorToLivre(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Book livre = new Book(c.getString(NUM_COL_AUTEUR),c.getString(NUM_COL_TITRE),c.getString(NUM_COL_ISBN),c.getString(NUM_COL_IMAGE),c.getString(NUM_COL_RESUME));
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return livre;
    }
}