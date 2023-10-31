package com.example.ueeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ueeapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerFeedback extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button sendResponseButton;
    private String selectedFeedback;
    private String userEmail; // Variable to store the user's email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);

        // Initialize your UI elements
        radioGroup = findViewById(R.id.radioGroup);
        sendResponseButton = findViewById(R.id.sendResponseButton);

        // Retrieve the user's email from the intent
        userEmail = getIntent().getStringExtra("userEmail");

        // Set up a listener for the "SEND RESPONSE" button
        sendResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected feedback option
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                selectedFeedback = selectedRadioButton.getText().toString();

                // Check if an option is selected
                if (selectedFeedback.isEmpty()) {
                    Toast.makeText(CustomerFeedback.this, "Please select a feedback option", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the feedback and email to Firestore
                saveFeedbackToDatabase(userEmail, selectedFeedback);
            }
        });
    }

    private void saveFeedbackToDatabase(String userEmail, String feedback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference feedbackRef = db.collection("feedback"); // Use your desired collection name

        // Create a map to store the feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("email", userEmail);
        feedbackData.put("feedback", feedback);

        feedbackRef.add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Feedback sent", Toast.LENGTH_SHORT).show();
                    navigateToFinalPage();
                })
                .addOnFailureListener(e -> {
                    // Handle the error if the feedback couldn't be saved
                    Toast.makeText(CustomerFeedback.this, "Failed to save feedback", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToFinalPage() {
        Intent intent = new Intent(getApplicationContext(), Final.class);
        startActivity(intent);
    }
}
