package com.example.ueeapp.activities;


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UniqueQRCode extends AppCompatActivity {
    ImageView qrCodeImageView;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_qrcode);
        qrCodeImageView = findViewById(R.id.iv_qr);
        backButton = findViewById(R.id.back);

        // Get the UID of the currently logged-in user using Firebase Authentication
        String userEmail = getIntent().getStringExtra("email");

        // Generate the QR code
        try {
            // Check if the QR code image already exists in internal storage
            File qrCodeFile = new File(getFilesDir(), userEmail + ".png");
            if (qrCodeFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(qrCodeFile.getAbsolutePath());
                qrCodeImageView.setImageBitmap(bitmap);
            } else {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(userEmail, BarcodeFormat.QR_CODE, 400, 400);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrCodeImageView.setImageBitmap(bitmap);

                // Save the QR code image to internal storage
                saveQRCodeToStorage(bitmap, userEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the userType from the previous activity
                String userType = getIntent().getStringExtra("userType");
                Intent intent;

                if ("plumber".equals(userType) || "electrician".equals(userType)) {
                    // Navigate to page1 (Replace Page1Activity with the actual activity for plumbers and electricians)
                    intent = new Intent(UniqueQRCode.this, SPP_MainActivity.class);
                    startActivity(intent);
                } else if ("laundere".equals(userType)) {
                    // Navigate to page2 (Replace Page2Activity with the actual activity for launderers)
                    intent = new Intent(UniqueQRCode.this, LaundererOrders.class);
                    startActivity(intent);
                }


            }
        });

    }

    private void saveQRCodeToStorage(Bitmap qrCodeBitmap, String uid) {
        try {
            // Save to internal storage
            File qrCodeFile = new File(getFilesDir(), uid + ".png");
            FileOutputStream out = new FileOutputStream(qrCodeFile);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            // Insert image into MediaStore to make it visible in the gallery
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, uid + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            }

            // Display a Toast message to notify the user that the QR code has been saved
            Toast.makeText(this, "QR code saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFirebaseUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Get the user's UID
            return user.getUid();
        } else {
            // Handle the case where the user is not authenticated or the user is null.
            // You can return an appropriate value or handle the situation as needed.
            return "UserNotAuthenticated";
        }
    }
}
