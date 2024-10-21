package com.example.smartmobilityx;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RideActivity extends AppCompatActivity {

    private Button startButton;
    private Button endButton;
    private long startTime;
    private long endTime;
    private final double ratePerMinute = 3.0; // Define your rate per minute

    // Create the ActivityResultLauncher for the Omni app
    private final ActivityResultLauncher<Intent> launchOmniApp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, "Returned from Omni app", Toast.LENGTH_SHORT).show();
                    // Continue your ride logic or any other necessary actions
                } else {
                    Toast.makeText(this, "No result from Omni app", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_activity);

        startButton = findViewById(R.id.startRideButton);
        endButton = findViewById(R.id.endRideButton); // Initialize end button

        startButton.setOnClickListener(v -> {
            startRide();
        });

        endButton.setOnClickListener(v -> {
            endRide();
        });
    }

    // This method handles the start of the ride
    private void startRide() {
        startTime = System.currentTimeMillis(); // Record the start time
        Toast.makeText(this, "Ride started", Toast.LENGTH_SHORT).show();
        redirectToOmniBLEToolApp(); // Redirect to Omni app
    }

    // This method handles the end of the ride
    private void endRide() {
        endTime = System.currentTimeMillis(); // Record the end time
        long durationInMillis = endTime - startTime; // Calculate duration
        long durationInMinutes = durationInMillis / (1000 * 60); // Convert to minutes
        double fare = durationInMinutes * ratePerMinute; // Calculate fare

        Toast.makeText(this, "Ride ended. Duration: " + durationInMinutes + " minutes, Fare: Php" + fare, Toast.LENGTH_LONG).show();

        // Pass the fare to Payment activity
        Intent intent = new Intent(RideActivity.this, Payment.class);
        intent.putExtra("fare_amount", fare);  // Pass fare as extra
        startActivity(intent);  // Start Payment activity
    }

    // This method handles the redirection to the Omni BLETool app
    private void redirectToOmniBLEToolApp() {
        Intent intent = new Intent();
        intent.setClassName("com.omni.production.check", "com.omni.production.check.activity.WelcomeActivity");

        try {
            startActivity(intent); // Attempt to start the activity
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find Omni app. Please install it.", Toast.LENGTH_LONG).show();
        }
    }


}
