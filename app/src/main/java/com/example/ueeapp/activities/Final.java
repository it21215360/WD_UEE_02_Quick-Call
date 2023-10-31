package com.example.ueeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ueeapp.R;

public class Final extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        ImageView backBtn = findViewById(R.id.navMainPg);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the intent to navigate back to the main activity
                Intent intent = new Intent(Final.this, AllServices.class);
                startActivity(intent); // Start the activity
            }
        });
    }
}
