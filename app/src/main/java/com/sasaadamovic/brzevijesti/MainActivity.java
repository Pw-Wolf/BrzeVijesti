package com.sasaadamovic.brzevijesti;

import android.content.res.Configuration; // Dodajte ovaj import
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View; // Dodajte ovaj import

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView; // Dodajte ovaj import

// Importi za vaše fragmente
import com.sasaadamovic.brzevijesti.HomeFragment;
import com.sasaadamovic.brzevijesti.StocksFragment;
import com.sasaadamovic.brzevijesti.FavoritesFragment;
import com.sasaadamovic.brzevijesti.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    // Ne treba nam više globalna bottomNavigationView, jer će ovisiti o orijentaciji
    // private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Provjeravamo koja je navigacijska komponenta prisutna u trenutnom layoutu
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        NavigationView sideNav = findViewById(R.id.nav_view_landscape);

        if (bottomNav != null) {
            // Ako je prisutna BottomNavigationView (portretni mod)
            bottomNav.setOnItemSelectedListener(bottomNavListener);
        } else if (sideNav != null) {
            // Ako je prisutna NavigationView (landscape mod)
            sideNav.setNavigationItemSelectedListener(sideNavListener);
        }


        if (savedInstanceState == null) {
            // Učitaj početni fragment (HomeFragment) samo ako se aktivnost ne obnavlja
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    // Listener za BottomNavigationView (za portretni mod)
    private BottomNavigationView.OnItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.navigation_home) {
                        selectedFragment = new HomeFragment();
                    } else if (itemId == R.id.navigation_search) {
                        selectedFragment = new StocksFragment();
                    } else if (itemId == R.id.navigation_favorites) {
                        selectedFragment = new FavoritesFragment();
                    } else if (itemId == R.id.navigation_profile) {
                        selectedFragment = new ProfileFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }
                    return true;
                }
            };

    // Listener za NavigationView (za landscape mod)
    private NavigationView.OnNavigationItemSelectedListener sideNavListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.navigation_home) {
                        selectedFragment = new HomeFragment();
                    } else if (itemId == R.id.navigation_search) {
                        selectedFragment = new StocksFragment();
                    } else if (itemId == R.id.navigation_favorites) {
                        selectedFragment = new FavoritesFragment();
                    } else if (itemId == R.id.navigation_profile) {
                        selectedFragment = new ProfileFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }
                    // Ako koristite DrawerLayout, ovdje biste zatvorili ladicu
                    return true;
                }
            };
}