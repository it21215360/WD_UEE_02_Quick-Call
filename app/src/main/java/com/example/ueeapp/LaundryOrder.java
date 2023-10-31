package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laundry.utilities.Constants;
import com.example.laundry.utilities.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LaundryOrder extends AppCompatActivity {

    EditText editShop;
    EditText editClothes;
    Spinner fragranceSpinner;
    EditText editTotalAmt;
    EditText editPick;
    EditText editDel;
    EditText txtUserEmail;
    Button btnSchedule;
    ImageView navOrder;
    DatabaseReference reference;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_order);

        preferenceManager = new PreferenceManager(getApplicationContext());

        String shopName = getIntent().getStringExtra("shopName");

        txtUserEmail = findViewById(R.id.userEmail);
        txtUserEmail.setEnabled(false);
        editClothes = findViewById(R.id.editClothes);
        fragranceSpinner = findViewById(R.id.fragranceSpinner);
        editTotalAmt = findViewById(R.id.editTotalAmt);
        editPick = findViewById(R.id.editPick);
        editDel = findViewById(R.id.editDel);
        btnSchedule = findViewById(R.id.btnSchedule);
        editShop = findViewById(R.id.editShop);
        editShop.setText(shopName);
        editShop.setEnabled(false);
        navOrder = findViewById(R.id.navOrder);

        reference = FirebaseDatabase.getInstance().getReference().child("Order");

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        // Retrieve the user's email from Firestore
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = database.collection("users").document(userId); // Replace with the actual user ID
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String userEmail = document.getString("email"); // Assuming "email" is the field in Firestore containing the email.
                    if (userEmail != null) {
                        txtUserEmail.setText(userEmail);
                    } else {
                        txtUserEmail.setText("Email: Not Found");
                    }
                } else {
                    txtUserEmail.setText("User Data Not Found");
                }
            } else {
                txtUserEmail.setText("Error Retrieving User Data");
            }
        });

        navOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NearbyLaundry.class);
                startActivity(intent);
                finish();
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areRequiredFieldsFilled()) {
                    insertOrder();

                    Intent intent = new Intent(LaundryOrder.this, OrderSummary.class);

                    intent.putExtra("userEmail", txtUserEmail.getText().toString());
                    intent.putExtra("shop", editShop.getText().toString());
                    intent.putExtra("noClothes", editClothes.getText().toString());
                    intent.putExtra("fragrance", fragranceSpinner.getSelectedItem().toString());
                    intent.putExtra("total", editTotalAmt.getText().toString());
                    intent.putExtra("pick", editPick.getText().toString());
                    intent.putExtra("delivery", editDel.getText().toString());

                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean areRequiredFieldsFilled() {
        String userEmail = txtUserEmail.getText().toString().trim();
        String shop = editShop.getText().toString().trim();
        String noClothes = editClothes.getText().toString().trim();
        String total = editTotalAmt.getText().toString().trim();
        String pick = editPick.getText().toString().trim();
        String delivery = editDel.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            txtUserEmail.setError("Required");
            txtUserEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(shop)) {
            editShop.setError("Required");
            editShop.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(noClothes)) {
            editClothes.setError("Required");
            editClothes.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(total)) {
            editTotalAmt.setError("Required");
            editTotalAmt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(pick)) {
            editPick.setError("Required");
            editPick.requestFocus();
            return false;
        }

        if (!isValidDate(pick)) {
            editPick.setError("Invalid date format (dd/mm/yyyy)");
            editPick.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(delivery)) {
            editDel.setError("Required");
            editDel.requestFocus();
            return false;
        }

        if (!isValidDate(delivery)) {
            editDel.setError("Invalid date format (dd/mm/yyyy)");
            editDel.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean isValidDate(String date) {
        // Define a simple regex pattern for a date in the format dd/mm/yyyy
        String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";

        // Use the pattern to match the date string
        return date.matches(datePattern);
    }


    private void insertOrder(){
        String userEmail = txtUserEmail.getText().toString();
        String shop = editShop.getText().toString();
        String noClothes = editClothes.getText().toString();
        String fragrance = fragranceSpinner.getSelectedItem().toString();
        String total = editTotalAmt.getText().toString();
        String pick = editPick.getText().toString();
        String delivery = editDel.getText().toString();

        Order order = new Order(userEmail,shop,noClothes,fragrance,total,pick,delivery);

        reference.push().setValue(order);
        Toast.makeText(LaundryOrder.this,"Scheduled!",Toast.LENGTH_SHORT).show();
    }
}