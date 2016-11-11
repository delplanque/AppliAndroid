package com.example.jordan.booklibrairy;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookLibrary {
    protected List<Book> lBooks;

    public BookLibrary(){

        lBooks=new ArrayList<Book>();
    }

    public List<Book> getlBooks() {

        return this.lBooks;
    }

    public void saveBookInCollection(Book nbook){

        if(!lBooks.contains(nbook))
            lBooks.add(nbook);

    }


    public void removeBookInCollection(Book rBook){

        lBooks.remove(rBook);
    }
}
