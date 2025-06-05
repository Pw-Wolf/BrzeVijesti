package com.sasaadamovic.brzevijesti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
// Ako planirate pokretati druge aktivnosti:
import android.content.Intent;

public class WelcomeActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Povezuje ovu aktivnost s XML layoutom

        // Pronalazimo gumbe iz layouta koristeći njihov ID
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // Postavljamo onClick listener za prvi gumb
        // ... unutar WelcomeActivity.java ...
        // Pretpostavimo da želite da button1 vodi na LoginActivity
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Postavljamo onClick listener za drugi gumb
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}