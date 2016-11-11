package com.example.jordan.booklibrairy;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/*import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.*;
import com.android.volley.*;*/

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String[] listItemsValue = new String[] {"img","author","title","isbn"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String resultatRequest = " ";



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

        List<Map<String, String>> listOfBook = new ArrayList<Map<String, String>>();
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


    public void Refresh(View v) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void detail(View v) {
        Context context = getApplicationContext();
        CharSequence text = "Succés!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
}