package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivityApprove extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_approve);

        textView = findViewById(R.id.textViewData1);
        String data1 = getIntent().getStringExtra("data1");
        textView.setText(data1);
    }
}