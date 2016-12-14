package com.example.jordan.booklibrairy.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jordan.booklibrairy.R;
import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.ListAuteurs;
import com.example.jordan.booklibrairy.bookSql.AuteurBDD;
import com.example.jordan.booklibrairy.bookSql.BDD;
import com.example.jordan.booklibrairy.bookSql.BookBDD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jordan on 14/12/2016.
 */

public class Modifbook_activity  extends AppCompatActivity
{
    String path = Environment.getExternalStorageDirectory().toString();
    OutputStream fOut = null;
    FileOutputStream out;
    ImageView imageView;
    RequestQueue requestQueue;
    String JsonIsbn = "";
    String auteur="";




    // This string will hold the results
    String titre = " ";
    String datepubli = " ";
    String resumeLivre = " ";
    String editeur=" ";
    String genre= " ";
    String commentaires=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_book);
        Bundle extras = getIntent().getExtras();
        imageView = (ImageView) findViewById(R.id.ivCouverture);
        final TextView textView_titre = (TextView) findViewById(R.id.titre);
        final TextView textView_auteur = (TextView) findViewById(R.id.nom_auteur);
        final TextView textView_sorti = (TextView) findViewById(R.id.sortie);
        final TextView textView_resum = (TextView) findViewById(R.id.resume);
        final TextView textView_isbn = (TextView) findViewById(R.id.idisbn);
        final TextView textView_editeur = (TextView) findViewById(R.id.editeur);
        final TextView textView_genre = (TextView) findViewById(R.id.genres);
        final TextView textView_commentaire = (TextView) findViewById(R.id.commentaires);



        String isbn = extras.getString("isbn");
        final BDD bdd = new BDD(Modifbook_activity.this);
        bdd.open();

        final BookBDD bookbdd=new BookBDD(bdd.getDh());
        final AuteurBDD auteurbdd = new AuteurBDD(bdd.getDh());
        int i=0;
        final Book book= bookbdd.getBookByIsbn(isbn);
        final ListAuteurs auteurs  = auteurbdd.getAllAuthorByIsbn(isbn);
        String resAuteurs="";
        for(Auteur auteur : auteurs){
            if(i==0){
                resAuteurs=auteur.getNomAuteur();
                i++;
            }
            else {
                resAuteurs = "," + auteur.getNomAuteur();
            }
        }
        String nomfichier= (book.getTitle()).replaceAll(" ","_");
        Bitmap image=loadImageFromStorage(book.getSrcImage(),nomfichier);


        imageView.setImageBitmap(image);

        textView_auteur.setText(resAuteurs);
        textView_titre.setText(book.getTitle());
        textView_sorti.setText(book.getAnnee());
        textView_resum.setText(book.getResume());
        textView_isbn.setText(book.getIsbn());
        textView_editeur.setText(book.getEditeur());
        textView_genre.setText(book.getGenre());
        textView_commentaire.setText(book.getCommentaires());

        final Button modif=(Button) findViewById(R.id.EnregistreBook);
        modif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String titre=textView_titre.getText().toString();
                String sorti=textView_sorti.getText().toString();
                String auteur=textView_auteur.getText().toString();
                String resum=textView_resum.getText().toString();
                String isbn=textView_isbn.getText().toString();
                String editeur=textView_editeur.getText().toString();
                String genre=textView_genre.getText().toString();
                String commentaires=textView_commentaire.getText().toString();

                ListAuteurs lauteurs  = new ListAuteurs();

                String[] Allauteurs = auteur.split(",");

                for(int i=0;i<Allauteurs.length;i++){
                    Auteur nAuteur = new Auteur(Allauteurs[i],isbn);
                    lauteurs.add(nAuteur);
                }

                bookbdd.deleteBookByIsbn(isbn);
                auteurbdd.deleteAllAuthorByIsbn(isbn);


                bookbdd.createBook(new Book(titre,isbn,book.getSrcImage(),resum,editeur,sorti,commentaires,genre));
                auteurbdd.createAllAuthor(lauteurs);

                Intent intent = new Intent(Modifbook_activity.this, MainActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(Modifbook_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
