package com.example.jordan.booklibrairy.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jordan.booklibrairy.R;
import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.ListAuteurs;
import com.example.jordan.booklibrairy.bookSql.AuteurBDD;
import com.example.jordan.booklibrairy.bookSql.BDD;
import com.example.jordan.booklibrairy.bookSql.BookBDD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class create_book extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);



    }
    public void Createbook(View v){
        Button button=(Button) findViewById(R.id.createbook);
        EditText editText_author = (EditText) findViewById(R.id.edit_message_author);
        final String author = editText_author.getText().toString();
        EditText editText_title = (EditText) findViewById(R.id.edit_message_title);
        final String title = editText_title.getText().toString();
        EditText editText_isbn = (EditText) findViewById(R.id.edit_message_isbn);
        final String isbn = editText_isbn.getText().toString();
        EditText editText_sortie = (EditText) findViewById(R.id.edit_message_sortie);
        final String sortie = editText_sortie.getText().toString();

        if(!author.isEmpty() && !title.isEmpty() && !isbn.isEmpty() && !sortie.isEmpty()){


            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.book);
/*
                Bitmap bmp =Bitmap.createBitmap(image.getDrawingCache());
                image.setDrawingCacheEnabled(false);*/


            String dirImage= saveToInternalStorage(b,"default");

            //Création d'un livre
            Book newbook=new Book(title,isbn,dirImage," "," ",sortie," "," ");
            final BDD bdd=new BDD(getBaseContext());
            bdd.open();
            //On insère le livre que l'on vient de créer
            BookBDD bookbdd=new BookBDD(bdd.getDh());
            bookbdd.createBook(newbook);
            ListAuteurs lauteurs  = new ListAuteurs();

            String[] Allauteurs = author.split(",");

            for(int i=0;i<Allauteurs.length;i++){
                Auteur nAuteur = new Auteur(Allauteurs[i],isbn);
                lauteurs.add(nAuteur);
            }
            //On insère égalment le ou les auteurs du livre
            AuteurBDD auteurbdd=new AuteurBDD(bdd.getDh());
            auteurbdd.createAllAuthor(lauteurs);

            bdd.close();

            Intent intent = new Intent(create_book.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "Un des champs n'a pas été rempli!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        }


    private String saveToInternalStorage(Bitmap bitmapImage, String nomfichier){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,nomfichier+".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(create_book.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
