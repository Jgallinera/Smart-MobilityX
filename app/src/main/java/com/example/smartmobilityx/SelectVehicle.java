package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SelectVehicle extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button confirmButton;
    private ImageView scooterImageView;
    private ImageView bikeImageView;
    private CardView scooterCardView;
    private CardView bikeCardView;
    private boolean isScooterSelected = false;
    private boolean isBikeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectvehicle);


        // Initialize card views and image views for scooter and bike selection
        scooterImageView = findViewById(R.id.scooterImageView);
        bikeImageView = findViewById(R.id.bikeImageView);
        scooterCardView = findViewById(R.id.scooterCardView);
        bikeCardView = findViewById(R.id.bikeCardView);
        confirmButton = findViewById(R.id.confirmButton);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(SelectVehicle.this, Maps.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        // Set click listeners for scooter and bike card views
        scooterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSelection(true);
            }
        });

        bikeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSelection(false);
            }
        });

        // Set click listener for confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScooterSelected) {
                    // Handle scooter selection confirmation
                } else if (isBikeSelected) {
                    // Handle bike selection confirmation
                }
            }
        });
    }

    // Handle selection animations for scooter and bike
    private void handleSelection(boolean selectScooter) {
        if (selectScooter) {
            if (isScooterSelected) {
                // Deselect scooter
                scooterCardView.setBackgroundResource(R.drawable.border_default);
                isScooterSelected = false;
                confirmButton.setVisibility(View.GONE);
            } else {
                // Select scooter
                scooterCardView.setBackgroundResource(R.drawable.green_border);
                bikeCardView.setBackgroundResource(R.drawable.border_default);
                isScooterSelected = true;
                isBikeSelected = false;
                confirmButton.setVisibility(View.VISIBLE);
            }
        } else {
            if (isBikeSelected) {
                // Deselect bike
                bikeCardView.setBackgroundResource(R.drawable.border_default);
                isBikeSelected = false;
                confirmButton.setVisibility(View.GONE);
            } else {
                // Select bike
                bikeCardView.setBackgroundResource(R.drawable.green_border);
                scooterCardView.setBackgroundResource(R.drawable.border_default);
                isBikeSelected = true;
                isScooterSelected = false;
                confirmButton.setVisibility(View.VISIBLE);
            }
        }

        confirmButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectVehicle.this, RentalOption.class);

            // Determine which vehicle is selected and pass it
            if (isScooterSelected) {
                intent.putExtra("VEHICLE_TYPE", "eScooter");
            } else if (isBikeSelected) {
                intent.putExtra("VEHICLE_TYPE", "eBicycle");
            }

            // Start RentalOption activity
            startActivity(intent);
        });
    }
}