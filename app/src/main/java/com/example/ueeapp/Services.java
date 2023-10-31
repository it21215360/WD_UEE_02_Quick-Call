package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Services extends AppCompatActivity {

    TextView textStain;
    TextView textWash;
    TextView textIron;
    TextView textDry;
    ImageView navBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        textStain = findViewById(R.id.txtStain);
        textWash = findViewById(R.id.txtWash);
        textIron = findViewById(R.id.txtIron);
        textDry = findViewById(R.id.txtDry);
        navBack = findViewById(R.id.navBack);

        textStain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClothesList.class);
                startActivity(intent);
                finish();
            }
        });

        textWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClothesList.class);
                startActivity(intent);
                finish();
            }
        });

        textIron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClothesList.class);
                startActivity(intent);
                finish();
            }
        });

        textDry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClothesList.class);
                startActivity(intent);
                finish();
            }
        });

        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryMain.class);
                startActivity(intent);
                finish();
            }
        });
    }
}