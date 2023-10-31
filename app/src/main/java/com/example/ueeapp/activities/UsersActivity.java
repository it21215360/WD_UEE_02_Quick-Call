package com.example.ueeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.ueeapp.adapters.UsersAdapter;
import com.example.ueeapp.databinding.ActivityUsersBinding;
import com.example.ueeapp.listeners.UserListener;
import com.example.ueeapp.models.User;
import com.example.ueeapp.utilities.Constants;
import com.example.ueeapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;
    private String selectedUserType = "All"; // Default selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        setupSearchView();
        setupSpinner();

        // Load users with default filters
        getUsers("", selectedUserType);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (selectedUserType.equals("All")) {
                    // Show a toast message indicating that the user should select a user type first
                    Toast.makeText(UsersActivity.this, "Please select a user type first", Toast.LENGTH_SHORT).show();
                } else {
                    getUsers(newText, selectedUserType); // Update user list with search query
                }
                return true;
            }
        });
    }


    private void setupSpinner() {
        binding.serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String userType = parentView.getItemAtPosition(position).toString();
                // Check if userType is not null or empty before updating selectedUserType
                if (userType != null && !userType.isEmpty()) {
                    selectedUserType = userType;
                }
                getUsers(binding.searchView.getQuery().toString(), selectedUserType); // Update user list with the selected user type and search query
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
            }
        });
    }

    private void getUsers(String searchQuery, String selectedUserType) {


        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }

                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.userType = queryDocumentSnapshot.getString(Constants.KEY_USER_TYPE);
                            user.serviceArea = queryDocumentSnapshot.getString(Constants.KEY_SERVICE_AREA);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();

                            // Check if the selected userType is "All" or matches the user's userType
                            // Exclude users with user type "Customer"
                            if ((selectedUserType.equals("All") || user.userType.equals(selectedUserType)) &&
                                    (searchQuery.isEmpty() || user.serviceArea.toLowerCase().contains(searchQuery.toLowerCase())) &&
                                    !isCustomer(user.userType)) {
                                users.add(user);
                            }


                        }

                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    private boolean isCustomer(String userType) {
        // Add conditions to identify customer users
        // For example, you can return true if the userType is "Customer"
        return userType.equals("Customer");
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override

    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), chatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}

