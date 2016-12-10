package com.example.jordan.booklibrairy.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jordan.booklibrairy.R;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.ListAuteurs;
import com.example.jordan.booklibrairy.bookSql.AuteurBDD;
import com.example.jordan.booklibrairy.bookSql.BDD;
import com.example.jordan.booklibrairy.bookSql.BookBDD;

public class detail_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_abook);

        ImageView imag_detail_Book = (ImageView) findViewById(R.id.imgdetail);
        TextView textView_author = (TextView) findViewById(R.id.autordetail);
        TextView textView_title = (TextView) findViewById(R.id.titledetail);
        TextView textView_isbn = (TextView) findViewById(R.id.isbndetail);
        TextView textView_resume= (TextView) findViewById(R.id.resumedetail);
        Book bookdetail=new Book();
        String res="";
        Bundle extras = getIntent().getExtras();

        String auteur ="";
        String isbn= "";
        String titre= "";
        String resume= "";

        if (extras != null) {

            //---------Récupère informations------------

            isbn =extras.getString("isbn");

            final BDD bdd = new BDD(this);
            //Création d'une instance de ma classe LivresBDD



            //On ouvre la base de données pour écrire dedans
            bdd.open();

            BookBDD bookbdd=new BookBDD(bdd.getDh());
            bookdetail= bookbdd.getBookByIsbn(isbn);

            AuteurBDD auteurbdd=new AuteurBDD(bdd.getDh());
            ListAuteurs listeAuteurs = auteurbdd.getAllAuthorByIsbn(isbn);
            res= listeAuteurs.toString();

            bdd.close();
        }
        imag_detail_Book.setImageResource(R.mipmap.ic_launcher);
        textView_author.setText(textView_author.getText() + res);
        textView_title.setText(textView_title.getText() +bookdetail.getTitle());
        textView_isbn.setText(textView_isbn.getText() + isbn);
        textView_resume.setText(textView_resume.getText() + bookdetail.getResume());

    }

}
