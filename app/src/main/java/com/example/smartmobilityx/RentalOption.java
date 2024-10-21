package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class RentalOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_option);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(RentalOption.this, SelectVehicle.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        // Retrieve the vehicle type from the intent
        Intent intent = getIntent();
        String vehicleType = intent.getStringExtra("VEHICLE_TYPE");

        // Standard Rental Button
        Button standardRentalButton = findViewById(R.id.standardRentalButton);
        standardRentalButton.setOnClickListener(v -> {
            // Start ConfirmationActivity with the vehicle type and standard rental
            Intent confirmationIntent = new Intent(RentalOption.this, Confirmation.class);
            confirmationIntent.putExtra("VEHICLE_TYPE", vehicleType);
            confirmationIntent.putExtra("RENTAL_METHOD", "Standard Rental");
            startActivity(confirmationIntent);
        });

        // For Pick-Up Button
        Button pickUpButton = findViewById(R.id.pickUpButton);
        pickUpButton.setOnClickListener(v -> {
            // Start ConfirmationActivity with the vehicle type and pick-up
            Intent confirmationIntent = new Intent(RentalOption.this, Confirmation.class);
            confirmationIntent.putExtra("VEHICLE_TYPE", vehicleType);
            confirmationIntent.putExtra("RENTAL_METHOD", "For Pick-Up");
            startActivity(confirmationIntent);
        });
    }
}
