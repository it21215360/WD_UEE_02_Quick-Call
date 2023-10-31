package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LaundryThank extends AppCompatActivity {

    ImageView navBack8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_thank);

        navBack8 = findViewById(R.id.navBack8);

        navBack8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryMain.class);
                startActivity(intent);
                finish();
            }
        });
    }
}