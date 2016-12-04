package com.example.jordan.booklibrairy;

/**
 * Created by jordan on 30/11/2016.
 */

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Enregistrer extends AppCompatActivity
{
    ImageView imageView;
    RequestQueue requestQueue;
    String JsonIsbn = "";
    String auteur="";
    String imageJson ="";

    // This string will hold the results
    String titre = "";
    String datepubli = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrer);
        Bundle extras = getIntent().getExtras();
        imageView = (ImageView) findViewById(R.id.ivCouverture);
        final TextView textView_titre = (TextView) findViewById(R.id.titre);
        final TextView textView_auteur = (TextView) findViewById(R.id.nom_auteur);
        final TextView textView_sorti = (TextView) findViewById(R.id.sortie);

        requestQueue = Volley.newRequestQueue(this);

        String isbn = extras.getString("isbn");
        JsonIsbn=isbn;

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
                            JSONArray imagelinks = new JSONArray(response.getString("imageLinks"));
                            //the response JSON Object
                            //and converts them into javascript objects
                            //JSONObject bookjson = new JSONObject(array.getString(0));
                            //JSONArray volumeinfo = new JSONArray(bookjson.getString("kind"));
                                JSONObject volumeInfo = array.getJSONObject(0).getJSONObject("volumeInfo");
                                String title = volumeInfo.getString("title");
                                String publishedDate = volumeInfo.getString("publishedDate");
                                titre = title;
                                datepubli = publishedDate;
                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                for (int j = 0; j < authors.length(); j++) {
                                    auteur = " "+  authors.getString(j);
                                }


                            textView_auteur.setText(textView_auteur.getText() + auteur);
                            textView_titre.setText(textView_titre.getText() + titre);
                            textView_titre.setText(textView_sorti.getText() + datepubli);
                            Picasso
                                    .with(getBaseContext())
                                    .load("http://books.google.com/books/content?id=-YPhjwEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api")
                                    .resize(500, 450)
                                    .into(imageView);


                        /*    for (int i = 0; i < volumeinfo.length(); i++) {
                                array.getString(i);
                                // On récupère un objet JSON du tableau
                                JSONObject infobook = new JSONObject(array.getString(i));

                                // Adds strings from object to the "data" string
                                data += "tile : " + infobook.getString("title") +
                                        "subtitle : " + infobook.getString("subtitle");


                            }*/


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







    }


}
