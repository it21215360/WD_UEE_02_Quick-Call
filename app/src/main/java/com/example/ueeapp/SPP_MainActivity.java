package com.example.ueeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class SPP_MainActivity extends AppCompatActivity {

    BottomNavigationView btmnavview;
    Prof_Home homefrag = new Prof_Home();
    Prof_Calendar calendarfrag = new Prof_Calendar();
    Prof_Profile profilefrag = new Prof_Profile();
    Rose_Chat chatfrag = new Rose_Chat();

    private Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spp_main);

        btmnavview = findViewById(R.id.navbar101);

        // Map your menu items to the corresponding fragments
        fragmentMap.put(R.id.Home, homefrag);
        fragmentMap.put(R.id.calendar, calendarfrag);
        fragmentMap.put(R.id.chat, chatfrag);
        fragmentMap.put(R.id.u_acc, profilefrag);

        getSupportFragmentManager().beginTransaction().replace(R.id.spflFragment, homefrag).commit();

        btmnavview.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the selected fragment from the map
                Fragment selectedFragment = fragmentMap.get(item.getItemId());

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.spflFragment, selectedFragment).commit();
                    return true;
                }

                return false;
            }
        });
    }
}