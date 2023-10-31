package com.example.ueeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class NearbyLaundry extends AppCompatActivity {

    ImageView navBack4;
    RecyclerView recyclerView;
    Spinner spinnerFilter;
    Shop_Adapter adapter;
    List<Shop> shopList;
    SearchView searchView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_laundry);

        navBack4 = findViewById(R.id.navBack4);
        recyclerView = findViewById(R.id.shopRecyclerView);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        navBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowCart.class);
                startActivity(intent);
                finish();
            }
        });

        shopList = new ArrayList<>();
        shopList.add(new Shop("A Plus Cleaners", "Ragama", "8:00 AM - 20:00 PM", 4.8f, R.drawable.shop1));
        shopList.add(new Shop("Clean Sweep ", "Dehiwala", "9:00 AM - 20:30 PM", 4.6f, R.drawable.shop2));
        shopList.add(new Shop("Dust to Shine", "Seeduwa", "8:30 AM - 21:00 PM", 4.7f, R.drawable.shop3));
        shopList.add(new Shop("Express Cleaners ", "Moratuwa", "9:00 AM - 21:00 PM", 4.1f, R.drawable.shop4));
        shopList.add(new Shop("Rainbow Cleaners", "Ragama", "10:00 AM - 20:30 PM", 4.5f, R.drawable.shop5));
        shopList.add(new Shop("Quick Wash Laundry", "Ragama", "7:00 AM - 19:30 PM", 4.5f, R.drawable.shop6));
        shopList.add(new Shop("The Laundry Lounge ", "Seeduwa", "8:00 AM - 20:30 PM", 4.7f, R.drawable.shop7));
        shopList.add(new Shop("Spiffy Clean ", "Dehiwala", "9:00 AM - 20:30 PM", 4.2f, R.drawable.shop8));

        adapter = new Shop_Adapter(shopList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected filter option
                String selectedFilter = parent.getItemAtPosition(position).toString();

                // Filter the shopList based on the selected option
                filterShopList(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected (if needed)
            }
        });
    }

    // Method to filter the shopList based on the selected filter
    private void filterShopList(String selectedFilter) {
        List<Shop> filteredList = new ArrayList<>();
        for (Shop shop : shopList) {
            if (selectedFilter.equals("Select Your Location") || shop.getCity().equals(selectedFilter)) {
                filteredList.add(shop);
            }
        }
        // Update the adapter's data with the filtered list
        adapter.updateShopList(filteredList);


        // Initialize the SearchView
        searchView1 = findViewById(R.id.searchView1);

        // Set up a listener for the search query
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query when the user submits
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search query as the user types (optional)
                performSearch(newText);
                return true;
            }
        });
    }

    // Method to perform the search

    private void performSearch(String query) {
        // Convert the query to lowercase for case-insensitive search
        query = query.toLowerCase();

        List<Shop> filteredList = new ArrayList<>();

        // Iterate through the original shopList and add matching shops to the filteredList
        for (Shop shop : shopList) {
            if (query.isEmpty() || shop.getName().toLowerCase().contains(query) || shop.getCity().toLowerCase().contains(query) || shop.getTime().toLowerCase().contains(query)) {
                filteredList.add(shop);
            }
        }

        // Update the adapter's data with the filtered list
        adapter.updateShopList(filteredList);

        
    }
}