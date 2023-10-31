package com.example.ueeapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

public class Scanner extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;
    private androidx.appcompat.app.AlertDialog alertDialog;

    private String scannedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
                ;
        mCodeScanner = new CodeScanner(this, scannerView);
        askPermission();

        if (CameraPermission) {
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCodeScanner.startPreview();
                }
            });
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(Scanner.this, "QR Code Decoded: " + result.getText(), Toast.LENGTH_SHORT).show();
                            String qrCodeData = result.getText();
                            Log.d("Scanner", "QR Code Decoded: " + qrCodeData);

                            // Verify the user in Firebase database
                            verifyUserInDatabase(qrCodeData);
                        }
                    });
                }
            });

        }
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Scanner.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);

            } else {
                mCodeScanner.startPreview();
                CameraPermission = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCodeScanner.startPreview();
                CameraPermission = true;
            } else {

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("Please provide the camera permission for using all the features of the app")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    ActivityCompat.requestPermissions(Scanner.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("You have denied some permission. Allow all permission at [settings] > [Permission]")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package",getPackageName(), null));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("Exit app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            }).create().show();
                }

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if (CameraPermission) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    private void verifyUserInDatabase(String qrCodeData) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        // Perform a query to find a user with a matching userID
        usersRef.whereEqualTo("email", qrCodeData)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // User is found in the database, allow access
                         //   Toast.makeText(Scanner.this, "Payment success", Toast.LENGTH_SHORT).show();
                            scannedEmail = qrCodeData;
                            displayScannerView(scannedEmail);
                        } else {
                            // User is not found in the database, deny access
                            Toast.makeText(Scanner.this, "User Not Valid", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Scanner", "Error querying database: " + e.getMessage());
                        Toast.makeText(Scanner.this, "Error verifying user", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void displayScannerView(String scannedEmail) {
        if (isFinishing()) {
            return; // Activity is finishing, no need to display the AlertDialog
        }

        // Check if the AlertDialog is already showing
        if (alertDialog != null && alertDialog.isShowing()) {
            return; // AlertDialog is already displayed
        }

        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);

        // Create the child View
        View view = LayoutInflater.from(Scanner.this).inflate(R.layout.scanner_view, successConstraintLayout);
        Button successDone = view.findViewById(R.id.successDone);

        // Remove the child View from its current parent if it has one
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        // Set up the AlertDialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Scanner.this);
        builder.setView(view);
        alertDialog = builder.create(); // Store the AlertDialog as a class member

        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(Scanner.this, "Done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CustomerFeedback.class);
                intent.putExtra("userEmail", scannedEmail);
                startActivity(intent);
                finish();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }



}
