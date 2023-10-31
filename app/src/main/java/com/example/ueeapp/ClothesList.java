package com.example.ueeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;

public class ClothesList extends AppCompatActivity {

    ImageView navBack1;
    private TextView showCart;
    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_clothes_list);
        reference = FirebaseDatabase.getInstance().getReference("AddToCart");

        showCart =findViewById(R.id.showCart);
        btn1 =findViewById(R.id.btn1);
        btn2 =findViewById(R.id.btn2);
        btn3 =findViewById(R.id.btn3);
        btn4 =findViewById(R.id.btn4);
        btn5 =findViewById(R.id.btn5);
        btn6 =findViewById(R.id.btn6);
        btn7 =findViewById(R.id.btn7);
        navBack1 = findViewById(R.id.navBack1);

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "T-Shirt");
                parameters.put("Price", "100");
                reference.child(ID_cart).setValue(parameters);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "Dress");
                parameters.put("Price", "200");
                reference.child(ID_cart).setValue(parameters);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "Blouse");
                parameters.put("Price", "100");
                reference.child(ID_cart).setValue(parameters);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "Shirt");
                parameters.put("Price", "150");
                reference.child(ID_cart).setValue(parameters);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "Jacket");
                parameters.put("Price", "250");
                reference.child(ID_cart).setValue(parameters);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "Trouser");
                parameters.put("Price", "200");
                reference.child(ID_cart).setValue(parameters);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ID_cart=reference.push().getKey();
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("Product_Name", "Shorts");
                parameters.put("Price", "100");
                reference.child(ID_cart).setValue(parameters);
            }
        });

    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            showCart.setText(""+ snapshot.getChildrenCount());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

        showCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ShowCart.class));
            }
        });

        navBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
