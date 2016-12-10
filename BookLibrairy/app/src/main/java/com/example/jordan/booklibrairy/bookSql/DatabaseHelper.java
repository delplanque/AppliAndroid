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

    // Table names
    private static final String TABLE_LIVRE = "table_livre";
    private static final String TABLE_AUTEUR = "table_auteur";

    // Common column names

    private static final String COL_ISBN = "isbn";

    // Table book columns
    private static final String COL_TITRE = "titre";
    private static final String COL_EDITEUR = "editeur";
    private static final String COL_ANNEE = "annee";
    private static final String COL_RESUME = "resume";
    private static final String COL_IMAGE = "image";
    private static final String COL_GENRE = "genre";
    private static final String COL_COMMENT = "commentaires";


    // Table auteur columns
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";


    private static final String CREATE_TABLE_BOOK =
            "CREATE TABLE " + TABLE_LIVRE + " ("
                    +    COL_ISBN + " TEXT NOT NULL, "
                    +    COL_TITRE + " TEXT NOT NULL, "
                    +    COL_EDITEUR + " TEXT NOT NULL, "
                    +    COL_ANNEE + " TEXT NOT NULL, "
                    +    COL_RESUME + " TEXT NOT NULL, "
                    +    COL_GENRE + " TEXT NOT NULL, "
                    +    COL_COMMENT + " TEXT NOT NULL, "
                    +    COL_IMAGE + " TEXT NOT NULL, "

                    +   "PRIMARY KEY(isbn) "
                    +   ")";

    // author table create statement
    private static final String CREATE_TABLE_AUTHOR =
            "CREATE TABLE " + TABLE_AUTEUR + " ("
                    +    COL_ID + " INTEGER, "
                    +    COL_NAME + " TEXT NOT NULL, "
                    +    COL_ISBN + " TEXT NOT NULL, "
                    +   "FOREIGN KEY (isbn) "
                    +       "REFERENCES table_livre(isbn) "
                    +       "ON DELETE CASCADE "
                    +       "ON UPDATE CASCADE "
                    +   ", "

                    +   "PRIMARY KEY(id) "
                    +   ")";








    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_AUTHOR);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTEUR);


        // create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}