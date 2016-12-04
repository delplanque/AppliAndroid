package com.example.jordan.booklibrairy;

/**
 * Created by jordan on 30/11/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.jordan.booklibrairy.book.Book;
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
    String titre = "";
    String datepubli = "";
    String resumeLivre = "";

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

                            JSONArray array = new JSONArray(response.getString("items"));
                            String description="";
                            //the response JSON Object
                            //and converts them into javascript objects
                            //JSONObject bookjson = new JSONObject(array.getString(0));
                            //JSONArray volumeinfo = new JSONArray(bookjson.getString("kind"));

                                JSONObject volumeInfo = array.getJSONObject(0).getJSONObject("volumeInfo");
                                if (!volumeInfo.isNull("description")){
                                    description = volumeInfo.getString("description");
                            }
                                String title = volumeInfo.getString("title");
                                String publishedDate = volumeInfo.getString("publishedDate");

                                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                            String srcImg = imageLinks.getString("thumbnail");
                                titre = title;
                                datepubli = publishedDate;
                                resumeLivre = description;
                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                for (int j = 0; j < authors.length(); j++) {
                                    auteur = " "+  authors.getString(j);
                                }






                            textView_auteur.setText(textView_auteur.getText() + auteur);
                            textView_titre.setText(textView_titre.getText() + titre);
                            textView_sorti.setText(textView_sorti.getText() + datepubli);
                            textView_resum.setText(textView_sorti.getText() + resumeLivre);

                            Picasso
                                    .with(getBaseContext())
                                    .load(srcImg)
                                    .resize(500, 450)
                                    .into(imageView);




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

        final ImageView image =(ImageView)findViewById(R.id.ivCouverture);
        final Button add=(Button) findViewById(R.id.bajouter);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                image.setDrawingCacheEnabled(true);
                Bitmap bmp =Bitmap.createBitmap(image.getDrawingCache());
                image.setDrawingCacheEnabled(false);
                String nomfichier= titre.replace(' ','_');
                File file = new File(path,nomfichier +".png");
                try {
                    fOut = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try{
                        out.close();
                    } catch(Throwable ignore) {}
                }

                EditText editText_author = (EditText) findViewById(R.id.nom_auteur);
                final String author = editText_author.getText().toString();

                EditText editText_resume = (EditText) findViewById(R.id.resume);
                final String resume = editText_resume.getText().toString();

                EditText editText_title = (EditText) findViewById(R.id.titre);
                final String title = editText_title.getText().toString();

                EditText editText_isbn = (EditText) findViewById(R.id.idisbn);
                final String isbn = editText_isbn.getText().toString();

                if(!author.isEmpty() && !title.isEmpty() && !isbn.isEmpty()){
                    final BookBDD livreBdd = new BookBDD(getBaseContext());
                    //Création d'une instance de ma classe LivresBDD


                    //Création d'un livre
                    Book newbook=new Book(author,title,isbn,file.getAbsolutePath(),resume);

                    //On ouvre la base de données pour écrire dedans
                    livreBdd.open();
                    //On insère le livre que l'on vient de créer
                    livreBdd.insertLivre(newbook);

                    //Pour vérifier que l'on a bien créé notre livre dans la BDD
                    //on extrait le livre de la BDD grâce au titre du livre que l'on a créé précédemment1
                    Book livreFromBdd = livreBdd.getLivreWithTitre(newbook.getTitle());
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

                    livreBdd.close();
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Un des champs n'a pas été rempli!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            }});





    }


}
