package com.example.ueeapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.ueeapp.utilities.Constants;
import com.example.ueeapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AllServices extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    private AppCompatImageView signOutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);

        // Find the ImageButtons by their IDs
        ImageButton laundryService = findViewById(R.id.laundryService);
        ImageButton electricalService = findViewById(R.id.electricalService);
        ImageButton plumbingService = findViewById(R.id.plumbingService);
        ImageButton chatService = findViewById(R.id.chatService);
        Button scanner = findViewById(R.id.scanner);
        signOutBtn = findViewById(R.id.imageSignOutTwo);

        preferenceManager = new PreferenceManager(getApplicationContext());


        // Set up OnClickListener for each button
        laundryService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action when imageButtonService1 is clicked
                Intent intent = new Intent(AllServices.this, LaundryMain.class);
                startActivity(intent);
               /* setListeners();*/
            }

        });

        electricalService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action when imageButtonService2 is clicked
                Intent intent = new Intent(AllServices.this, Home.class);
                intent.putExtra("serviceType", "electrical");
                startActivity(intent);
            }
        });

        plumbingService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action when imageButtonService3 is clicked
                Intent intent = new Intent(AllServices.this, Home.class);
                intent.putExtra("serviceType", "plumbing");
                startActivity(intent);
            }
        });

        chatService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action when imageButtonService4 is clicked
                Intent intent = new Intent(AllServices.this, MainActivity.class);
                startActivity(intent);
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action when imageButtonService4 is clicked
                Intent intent = new Intent(AllServices.this, Scanner.class);
                startActivity(intent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }
   /* private void setListeners() {
        signOutBtn.setOnClickListener(view -> signOut());
    }*/

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

}
