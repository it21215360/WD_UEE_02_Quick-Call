package com.example.ueeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowCart extends AppCompatActivity {

    private RecyclerView rec;
    private List<ShowCart_Model> showCart_modelList = new ArrayList<>();
    private TextView totalPriceTextView;
    ImageView navBack2;
    Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);

        navBack2 = findViewById(R.id.navBack2);
        btnNext = findViewById(R.id.btnNext);
        rec = findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        navBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClothesList.class);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NearbyLaundry.class);
                startActivity(intent);
                finish();
            }
        });

        DatabaseReference show_info = FirebaseDatabase.getInstance().getReference("AddToCart");

        show_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            showCart_modelList.clear();
                            int totalPrice = 0;

                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                ShowCart_Model showCart_model = snapshot1.getValue(ShowCart_Model.class);
                                showCart_modelList.add(showCart_model);

                                // Calculate the total price
                                int price = Integer.parseInt(showCart_model.getPrice());
                                totalPrice += price;
                            }
                            // Set the total price to the TextView
                            totalPriceTextView.setText("Total Price: $" + totalPrice);

                            rec.setAdapter(new ShowCart_Adapter(getApplicationContext(), showCart_modelList));
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}