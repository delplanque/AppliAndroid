package com.example.jordan.booklibrairy.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jordan.booklibrairy.R;
import com.example.jordan.booklibrairy.book.Book;
import com.example.jordan.booklibrairy.bookSql.BookBDD;

public class create_book extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);



    }
    public void Createbook(View v){
        //Button button=(Button) findViewById(R.id.createbook);
        EditText editText_author = (EditText) findViewById(R.id.edit_message_author);
        final String author = editText_author.getText().toString();
        EditText editText_title = (EditText) findViewById(R.id.edit_message_title);
        final String title = editText_title.getText().toString();
        EditText editText_isbn = (EditText) findViewById(R.id.edit_message_isbn);
        final String isbn = editText_isbn.getText().toString();

        if(!author.isEmpty() && !title.isEmpty() && !isbn.isEmpty()){
            final BookBDD livreBdd = new BookBDD(this);
            //Création d'une instance de ma classe LivresBDD


            //Création d'un livre
            Book newbook=new Book(author,title,isbn);

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
        }





    public void Back(View v) {
        //pour retourner a l’activite principale il suffit seulement d’appler la methode finish() qui vas tuer cette activite

        finish() ;

    }


}
