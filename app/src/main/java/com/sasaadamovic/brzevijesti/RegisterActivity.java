package com.sasaadamovic.brzevijesti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vraća korisnika na LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Zatvara RegisterActivity
            }
        });
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validacija unosa
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Korisničko ime je obavezno");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email je obavezan");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Unesite ispravan email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Lozinka je obavezna");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Lozinka mora imati najmanje 6 znakova");
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Potvrda lozinke je obavezna");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Lozinke se ne podudaraju");
            editTextConfirmPassword.requestFocus();
            // Očisti polja za lozinku radi sigurnosti
            editTextPassword.setText("");
            editTextConfirmPassword.setText("");
            return;
        }

        // --- OVDJE IDE VAŠA LOGIKA ZA SPREMANJE NOVOG KORISNIKA ---
        // Ovo je mjesto gdje biste spremili korisničke podatke u bazu podataka,
        // poslali ih na server, itd.
        // Za ovaj primjer, samo ćemo prikazati Toast poruku.

        Toast.makeText(this, "Registracija uspješna za: " + username, Toast.LENGTH_LONG).show();

        // Nakon uspješne registracije, možete preusmjeriti korisnika na LoginActivity
        // ili direktno na MainActivity ako želite automatsku prijavu.
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Zatvara RegisterActivity

        // --- KRAJ LOGIKE ZA SPREMANJE ---
    }
}