package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewAppointMain extends AppCompatActivity {

    Button buttonhome2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appoint_main);
/*
        buttonhome2 = (Button) findViewById(R.id.buttonhome2);
        buttonhome2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAppointMain.this,Home.class);
                startActivity(intent);
            }
        });*/
    }
}