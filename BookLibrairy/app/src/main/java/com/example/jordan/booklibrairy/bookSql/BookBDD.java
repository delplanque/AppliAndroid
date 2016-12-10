package com.example.jordan.booklibrairy.bookSql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.bookSql.MaBaseSQLite;

import java.util.ArrayList;
import java.util.List;

public class BookBDD {
    private static final String LOG = BookBDD.class.getName();

    public static final String TABLE_LIVRE = "table_livre";

    public static final String COL_ISBN = "isbn";
    public static final String COL_TITRE = "titre";
    public static final String COL_EIDTEUR = "editeur";
    public static final String COL_ANNEE = "annee";
    public static final String COL_RESUME = "resume";
    public static final String COL_GENRE = "genre";
    public static final String COL_COMMENT = "commentaires";
    public static final String COL_IMAGE = "image";

    private DatabaseHelper dh;

    public BookBDD(DatabaseHelper dh) {
        this.dh = dh;
    }

    public Book fromCursor(Cursor c) {

        if(c.getCount() > 0) {
            String isbn = c.getString(c.getColumnIndex(COL_ISBN));
            String titre = c.getString(c.getColumnIndex(COL_TITRE));
            String editeur = c.getString(c.getColumnIndex(COL_EIDTEUR));
            String annee = c.getString(c.getColumnIndex(COL_ANNEE));
            String resume = c.getString(c.getColumnIndex(COL_RESUME));
            String genre = c.getString(c.getColumnIndex(COL_GENRE));
            String commentaire = c.getString(c.getColumnIndex(COL_COMMENT));
            String image = c.getString(c.getColumnIndex(COL_IMAGE));
            Book livre=new Book(titre,isbn,image,resume,editeur,annee,commentaire,genre);

            return livre;
        }
        return null;

    }

    public ContentValues toContentValues(Book livre) {

        ContentValues values = new ContentValues();

        values.put(COL_ISBN, livre.getIsbn());
        values.put(COL_TITRE, livre.getTitle());
        values.put(COL_EIDTEUR, livre.getEditeur());
        values.put(COL_ANNEE, livre.getAnnee());
        values.put(COL_RESUME, livre.getResume());
        values.put(COL_GENRE, livre.getGenre());
        values.put(COL_COMMENT, livre.getCommentaires());
        values.put(COL_IMAGE, livre.getSrcImage());

        return values;
    }

    public long createBook(Book livre) {
        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = toContentValues(livre);

        return db.insert(TABLE_LIVRE, null, values);
    }

    public List<Book> getAllBookByQuery(String requete) {

        List<Book> list = new ArrayList<Book>();

        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor c = db.rawQuery(requete, null);

        if (c.moveToFirst()) {
            do {
                Book entry = fromCursor(c);

                list.add(entry);
            } while (c.moveToNext());
        }

        c.close();

        return list;
    }

    public Book getBookByQuery(String requete) {

        SQLiteDatabase db = dh.getReadableDatabase();
        Log.e(LOG, requete);

        Cursor c = db.rawQuery(requete, null);

        if (c != null) {
            c.moveToFirst();
        }

        Book livre = fromCursor(c);

        c.close();

        return livre;
    }

    public Book getBookByIsbn(String isbn) {

        String selectQuery = "SELECT  * FROM " + TABLE_LIVRE + " WHERE "
                + COL_ISBN + " = " + "'" + isbn + "'"
                ;

        return getBookByQuery(selectQuery);
    }



    public List<Book> getAllBook() {

        String selectQuery = "SELECT  * FROM " + TABLE_LIVRE;

        return getAllBookByQuery(selectQuery);
    }

    public long updateBookByIsbn(Book arg, String isbn) {
        SQLiteDatabase db = dh.getWritableDatabase();

        ContentValues values = toContentValues(arg);

        return db.update(TABLE_LIVRE, values, COL_ISBN + " = ?",
                new String[] { String.valueOf(isbn) });
    }

    public void deleteBookByIsbn(String isbn) {
        SQLiteDatabase db = dh.getWritableDatabase();
        db.delete(TABLE_LIVRE, COL_ISBN + " = ?",
                new String[] { String.valueOf(isbn) });
    }
}