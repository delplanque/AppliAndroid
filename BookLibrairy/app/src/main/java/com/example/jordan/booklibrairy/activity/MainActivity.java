package com.example.jordan.booklibrairy.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;
import com.example.jordan.booklibrairy.R;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.book.ListAuteurs;
import com.example.jordan.booklibrairy.bookSql.AuteurBDD;
import com.example.jordan.booklibrairy.bookSql.BDD;
import com.example.jordan.booklibrairy.bookSql.BookBDD;
import com.example.jordan.booklibrairy.book.BookLibrary;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;
import com.example.jordan.booklibrairy.utils.MyViewBinder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;
    BookLibrary books;
    ListView bookList;
    EditText search;

    String[] listItemsValue = new String[] {"img","author","title","isbn"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        bookList =(ListView) findViewById(R.id.booklist);
        books= new BookLibrary();

        final BDD bdd = new BDD(this);
        //Création d'une instance de ma classe LivresBDD



        //On ouvre la base de données pour écrire dedans
        bdd.open();

        BookBDD bookbdd=new BookBDD(bdd.getDh());

        ArrayList<Book> allb=(ArrayList) bookbdd.getAllBook();


        for (Book b: allb) {
            books.saveBookInCollection(b);
        }



        bdd.close();

        List<Map<String, Object>> listOfBook = new ArrayList<>();
        for(Book book :books.getlBooks()){
            String nomfichier= (book.getTitle()).replaceAll(" ","_");
            AuteurBDD auteurbdd=new AuteurBDD(bdd.getDh());
            ListAuteurs listeAuteurs = auteurbdd.getAllAuthorByIsbn(book.getIsbn());
            String res= listeAuteurs.toString();
            Bitmap image=loadImageFromStorage(book.getSrcImage(),nomfichier);

            Map<String,Object> bookMap=new HashMap<>();
            bookMap.put("img",image);
            bookMap.put("author",res);
            bookMap.put("title",book.getTitle());
            bookMap.put("isbn",book.getIsbn());
            listOfBook.add(bookMap);
        }



        search = (EditText) findViewById(R.id.search);

        SimpleAdapter listAdapter=new SimpleAdapter(this.getBaseContext(),listOfBook,R.layout.book_detail,
                listItemsValue,
                new int[] {R.id.img,R.id.author,R.id.title,R.id.isbn});
        bookList.setAdapter(listAdapter);
        listAdapter.setViewBinder(new MyViewBinder());
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                filtrer();
            }
        });


        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap obj = (HashMap) bookList.getItemAtPosition(position);
                Intent myIntent = new Intent(view.getContext(), detail_activity.class);
                String isbn = (String) obj.get("isbn");
                myIntent.putExtra("isbn",isbn);


                startActivityForResult(myIntent, 0);

                finish();

            }
        });

    }

    public void Add(View v) {
        //on creer une nouvelle intent on definit la class de depart ici this et la class d'arrivé ici SecondActivite
        Intent intent=new Intent(this,create_book.class);
        //on lance l'intent, cela a pour effet de stoper l'activité courante et lancer une autre activite ici SecondActivite
        startActivity(intent);
        finish();

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

        if (resultCode == Activity.RESULT_OK) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String isbn = "";
            String type = "";
            String prefix ="";
            if (scanningResult != null) {

                isbn = scanningResult.getContents().toLowerCase();
                type = scanningResult.getFormatName().toLowerCase();
                prefix = isbn.substring(0, 3);
            } else {
                Log.e("SEARCH_EAN", "IntentResult  NULL!");
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
                final BDD bdd = new BDD(this);
                //On ouvre la base de données pour écrire dedans
                bdd.open();
                boolean present=false;
                BookBDD bookbdd=new BookBDD(bdd.getDh());

                //On recuperer tout les livres de la base de données
                ArrayList<Book> allb=(ArrayList) bookbdd.getAllBook();

                /*On verifie pour tout les livres de la liste si l'isbn scanner correspond
                * a un des isbn de la liste et
                * on passe le booleen present a true si on en trouve un
                */
                for (Book b: allb) {

                    if(b.getIsbn().compareToIgnoreCase(isbn)==0){
                        present=true;

                    }
                }



                bdd.close();
                /*
                *On verifie que le llivre n'est pas déjà présent dans la bibliotheque
                * Si il n'est pas présent on continue alors sur la view suivante
                * qui va permettre d'ajouter le livre
                * Sinon on retourne sur l'acceuil
                */
                if(present == true){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ce livre est déjà présent dans votre bibliotheque", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Code Ok", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intention;
                    Toast toast1;

                    intention = new Intent(this, Enregistrer.class);
                    intention.putExtra("isbn", isbn);
                    toast1 = Toast.makeText(this, "Ce livre n'est pas dans votre bibliothèque, enregistrez le !", Toast.LENGTH_LONG);
                    toast1.show();


                    startActivity(intention);

                    finish();
                }


            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("SEARCH_EAN", "CANCEL");
        }

    }

    public void filtrer() {
        // retourner la chaine saisie par l'utilisateur
        String name = search.getText().toString();
        // créer une nouvelle liste qui va contenir la résultat à afficher

        final BDD bdd = new BDD(this);
        //Création d'une instance de ma classe LivresBDD



        //On ouvre la base de données pour écrire dedans
        bdd.open();

        List<Map<String, Object>> listOfBook = new ArrayList<>();
        for(Book book :books.getlBooks()){
            if (book.getTitle().toLowerCase().startsWith(name)) {
                String nomfichier= (book.getTitle()).replaceAll(" ","_");
                AuteurBDD auteurbdd=new AuteurBDD(bdd.getDh());
                ListAuteurs listeAuteurs = auteurbdd.getAllAuthorByIsbn(book.getIsbn());
                String res= listeAuteurs.toString();
                Bitmap image=loadImageFromStorage(book.getSrcImage(),nomfichier);
                Map<String, Object> bookMap = new HashMap<>();
                bookMap.put("img", image);
                bookMap.put("author", res);
                bookMap.put("title", book.getTitle());
                bookMap.put("isbn", book.getIsbn());
                listOfBook.add(bookMap);
            }
        }
        bdd.close();
        //vider la liste
        bookList.setAdapter(null);
        // ajouter la nouvelle liste
        SimpleAdapter listAdapter=new SimpleAdapter(this.getBaseContext(),listOfBook,R.layout.book_detail,
                listItemsValue,
                new int[] {R.id.img,R.id.author,R.id.title,R.id.isbn});
        listAdapter.setViewBinder(new MyViewBinder());
        bookList.setAdapter(listAdapter);
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

}