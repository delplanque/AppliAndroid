package com.example.jordan.booklibrairy;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.example.jordan.booklibrairy.book.Auteur;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.Livre;
import com.example.jordan.booklibrairy.bookSql.BookBDD;
import com.example.jordan.booklibrairy.book.BookLibrary;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.*;
import com.android.volley.*;
import com.example.jordan.booklibrairy.bookSql.DatabaseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    String JsonIsbn = "9783161484100";

    String JsonURL = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+JsonIsbn;
    // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;

    String[] listItemsValue = new String[] {"img","author","title","isbn"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        requestQueue = Volley.newRequestQueue(this);

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
                            //the response JSON Object
                            //and converts them into javascript objects
                            //JSONObject bookjson = new JSONObject(array.getString(0));
                            //JSONArray volumeinfo = new JSONArray(bookjson.getString("kind"));
                           for(int i=0;i<array.length();i++){
                               JSONObject volumeInfo = array.getJSONObject(i).getJSONObject("volumeInfo");
                               String title = volumeInfo.getString("title");
                                data=title;
                               JSONArray authors = volumeInfo.getJSONArray("authors");
                               for(int j =0; j< authors.length(); j++){
                                   String author = authors.getString(i);
                               }
                           }

                            Context context = getApplicationContext();
                            CharSequence text = data;
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

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




        final ListView bookList =(ListView) findViewById(R.id.booklist);
        BookLibrary books= new BookLibrary();

        final BookBDD livreBdd = new BookBDD(this);
        //Création d'une instance de ma classe LivresBDD



        //On ouvre la base de données pour écrire dedans
        livreBdd.open();



        ArrayList<Book> allb=livreBdd.getAllBooks();
        for (Book b: allb) {
            books.saveBookInCollection(b);
        }


        livreBdd.close();

        List<Map<String, String>> listOfBook = new ArrayList<>();
        for(Book book :books.getlBooks()){
            Map<String,String> bookMap=new HashMap<>();
            bookMap.put("img",String.valueOf(R.mipmap.ic_launcher));
            bookMap.put("author",book.getAuthor());
            bookMap.put("title",book.getTitle());
            bookMap.put("isbn",book.getIsbn());
            listOfBook.add(bookMap);
        }





        SimpleAdapter listAdapter=new SimpleAdapter(this.getBaseContext(),listOfBook,R.layout.book_detail,
                listItemsValue,
                new int[] {R.id.img,R.id.author,R.id.title,R.id.isbn});
        bookList.setAdapter(listAdapter);


        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap obj = (HashMap) bookList.getItemAtPosition(position);
                Intent myIntent = new Intent(view.getContext(), detail_activity.class);
                String aut = (String) obj.get("author");
                String isbn = (String) obj.get("isbn");
                String titre = (String) obj.get("title");
                Book detailbook=new Book(aut,titre,isbn);
                myIntent.putExtra("auteur",aut);
                myIntent.putExtra("isbn",isbn);
                myIntent.putExtra("titre",titre);
                myIntent.putExtra("resume",detailbook.getResume());


                startActivityForResult(myIntent, 0);

            }
        });

    }

    public void Add(View v) {
        //on creer une nouvelle intent on definit la class de depart ici this et la class d'arrivé ici SecondActivite
        Intent intent=new Intent(this,create_book.class);
        //on lance l'intent, cela a pour effet de stoper l'activité courante et lancer une autre activite ici SecondActivite
        startActivity(intent);


    }

    public void jsonRequestBook(){
        requestQueue = Volley.newRequestQueue(this);

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
                            //the response JSON Object
                            //and converts them into javascript objects
                            //JSONObject bookjson = new JSONObject(array.getString(0));
                            //JSONArray volumeinfo = new JSONArray(bookjson.getString("kind"));
                            for(int i=0;i<array.length();i++){
                                JSONObject volumeInfo = array.getJSONObject(i).getJSONObject("volumeInfo");
                                String title = volumeInfo.getString("title");
                                data=title;
                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                for(int j =0; j< authors.length(); j++){
                                    String author = authors.getString(i);
                                }
                            }

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



    public void detail(View v) {
        Context context = getApplicationContext();
        CharSequence text = "Succés!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_scan :
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }


@Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        String isbn = "";
        String type = "";
        String prefix ="";
        if (scanningResult != null) {
            isbn = scanningResult.getContents().toLowerCase();
            type = scanningResult.getFormatName().toLowerCase();
            prefix = isbn.substring(0, 3);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Aucunes données scannées", Toast.LENGTH_SHORT);
            toast.show();
        }

        if(!type.equals("ean_13"))
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Mauvais format", Toast.LENGTH_SHORT);
            toast.show();
        }

        if(!(prefix.equals("977") || prefix.equals("978") || prefix.equals("979")))
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "C'est n'est pas un livre !", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Code Ok", Toast.LENGTH_SHORT);
            toast.show();

           // GestionLivre gl = new GestionLivre(this);
           //  trouvaille = ;
            Intent intention;
            Toast toast1;
          //  if(trouvaille != null)
          //  {
          //      intention = new Intent(this, AfficherLivre.class);
          //      intention.putExtra("livre", trouvaille);
          //      toast1 = Toast.makeText(this, "Ce livre est déjà dans votre bibliothèque.", Toast.LENGTH_LONG);
          //      toast1.show();
          //  }
          //  else {
                intention = new Intent(this, Enregistrer.class);
                intention.putExtra("isbn", isbn);
                toast1 = Toast.makeText(this, "Ce livre n'est pas dans votre bibliothèque, enregistrez le !", Toast.LENGTH_LONG);
                toast1.show();

          //  }

            startActivity(intention);
        }
    }
}