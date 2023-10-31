package com.example.ueeapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ueeapp.R;
import com.example.ueeapp.databinding.ActivitySignUpBinding;
import com.example.ueeapp.utilities.Constants;
import com.example.ueeapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    //RadioGroup professionRadioGroup = findViewById(R.id.professionRadioGroup);
    //Spinner serviceAreaSpinner = findViewById(R.id.serviceAreaSpinner);
    //String[] serviceAreaOptions = getResources().getStringArray(R.array.service_area_options);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        initUI();
    }
    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void initUI() {
        // Initialize the RadioGroup and Spinner.
        RadioGroup professionRadioGroup = findViewById(R.id.professionRadioGroup);
        Spinner serviceAreaSpinner = findViewById(R.id.serviceAreaSpinner);

        // Retrieve the service area options from the string array resource.
        String[] serviceAreaOptions = getResources().getStringArray(R.array.service_area_options);

        // Create an ArrayAdapter for the Spinner.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceAreaOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceAreaSpinner.setAdapter(spinnerAdapter);

        // Add a listener to the RadioGroup to show/hide the Spinner based on the selected option.
        professionRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCustomer) {
                // Hide the Service Area field for customers
                serviceAreaSpinner.setVisibility(View.GONE);
            } else {
                // Show the Service Area field for other professions
                serviceAreaSpinner.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showToast(String message) {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
    }

    private void signUp() {
          loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        
        user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);

        // Check which user type is selected
        RadioGroup professionRadioGroup = findViewById(R.id.professionRadioGroup);
        int selectedProfessionId = professionRadioGroup.getCheckedRadioButtonId();

        Spinner serviceAreaSpinner = findViewById(R.id.serviceAreaSpinner);
        String selectedServiceArea = serviceAreaSpinner.getSelectedItem().toString();

        if (selectedProfessionId == R.id.radioCustomer) {
            user.put(Constants.KEY_USER_TYPE, "Customer");
        } else if (selectedProfessionId == R.id.radioPlumber) {
            user.put(Constants.KEY_USER_TYPE, "Plumber");
            user.put(Constants.KEY_SERVICE_AREA, selectedServiceArea);
        }else if (selectedProfessionId == R.id.radioElectrician) {
            user.put(Constants.KEY_USER_TYPE, "Electrician");
            user.put(Constants.KEY_SERVICE_AREA, selectedServiceArea);
        }else if (selectedProfessionId == R.id.radioLaunderer) {
            user.put(Constants.KEY_USER_TYPE, "Launderer");
            user.put(Constants.KEY_SERVICE_AREA, selectedServiceArea);
        }

        if (professionRadioGroup.getCheckedRadioButtonId() == R.id.radioLaunderer || professionRadioGroup.getCheckedRadioButtonId() == R.id.radioElectrician || professionRadioGroup.getCheckedRadioButtonId() == R.id.radioPlumber) {
            // Create a bundle to pass data to the DisplayQRActivity
            Bundle bundle = new Bundle();
            bundle.putString("email", binding.inputEmail.getText().toString());

            Intent displayQRIntent = new Intent(getApplicationContext(), UniqueQRCode.class);
            displayQRIntent.putExtras(bundle);
            startActivity(displayQRIntent);
        }
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                  /*  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                    RadioGroup professionRadioGroupTwo = findViewById(R.id.professionRadioGroup);
                    int selectedProfessionIdTwo = professionRadioGroupTwo.getCheckedRadioButtonId();

                    if (selectedProfessionIdTwo == R.id.radioCustomer) {
                        user.put(Constants.KEY_USER_TYPE, "Customer");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (selectedProfessionIdTwo == R.id.radioPlumber || selectedProfessionIdTwo == R.id.radioElectrician) {
                        user.put(Constants.KEY_USER_TYPE, "Plumber");
                        user.put(Constants.KEY_SERVICE_AREA, selectedServiceArea);
                        Intent intent = new Intent(getApplicationContext(), SPP_MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (selectedProfessionIdTwo == R.id.radioLaunderer) {
                        user.put(Constants.KEY_USER_TYPE, "Launderer");
                        user.put(Constants.KEY_SERVICE_AREA, selectedServiceArea);
                        Intent intent = new Intent(getApplicationContext(), LaundererOrders.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });


    }

    private String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

    );
    private Boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast("Select profile image");
            return false;
        } else if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
             return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid emil");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm Your password");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Password and confirm password must be same");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }





}