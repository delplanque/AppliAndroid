package com.example.jordan.booklibrairy.book;

import java.util.ArrayList;
import java.util.List;

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
