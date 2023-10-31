package com.example.ueeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ueeapp.databinding.ActivitySignInBinding;
import com.example.ueeapp.utilities.Constants;
import com.example.ueeapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            // User is already signed in
            redirectToAppropriatePage(); // Handle redirection based on user role here
            finish(); // Finish this activity
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void redirectToAppropriatePage() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null) {
                            String userRole = documentSnapshot.getString(Constants.KEY_USER_TYPE);
                            if ("Customer".equals(userRole)) {
                                redirectToServices(documentSnapshot);
                            } else if ("Plumber".equals(userRole) || "Electrician".equals(userRole)) {
                                redirectToDashboard1(documentSnapshot);
                            } else if ("Launderer".equals(userRole)) {
                                redirectToDashboardTwo(documentSnapshot);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error While Logging", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    private void setListener() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
          binding.buttonSignIn.setOnClickListener(v -> {
              if(isValidSignInDetails()) {
                  signIn();
              }
          });
    }


    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String userRole = documentSnapshot.getString(Constants.KEY_USER_TYPE);
                        Log.d("SignInActivity", "User Role: " + userRole);
                        if (userRole != null) {
                            if ("Customer".equals(userRole)) {
                                redirectToServices(documentSnapshot);
                            } else if ("Plumber".equals(userRole) || "Electrician".equals(userRole)) {
                                redirectToDashboard1(documentSnapshot);
                            } else if ("Launderer".equals(userRole)) {
                                redirectToDashboardTwo(documentSnapshot);
                            } else {
                                loading(false);
                                showToast("Unknown user role");
                            }
                        } else {
                            loading(false);
                            showToast("User role is null");
                        }
                    } else {
                        loading(false);
                        showToast("Unable to sign in ");
                    }
                });
    }

    private void redirectToServices(DocumentSnapshot documentSnapshot) {
        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
        preferenceManager.putString(Constants.KEY_USER_TYPE, documentSnapshot.getString(Constants.KEY_USER_TYPE));

        Intent intent = new Intent(getApplicationContext(), AllServices.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void redirectToDashboard1(DocumentSnapshot documentSnapshot) {
        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
        preferenceManager.putString(Constants.KEY_USER_TYPE, documentSnapshot.getString(Constants.KEY_USER_TYPE));
        // Redirect to Dashboard1 for plumbers and electricians
        Intent intent = new Intent(getApplicationContext(), SPP_MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void redirectToDashboardTwo(DocumentSnapshot documentSnapshot) {
        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
        preferenceManager.putString(Constants.KEY_USER_TYPE, documentSnapshot.getString(Constants.KEY_USER_TYPE));
        // Redirect to DashboardTwo for launderer
        Intent intent = new Intent(getApplicationContext(), LaundererOrders.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }
    }

}