package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LaundryFeedback extends AppCompatActivity {

    EditText nameEdit;
    EditText emailEdit;
    EditText suggestionEdit;
    Spinner ratingSpinner;
    Spinner taskSpinner;
    Button btnSend;
    ImageView navBack7;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_feedback);

        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        suggestionEdit = findViewById(R.id.suggestionEdit);
        ratingSpinner = findViewById(R.id.ratingSpinner);
        taskSpinner = findViewById(R.id.taskSpinner);
        navBack7 = findViewById(R.id.navBack7);
        btnSend = findViewById(R.id.btnSend);

        reference = FirebaseDatabase.getInstance().getReference().child("Feedback");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertFeedback();
                Intent intent = new Intent(getApplicationContext(), LaundryThank.class);
                startActivity(intent);
                finish();
            }
        });

        navBack7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryStatus.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void insertFeedback(){
        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String rate = ratingSpinner.getSelectedItem().toString();
        String task = taskSpinner.getSelectedItem().toString();
        String suggestion = suggestionEdit.getText().toString();

        Feedback feedback = new Feedback(name,email,rate,task,suggestion);

        reference.push().setValue(feedback);
        Toast.makeText(LaundryFeedback.this,"Your Feedback sent!",Toast.LENGTH_SHORT).show();
    }
}