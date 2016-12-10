package com.example.jordan.booklibrairy.bookSql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.book.ListAuteurs;

/**
 * Created by jordan on 10/12/2016.
 */

public class AuteurBDD {


    private static final String LOG = AuteurBDD.class.getName();

    public static final String TABLE_AUTEUR = "table_auteur";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_ISBN = "isbn";

    private DatabaseHelper dh;

    public AuteurBDD(DatabaseHelper dh) {
        this.dh = dh;
    }

    public Auteur fromCursor(Cursor c) {

        Auteur auteur = new Auteur();
        auteur.setId(c.getLong(c.getColumnIndex(COL_ID)));
        auteur.setNomAuteur(c.getString(c.getColumnIndex(COL_NAME)));
        auteur.setIsbn(c.getString(c.getColumnIndex(COL_ISBN)));
        return auteur;
    }

    public ContentValues toContentValues(Auteur auteur) {

        ContentValues values = new ContentValues();

        values.put(COL_NAME, auteur.getNomAuteur());
        values.put(COL_ISBN, auteur.getIsbn());

        return values;
    }

    public long createAuthor(Auteur auteur) {
        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = toContentValues(auteur);

        return db.insert(TABLE_AUTEUR, null, values);
    }

    public void createAllAuthor(ListAuteurs auteurs) {

        SQLiteDatabase db = dh.getWritableDatabase();

        for(Auteur auteur: auteurs){

            ContentValues values = toContentValues(auteur);

            db.insert(TABLE_AUTEUR, null, values);
        }
    }

    public ListAuteurs getAllAuthorByQuery(String requete) {

        ListAuteurs list = new ListAuteurs();

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if (c.moveToFirst()) {
            do {
                Auteur auteur = fromCursor(c);

                list.addAuteur(auteur);
            } while (c.moveToNext());
        }

        c.close();

        return list;
    }

    public Auteur getAuthorByQuery(String requete) {

        SQLiteDatabase db = dh.getReadableDatabase();
        Log.e(LOG, requete);

        Cursor c = db.rawQuery(requete, null);

        if (c != null) {
            c.moveToFirst();
        }

        Auteur auteur = fromCursor(c);

        c.close();

        return auteur;
    }

    public Auteur getAuthorById(long id) {

        String selectQuery = "SELECT  * FROM " + TABLE_AUTEUR + " WHERE "
                + COL_ID + " = " + id
                ;

        return getAuthorByQuery(selectQuery);
    }


    public ListAuteurs getAllAuthorByIsbn(String isbn) {

        String selectQuery = "SELECT  * FROM " + TABLE_AUTEUR + " WHERE "
                + COL_ISBN + " = " + "'" + isbn + "'"
                ;

        return getAllAuthorByQuery(selectQuery);
    }


    public ListAuteurs getAllAuthor() {

        String selectQuery = "SELECT  * FROM " + TABLE_AUTEUR;

        return getAllAuthorByQuery(selectQuery);
    }

    public long updateAuthorById(Auteur auteur, long id) {
        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = toContentValues(auteur);

        // updating row
        return db.update(TABLE_AUTEUR, values, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void updateAllAuthorByIsbn(ListAuteurs auteurs, String isbn){

        deleteAllAuthorByIsbn(isbn);
        createAllAuthor(auteurs);
    }

    public void deleteAuthorById(long id) {
        SQLiteDatabase db = dh.getWritableDatabase();
        db.delete(TABLE_AUTEUR, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void deleteAllAuthorByIsbn(String isbn){

        SQLiteDatabase db = dh.getWritableDatabase();
        db.delete(TABLE_AUTEUR, COL_ISBN + " = ?",
                new String[]{ isbn});
    }
}
