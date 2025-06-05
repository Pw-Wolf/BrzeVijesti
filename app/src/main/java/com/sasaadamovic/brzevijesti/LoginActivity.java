package com.sasaadamovic.brzevijesti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ovdje pokrenite aktivnost za registraciju
                // Toast.makeText(LoginActivity.this, "Otvaram stranicu za registraciju...", Toast.LENGTH_SHORT).show(); // Možete obrisati ili ostaviti ovaj Toast
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                // Ovdje možete dodati i finish(); ako želite zatvoriti LoginActivity kada se otvori RegisterActivity,
                // ali obično se ostavlja otvorenom kako bi se korisnik mogao vratiti.
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email je obavezan");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Lozinka je obavezna");
            editTextPassword.requestFocus();
            return;
        }

        // --- OVDJE IDE VAŠA LOGIKA ZA PROVJERU KORISNIČKOG IMENA I LOZINKE ---
        // Ovo je samo primjer. U stvarnoj aplikaciji biste provjeravali
        // korisničke podatke s bazom podataka, serverom, SharedPreferences, itd.

        if (email.equals("test@example.com") && password.equals("password123")) {
            Toast.makeText(this, "Prijava uspješna!", Toast.LENGTH_SHORT).show();
            // Nakon uspješne prijave, možete preusmjeriti korisnika na glavnu aktivnost
            Intent intent = new Intent(LoginActivity.this, MainActivity.class); // Ili neka druga aktivnost
            startActivity(intent);
            finish(); // Zatvara LoginActivity kako se korisnik ne bi mogao vratiti pritiskom na "back"
        } else {
            Toast.makeText(this, "Neispravan email ili lozinka.", Toast.LENGTH_LONG).show();
        }
        // --- KRAJ LOGIKE ZA PROVJERU ---
    }
}