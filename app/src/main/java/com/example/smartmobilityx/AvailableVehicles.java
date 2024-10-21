package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AvailableVehicles extends AppCompatActivity {

    private TextView locationTextView; // TextView for displaying the selected location
    private TextView availableEBicyclesTextView;
    private TextView availableEScootersTextView;
    private Button selectVehicleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_vehicles);

        // Initialize the TextViews and Button
        locationTextView = findViewById(R.id.locationTextView); // Make sure this ID matches your layout
        availableEBicyclesTextView = findViewById(R.id.availableEBicyclesTextView);
        availableEScootersTextView = findViewById(R.id.availableEScootersTextView);
        selectVehicleButton = findViewById(R.id.selectVehicleButton);

        // Retrieve the selected location from the Intent
        String selectedLocation = getIntent().getStringExtra("selectedLocation");
        if (selectedLocation != null) {
            // Display the selected location at the top
            locationTextView.setText("Available Vehicles near: " + selectedLocation);
        }

        // Retrieve the number of available vehicles from the Intent
        int availableEBicycles = getIntent().getIntExtra("available_e_bicycles", 0);
        int availableEScooters = getIntent().getIntExtra("available_e_scooters", 0);

        // Set the text for available vehicles
        availableEBicyclesTextView.setText("Available E-Bicycles: " + availableEBicycles);
        availableEScootersTextView.setText("Available E-Scooters: " + availableEScooters);

        // Set click listener for the button to navigate to SelectVehicle activity
        selectVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(SelectVehicle.class);
            }
        });
    }

    private void navigateTo(Class<?> destinationClass) {
        Intent intent = new Intent(AvailableVehicles.this, destinationClass);
        startActivity(intent);
    }
}