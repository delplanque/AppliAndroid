package com.example.jordan.booklibrairy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class detail_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_abook);

        ImageView imag_detail_Book = (ImageView) findViewById(R.id.imgdetail);
        TextView textView_author = (TextView) findViewById(R.id.autordetail);
        TextView textView_title = (TextView) findViewById(R.id.titledetail);
        TextView textView_isbn = (TextView) findViewById(R.id.isbndetail);
        TextView textView_resume= (TextView) findViewById(R.id.resumedetail);

        Bundle extras = getIntent().getExtras();

        String auteur ="";
        String isbn= "";
        String titre= "";
        String resume= "";

        if (extras != null) {

            //---------Récupère informations------------
            auteur =extras.getString("auteur");
            isbn =extras.getString("isbn");
            titre =extras.getString("titre");
            resume =extras.getString("resume");
        }
        imag_detail_Book.setImageResource(R.mipmap.ic_launcher);
        textView_author.setText(textView_author.getText() + auteur);
        textView_title.setText(textView_title.getText() + titre);
        textView_isbn.setText(textView_isbn.getText() + isbn);
        textView_resume.setText(textView_resume.getText() + resume);

    }

}
