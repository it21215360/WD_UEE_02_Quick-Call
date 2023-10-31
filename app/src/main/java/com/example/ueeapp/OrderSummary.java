package com.example.ueeapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.laundry.utilities.Constants;
import com.example.laundry.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderSummary extends AppCompatActivity {

    ImageView navSummary;
    TextView txtSumOrderId, sumShop, sumClothes, sumFragrance, sumTotal, sumPick, sumDelivery;
    Button btnConfirm;
    DatabaseReference reference;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        preferenceManager = new PreferenceManager(getApplicationContext());


        final String[] userEmail = {getIntent().getStringExtra("userEmail")};
        String shop = getIntent().getStringExtra("shop");
        String noClothes = getIntent().getStringExtra("noClothes");
        String fragrance = getIntent().getStringExtra("fragrance");
        String total = getIntent().getStringExtra("total");
        String pick = getIntent().getStringExtra("pick");
        String delivery = getIntent().getStringExtra("delivery");

        navSummary = findViewById(R.id.navSummary);
        btnConfirm = findViewById(R.id.btnConfirm);
        txtSumOrderId = findViewById(R.id.sumOrderId);
        txtSumOrderId.setText(userEmail[0]);
        sumShop = findViewById(R.id.sumShop);
        sumShop.setText(shop);
        sumClothes = findViewById(R.id.sumClothes);
        sumClothes.setText(noClothes);
        sumFragrance = findViewById(R.id.sumFragrance);
        sumFragrance.setText(fragrance);
        sumTotal = findViewById(R.id.sumTotal);
        sumTotal.setText(total);
        sumPick = findViewById(R.id.sumPick);
        sumPick.setText(pick);
        sumDelivery = findViewById(R.id.sumDelivery);
        sumDelivery.setText(delivery);

        reference = FirebaseDatabase.getInstance().getReference().child("LaundryOrders");

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        // Retrieve the user's email from Firestore
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = database.collection("users").document(userId); // Replace with the actual user ID
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userEmail[0] = document.getString("email");
                    if (userEmail[0] != null) {
                        txtSumOrderId.setText(userEmail[0]);
                    } else {
                        txtSumOrderId.setText("Email: Not Found");
                    }
                } else {
                    txtSumOrderId.setText("User Data Not Found");
                }
            } else {
                txtSumOrderId.setText("Error Retrieving User Data");
            }
        });


        navSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaundryOrder.class);
                startActivity(intent);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessDialog();
            }
        });
    }

    private void showSuccessDialog(){
        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(OrderSummary.this).inflate(R.layout.success_dialog, successConstraintLayout);
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummary.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                insertOrder();
                Toast.makeText(OrderSummary.this, "Done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaundryMain.class);
                startActivity(intent);
                finish();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void insertOrder(){
        String userEmail = txtSumOrderId.getText().toString();
        String shop = sumShop.getText().toString();
        String noClothes = sumClothes.getText().toString();
        String fragrance = sumFragrance.getText().toString();
        String total = sumTotal.getText().toString();
        String pick = sumPick.getText().toString();
        String delivery = sumDelivery.getText().toString();

        Order order = new Order(userEmail,shop,noClothes,fragrance,total,pick,delivery);

        reference.push().setValue(order);
    }
}
