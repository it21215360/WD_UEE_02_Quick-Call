package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ElecList extends AppCompatActivity {
 Button buttonuser1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elec_list);

        buttonuser1 = (Button) findViewById(R.id.buttonuser1);
        buttonuser1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ElecList.this,ScheduleAppointMain.class);
                startActivity(intent);
            }
        });
    }
}