package com.example.jordan.booklibrairy.bookSql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jordan on 10/12/2016.
 */

public class BDD {

    protected SQLiteDatabase db = null;
    protected DatabaseHelper dh = null;

    public BDD(Context pContext) {
        this.dh = new DatabaseHelper(pContext);
        open();
    }

    public SQLiteDatabase open() {
        db = dh.getWritableDatabase();
        return db;
    }

    public void close() {
        db.close();
    }

    public DatabaseHelper getDh(){return dh;}
    public SQLiteDatabase getDb() {
        return db;
    }
}
