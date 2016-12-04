package com.example.jordan.booklibrairy.bookSql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.book.Livre;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jordan on 03/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "librairybis.db";

    // Table Names
    private static final String TABLE_LIVRE = "livres";
    private static final String TABLE_AUTEUR = "auteurs";
    private static final String TABLE_LIVRE_AUTEUR = "livres_auteurs";

    // Common column names
    private static final String KEY_ID = "id";

    // Livre Table - column nmaes
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_TITRE = "titre";

    // Auteur Table - column names
    private static final String KEY_AUTEUR_NOM = "nom_auteur";

    // Livre_Auteur Table - column names
    private static final String KEY_LIVRE_ID = "livre_id";
    private static final String KEY_AUTEUR_ID = "auteur_id";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_LIVRE = "CREATE TABLE "
            + TABLE_LIVRE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ISBN + " TEXT," + KEY_TITRE + "TEXT "
             + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_AUTEUR = "CREATE TABLE " + TABLE_AUTEUR
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AUTEUR_NOM + " TEXT"
            +  ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_LIVRE_AUTEUR = "CREATE TABLE "
            + TABLE_LIVRE_AUTEUR + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_LIVRE_ID + " INTEGER," + KEY_AUTEUR_ID + " INTEGER"
            +  ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_LIVRE);
        db.execSQL(CREATE_TABLE_AUTEUR);
        db.execSQL(CREATE_TABLE_LIVRE_AUTEUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVRE_AUTEUR);

        // create new tables
        onCreate(db);
    }



    public long createLivre(Livre livre, long[] auteur_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ISBN, livre.getIsbn());
        values.put(KEY_TITRE, livre.getTitre());


        // insert row
        long livre_id = db.insert(TABLE_LIVRE, null, values);

        // insert tag_ids
        for (long auteur_id : auteur_ids) {
            createLivreAuteur(livre_id, auteur_id);
        }

        return livre_id;
    }

    /**
     * get livre
     */
    public Livre getLivre(long livre_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LIVRE + " WHERE "
                + KEY_ID + " = " + livre_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Livre livre = new Livre();
        livre.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        livre.setTitre((c.getString(c.getColumnIndex(KEY_TITRE))));
        livre.setIsbn((c.getString(c.getColumnIndex(KEY_ISBN))));

        return livre;
    }

    /**
     * getting all livres
     * */
    public List<Livre> getAllLivres() {
        List<Livre> livres = new ArrayList<Livre>();
        String selectQuery = "SELECT  * FROM " + TABLE_LIVRE  ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Livre td = new Livre();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setTitre((c.getString(c.getColumnIndex(KEY_TITRE))));
                td.setIsbn((c.getString(c.getColumnIndex(KEY_ISBN))));
                // adding to todo list
                livres.add(td);
            } while (c.moveToNext());
        }

        return livres;
    }

    /**
     * getting all livre under auteur
     * */
    public List<Livre> getAllLivresByAuteurs(String tag_name) {
        List<Livre> livres = new ArrayList<Livre>();

        String selectQuery = "SELECT  * FROM " + TABLE_LIVRE + " td, "
                + TABLE_AUTEUR + " tg, " + TABLE_LIVRE_AUTEUR + " tt WHERE tg."
                + KEY_AUTEUR_NOM + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_AUTEUR_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_LIVRE_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Livre livre = new Livre();
                livre.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                livre.setTitre((c.getString(c.getColumnIndex(KEY_TITRE))));
                livre.setIsbn((c.getString(c.getColumnIndex(KEY_ISBN))));

                // adding to todo list
                livres.add(livre);
            } while (c.moveToNext());
        }

        return livres;
    }

    /**
     * getting livre count
     */
    public int getLivreCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LIVRE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a livre
     */
    public int updateLivre(Livre livre) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ISBN, livre.getIsbn());
        values.put(KEY_TITRE, livre.getTitre());

        // updating row
        return db.update(TABLE_LIVRE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(livre.getId()) });
    }


    public void deleteLivre(long auteur_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIVRE, KEY_ID + " = ?",
                new String[] { String.valueOf(auteur_id) });
    }


    public long createAuteur(Auteur auteur) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTEUR_NOM, auteur.getNomAuteur());

        // insert row
        long auteur_id = db.insert(TABLE_AUTEUR, null, values);

        return auteur_id;
    }


    public List<Auteur> getAllAuteurs() {
        List<Auteur> auteurs = new ArrayList<Auteur>();
        String selectQuery = "SELECT  * FROM " + TABLE_AUTEUR;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {
                Auteur t = new Auteur();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setNomAuteur(c.getString(c.getColumnIndex(KEY_AUTEUR_NOM)));


                auteurs.add(t);
            } while (c.moveToNext());
        }
        return auteurs;
    }

    public List<Auteur> getAllAuteursByLivre(String titre_livre) {
        List<Auteur> auteurs = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_LIVRE + " td, "
                + TABLE_AUTEUR + " tg, " + TABLE_LIVRE_AUTEUR + " tt WHERE td."
                + KEY_TITRE + " = '" + titre_livre + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_AUTEUR_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_LIVRE_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {
                Auteur t = new Auteur();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setNomAuteur(c.getString(c.getColumnIndex(KEY_AUTEUR_NOM)));

                // adding to tags list
                auteurs.add(t);
            } while (c.moveToNext());
        }
        return auteurs;
    }


    public int updateAuteur(Auteur auteur) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTEUR_NOM, auteur.getNomAuteur());

        // updating row
        return db.update(TABLE_AUTEUR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(auteur.getId()) });
    }


    public void deleteAuteur(Auteur auteur, boolean should_delete_all_) {
        SQLiteDatabase db = this.getWritableDatabase();


        if (should_delete_all_) {
            List<Livre> allTagToDos = getAllLivresByAuteurs(auteur.getNomAuteur());

            for (Livre livre : allTagToDos) {
                deleteLivre(livre.getId());
            }
        }

        db.delete(TABLE_AUTEUR, KEY_ID + " = ?",
                new String[] { String.valueOf(auteur.getId()) });
    }



    public long createLivreAuteur(long livre_id, long auteur_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LIVRE_ID, livre_id);
        values.put(KEY_AUTEUR_ID, auteur_id);

        long id = db.insert(TABLE_LIVRE_AUTEUR, null, values);

        return id;
    }


    public int updateLivreAuteur(long id, long auteur_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTEUR_ID, auteur_id);

        // updating row
        return db.update(TABLE_LIVRE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }



    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}