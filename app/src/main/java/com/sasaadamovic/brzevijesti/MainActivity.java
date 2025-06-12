package com.sasaadamovic.brzevijesti; // Vaš naziv paketa

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Importi za vaše fragmente
import com.sasaadamovic.brzevijesti.HomeFragment;
import com.sasaadamovic.brzevijesti.StocksFragment; // Pretpostavljam da imate ovu klasu
import com.sasaadamovic.brzevijesti.FavoritesFragment; // Pretpostavljam da imate ovu klasu
import com.sasaadamovic.brzevijesti.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Učitavanje početnog fragmenta (npr. HomeFragment) pri prvom pokretanju
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.navigation_home) {
                        selectedFragment = new HomeFragment();
                    }
                    else if (itemId == R.id.navigation_search) {
                        selectedFragment = new StocksFragment(); // Pretpostavljam da imate ovu klasu
                    } else if (itemId == R.id.navigation_favorites) {
                        selectedFragment = new FavoritesFragment(); // Pretpostavljam da imate ovu klasu
                    } else if (itemId == R.id.navigation_profile) {
                        selectedFragment = new ProfileFragment(); // Pretpostavljam da imate ovu klasu
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }
                    return true;
                }
            };
}