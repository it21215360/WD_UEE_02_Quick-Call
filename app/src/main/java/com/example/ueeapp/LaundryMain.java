package com.example.ueeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundry.activities.SignInActivity;
import com.example.laundry.databinding.ActivityLaundryMainBinding;
import com.example.laundry.databinding.ActivitySignInBinding;
import com.example.laundry.utilities.Constants;
import com.example.laundry.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LaundryMain extends AppCompatActivity {
    TextView cardServices;
    private SearchView searchView;
    private CardView card1, card2, card3, card4;
    private TextView cardNearby, cardOrder, cardStatus;
    //private ImageView imageSignOut;
    private @NonNull ActivityLaundryMainBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_main);

        preferenceManager = new PreferenceManager(this);

        //imageSignOut = findViewById(R.id.imageSignOut);
        cardServices = findViewById(R.id.cardServices);
        cardNearby = findViewById(R.id.cardNearby);
        cardStatus = findViewById(R.id.cardStatus);
        cardOrder = findViewById(R.id.cardOrder);

        /*imageSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(); // Call the signOut method when the button is clicked
            }
        });*/

        cardServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                startActivity(intent);
                finish();
            }
        });

        cardNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NearbyLaundry.class);
                startActivity(intent);
                finish();
            }
        });

        cardStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryStatus.class);
                startActivity(intent);
                finish();
            }
        });

        cardOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryOrder.class);
                startActivity(intent);
                finish();
            }
        });


        // Initialize your CardViews and TextViews
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);

        cardNearby = findViewById(R.id.cardNearby);
        cardServices = findViewById(R.id.cardServices);
        cardOrder = findViewById(R.id.cardOrder);
        cardStatus = findViewById(R.id.cardStatus);

        // Initialize the SearchView
        searchView = findViewById(R.id.searchView);

        // Set up a listener for the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query when the user submits
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search query as the user types (optional)
                performSearch(newText);
                return true;
            }
        });
    }
    private void performSearch(String query) {

        query = query.toLowerCase();

        card1.setVisibility(cardNearby.getText().toString().toLowerCase().contains(query) ? View.VISIBLE : View.GONE);
        card2.setVisibility(cardServices.getText().toString().toLowerCase().contains(query) ? View.VISIBLE : View.GONE);
        card3.setVisibility(cardOrder.getText().toString().toLowerCase().contains(query) ? View.VISIBLE : View.GONE);
        card4.setVisibility(cardStatus.getText().toString().toLowerCase().contains(query) ? View.VISIBLE : View.GONE);
    }
   /* private void showToast(String message) {
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
    }*/

}