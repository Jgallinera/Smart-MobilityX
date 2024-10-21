package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Confirmation extends AppCompatActivity {

    private static final String TAG = "Confirmation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the vehicle type and rental method from the intent
        Intent intent = getIntent();
        String vehicleType = intent.getStringExtra("VEHICLE_TYPE");
        String rentalMethod = intent.getStringExtra("RENTAL_METHOD");

        // Log the values received
        Log.d(TAG, "Vehicle Type: " + vehicleType);
        Log.d(TAG, "Rental Method: " + rentalMethod);

        // Determine the appropriate layout to set based on vehicle type and rental method
        int layoutId = R.layout.default_layout; // Fallback layout in case of an invalid state

        if (vehicleType != null && rentalMethod != null) {
            if ("eScooter".equals(vehicleType)) {
                if ("Standard Rental".equals(rentalMethod)) {
                    layoutId = R.layout.escooter_vehicleconfirmation_standardrental;
                } else if ("For Pick-Up".equals(rentalMethod)) {
                    layoutId = R.layout.escooter_vehicleconfirmation_forpickup;
                }
            } else if ("eBicycle".equals(vehicleType)) {
                if ("Standard Rental".equals(rentalMethod)) {
                    layoutId = R.layout.ebicycle_vehicleconfirmation_standardrental;
                } else if ("For Pick-Up".equals(rentalMethod)) {
                    layoutId = R.layout.ebicycle_vehicleconfirmation_forpickup;
                }
            }
        } else {
            Log.e(TAG, "Vehicle Type or Rental Method is null.");
        }

        Log.d(TAG, "Setting content view to layout ID: " + layoutId);
        setContentView(layoutId);

        // Set up UI components for the selected layout
        ImageView vehicleImageView = findViewById(R.id.vehicleImageView);
        if (vehicleImageView == null) {
            Log.e(TAG, "vehicleImageView not found in layout");
        } else {
            Log.d(TAG, "vehicleImageView found");
        }

        TextView vehicleDescriptionTextView = findViewById(R.id.vehicleDescriptionTextView);
        if (vehicleDescriptionTextView == null) {
            Log.e(TAG, "vehicleDescriptionTextView not found in layout");
        } else {
            Log.d(TAG, "vehicleDescriptionTextView found");
        }

        Button rentVehicleButton = findViewById(R.id.rentVehicleButton);
        if (rentVehicleButton == null) {
            Log.e(TAG, "rentVehicleButton not found in layout");
        } else {
            Log.d(TAG, "rentVehicleButton found");
        }

        // Dynamically set the description text
        String descriptionText = getDescriptionText(vehicleType, rentalMethod);
        if (vehicleDescriptionTextView != null) {
            vehicleDescriptionTextView.setText(descriptionText);
        }

        // Example setup for the selected layout (optional, depends on your needs)
        if (vehicleImageView != null) {
            if ("eScooter".equals(vehicleType)) {
                vehicleImageView.setImageResource(R.drawable.escooter_img);
            } else if ("eBicycle".equals(vehicleType)) {
                vehicleImageView.setImageResource(R.drawable.ebicycle_img);
            }
        }

        // Handle the "Rent Vehicle" button click
        if (rentVehicleButton != null) {
            rentVehicleButton.setOnClickListener(v -> {
                Intent qrCodeScannerIntent = new Intent(Confirmation.this, RideActivity.class);
                Log.d(TAG, "Launching QRCodeScanner activity");
                startActivity(qrCodeScannerIntent);
            });
        }

        // Back Button
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                Intent backIntent = new Intent(Confirmation.this, RentalOption.class);
                backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(backIntent);
                finish();
            });
        } else {
            Log.e(TAG, "backButton not found in layout");
        }
    }

    // Method to generate the description text based on vehicle type and rental method
    private String getDescriptionText(String vehicleType, String rentalMethod) {
        if ("eScooter".equals(vehicleType)) {
            if ("Standard Rental".equals(rentalMethod)) {
                return "You have chosen to rent an eScooter for Standard Rental. Enjoy your ride!";
            } else if ("For Pick-Up".equals(rentalMethod)) {
                return "You have chosen to rent an eScooter for Pick-Up. Enjoy your ride!";
            }
        } else if ("eBicycle".equals(vehicleType)) {
            if ("Standard Rental".equals(rentalMethod)) {
                return "You have chosen to rent an eBicycle for Standard Rental. Enjoy your ride!";
            } else if ("For Pick-Up".equals(rentalMethod)) {
                return "You have chosen to rent an eBicycle for Pick-Up. Enjoy your ride!";
            }
        }
        return "Invalid selection.";
    }
}
