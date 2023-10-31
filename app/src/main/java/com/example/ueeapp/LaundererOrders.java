package com.example.ueeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laundry.activities.SignInActivity;
import com.example.laundry.databinding.ActivityLaundryMainBinding;
import com.example.laundry.utilities.Constants;
import com.example.laundry.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LaundererOrders extends AppCompatActivity {

    private RecyclerView rec1;
    private List<LaundererOrders_Model> laundererOrders_modelList = new ArrayList<>();
    //private ImageView imageSignOut1;
    private @NonNull ActivityLaundryMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launderer_orders);

        preferenceManager = new PreferenceManager(this);

       // imageSignOut1 = findViewById(R.id.imageSignOut1);
        rec1 = findViewById(R.id.rec1);
        rec1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

      /*  imageSignOut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(); // Call the signOut method when the button is clicked
            }
        });*/

        DatabaseReference show_info = FirebaseDatabase.getInstance().getReference("LaundryOrders");

        show_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    laundererOrders_modelList.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        LaundererOrders_Model laundererOrders_model = snapshot1.getValue(LaundererOrders_Model.class);
                        laundererOrders_modelList.add(laundererOrders_model);
                    }
                    rec1.setAdapter(new LaundererOrders_Adapter(getApplicationContext(), laundererOrders_modelList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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