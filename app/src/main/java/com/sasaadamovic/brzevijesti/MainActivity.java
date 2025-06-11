package com.sasaadamovic.brzevijesti; // Prilagodite vašem imenu paketa

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Postavlja layout definiran u activity_main.xml
        setContentView(R.layout.activity_main);

        // Ovdje možete dodati dodatnu logiku ako je potrebno,
        // npr. dohvaćanje TextView-a i mijenjanje teksta programski:
        // TextView textView = findViewById(R.id.textViewHello);
        // textView.setText("Novi tekst!");
    }
}