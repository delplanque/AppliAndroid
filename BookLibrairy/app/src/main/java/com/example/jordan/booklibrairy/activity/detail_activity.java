package com.example.jordan.booklibrairy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jordan.booklibrairy.R;
import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.ListAuteurs;
import com.example.jordan.booklibrairy.bookSql.AuteurBDD;
import com.example.jordan.booklibrairy.bookSql.BDD;
import com.example.jordan.booklibrairy.bookSql.BookBDD;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class detail_activity extends AppCompatActivity {
    String isbn= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_abook);

        ImageView imag_detail_Book = (ImageView) findViewById(R.id.imgdetail);
        TextView textView_author = (TextView) findViewById(R.id.autordetail);
        TextView textView_title = (TextView) findViewById(R.id.titledetail);
        TextView textView_isbn = (TextView) findViewById(R.id.isbndetail);
        TextView textView_resume= (TextView) findViewById(R.id.resumedetail);
        TextView textView_genre = (TextView) findViewById(R.id.genredetail);
        TextView textView_com = (TextView) findViewById(R.id.comdetail);
        TextView textView_annee = (TextView) findViewById(R.id.sortiedetail);
        TextView textView_editeur= (TextView) findViewById(R.id.editdetail);
        Book bookdetail=new Book();
        String res="";
        Bundle extras = getIntent().getExtras();

        String auteur ="";

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
        String nomfichier= (bookdetail.getTitle()).replaceAll(" ","_");
        Bitmap image=loadImageFromStorage(bookdetail.getSrcImage(),nomfichier);


        imag_detail_Book.setImageBitmap(image);
        textView_author.setText(textView_author.getText() + res);
        textView_title.setText(textView_title.getText() +bookdetail.getTitle());
        textView_isbn.setText(textView_isbn.getText() + isbn);
        textView_resume.setText(textView_resume.getText() + bookdetail.getResume());
        textView_genre.setText(textView_genre.getText() + bookdetail.getGenre());
        textView_annee.setText(textView_annee.getText() + bookdetail.getAnnee());
        textView_com.setText(textView_com.getText() + bookdetail.getCommentaires());
        textView_editeur.setText(textView_editeur.getText() + bookdetail.getEditeur());



        final Button suppr=(Button) findViewById(R.id.SupprBook);
        suppr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(detail_activity.this);
                builder.setMessage(R.string.Supression);
                builder.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //if yes we come back to the main activity.
                        final BDD bdd = new BDD(detail_activity.this);
                        //Création d'une instance de ma classe LivresBDD



                        //On ouvre la base de données pour écrire dedans
                        bdd.open();

                        BookBDD bookbdd=new BookBDD(bdd.getDh());
                        AuteurBDD auteurbdd = new AuteurBDD(bdd.getDh());

                        bookbdd.deleteBookByIsbn(isbn);
                        auteurbdd.deleteAllAuthorByIsbn(isbn);

                        Intent intent = new Intent(detail_activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("non", new DialogInterface.OnClickListener() {
                    @Override

                    // if no we just cancel the pop up
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        final Button modif=(Button) findViewById(R.id.ModifBook);
        modif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), Modifbook_activity.class);
                myIntent.putExtra("isbn",isbn);


                startActivityForResult(myIntent, 0);

                finish();
            }
        });

    }

    private Bitmap loadImageFromStorage(String path,String nomfichier)
    {
        Bitmap b=null;
        try {
            File f=new File(path, nomfichier+".png");
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(detail_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
