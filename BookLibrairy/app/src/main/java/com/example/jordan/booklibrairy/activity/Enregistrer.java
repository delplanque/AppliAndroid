package com.example.jordan.booklibrairy.activity;

/**
 * Created by jordan on 30/11/2016.
 */

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Enregistrer extends AppCompatActivity
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
        setContentView(R.layout.activity_enregistrer);
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

        final ListAuteurs auteurs = new ListAuteurs();

        requestQueue = Volley.newRequestQueue(this);

        String isbn = extras.getString("isbn");
        JsonIsbn=isbn;
        textView_isbn.setText( textView_isbn.getText() + JsonIsbn);
        String JsonURL = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+JsonIsbn;
        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int nbitems = response.getInt("totalItems");
                            if(nbitems!=0){
                                JSONArray array = new JSONArray(response.getString("items"));
                                String description="";
                                String categorie="";
                                //the response JSON Object
                                //and converts them into javascript objects
                                //JSONObject bookjson = new JSONObject(array.getString(0));
                                //JSONArray volumeinfo = new JSONArray(bookjson.getString("kind"));

                                JSONObject volumeInfo = array.getJSONObject(0).getJSONObject("volumeInfo");
                                if (!volumeInfo.isNull("description")){
                                    description = volumeInfo.getString("description");
                                }
                                if (!volumeInfo.isNull("categories")){

                                    JSONArray allcate = volumeInfo.getJSONArray("categories");
                                    for (int j = 0; j < allcate.length(); j++) {
                                        if(j!=0)
                                            categorie = ","+  allcate.getString(j);

                                        categorie =  allcate.getString(j);
                                    }
                                }
                                else{
                                    categorie="inconnue";
                                }
                                String title = volumeInfo.getString("title");
                                String publishedDate = volumeInfo.getString("publishedDate");
                                publishedDate=publishedDate.split("T")[0];
                                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                                String srcImg = imageLinks.getString("thumbnail");
                                titre = title;
                                datepubli = publishedDate;
                                resumeLivre = description;
                                genre=categorie;
                                editeur=volumeInfo.getString("publisher");
                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                for (int j = 0; j < authors.length(); j++) {
                                    if(j!=0)
                                        auteur = ","+  authors.getString(j);

                                    auteur =  authors.getString(j);
                                }


                                textView_auteur.setText(textView_auteur.getText() + auteur);
                                textView_titre.setText(textView_titre.getText() + titre);
                                textView_sorti.setText(textView_sorti.getText() + datepubli);
                                textView_resum.setText(textView_resum.getText() + resumeLivre);
                                textView_editeur.setText(textView_resum.getText() + editeur);
                                textView_genre.setText(textView_genre.getText() + genre);

                                Picasso
                                        .with(getBaseContext())
                                        .load(srcImg)
                                        .resize(500, 450)
                                        .into(imageView);

                            }

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        requestQueue.add(obreq);

        final Button add=(Button) findViewById(R.id.bajouter);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ImageView im =(ImageView)findViewById(R.id.ivCouverture);

                im.buildDrawingCache();
                im.setDrawingCacheEnabled(true);

// this is the important code :)
// Without it the view will have a dimension of 0,0 and the bitmap will be null
               /* im.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                im.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());*/

                im.buildDrawingCache(true);
                BitmapDrawable drawable = (BitmapDrawable) im.getDrawable();
                Bitmap b = drawable.getBitmap();
                im.setDrawingCacheEnabled(false); // clear drawing cache
/*
                Bitmap bmp =Bitmap.createBitmap(image.getDrawingCache());
                image.setDrawingCacheEnabled(false);*/

                String nomfichier= titre.replaceAll(" ","_");
               String dirImage= saveToInternalStorage(b,nomfichier);
               /* File file = new File(path,nomfichier +".png");
                try {
                    fOut = new FileOutputStream(file);
                    bmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try{
                        out.close();
                    } catch(Throwable ignore) {}
                }
*/
                EditText editText_author = (EditText) findViewById(R.id.nom_auteur);
                final String author = editText_author.getText().toString();

                EditText editText_resume = (EditText) findViewById(R.id.resume);
                final String resume = editText_resume.getText().toString();

                EditText editText_title = (EditText) findViewById(R.id.titre);
                final String title = editText_title.getText().toString();

                EditText editText_isbn = (EditText) findViewById(R.id.idisbn);
                final String isbn = editText_isbn.getText().toString();

                EditText editText_annee = (EditText) findViewById(R.id.sortie);
                final String annee = editText_annee.getText().toString();

                EditText editText_editeur = (EditText) findViewById(R.id.editeur);
                final String edit = editText_editeur.getText().toString();

                EditText editText_genre = (EditText) findViewById(R.id.genres);
                final String genre = editText_genre.getText().toString();

                EditText editText_com = (EditText) findViewById(R.id.commentaires);
                final String com = editText_com.getText().toString();

                if(!author.isEmpty() && !title.isEmpty() && !isbn.isEmpty()){
                    final BDD bdd=new BDD(getBaseContext());

                   String tabauteur[]=author.split(",");

                    for(int i=0;i<tabauteur.length;i++){
                        Auteur auteur= new Auteur(tabauteur[i],isbn);
                        auteurs.add(auteur);
                    }

                    //Création d'un livre avec son ou ses auteurs
                    Book newbook=new Book(title,isbn,dirImage,resume,edit,annee,com,genre);

                    //On ouvre la base de données pour écrire dedans
                    bdd.open();
                    //On insère le livre que l'on vient de créer
                    BookBDD bookbdd=new BookBDD(bdd.getDh());
                    bookbdd.createBook(newbook);

                    //On insère égalment le ou les auteurs du livre
                    AuteurBDD auteurbdd=new AuteurBDD(bdd.getDh());
                    auteurbdd.createAllAuthor(auteurs);

                    //Pour vérifier que l'on a bien créé notre livre dans la BDD
                    //on extrait le livre de la BDD grâce au titre du livre que l'on a créé précédemment1
                    Book livreFromBdd = bookbdd.getBookByIsbn(newbook.getIsbn());
                    //Si un livre est retourné (donc si le livre à bien été ajouté à la BDD)
                    if(livreFromBdd != null){
                        //On affiche les infos du livre dans un Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Succés!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }

                    //on supprime le livre de la BDD grâce à son ID
                    //livreBdd.removeLivreWithID(livreFromBdd.getId());

                    bdd.close();

                    Intent intent = new Intent(Enregistrer.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "ERREUR!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }});





    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Enregistrer.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String saveToInternalStorage(Bitmap bitmapImage,String nomfichier){
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




}
