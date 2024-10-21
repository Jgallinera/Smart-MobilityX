package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView homeButton;
    private TextView paymentButton;
    private TextView rideHistoryButton;
    private TextView logoutButton;
    private ImageButton hamburgerMenuButton;
    private TextView profileTitle;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText mobileNumberEditText;
    private EditText userNameEditText;
    private Button editDetailsButton;
    private Button saveDetailsButton;

    private DatabaseReference databaseReference;
    private UserPreferences userPreferences;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        drawerLayout = findViewById(R.id.drawer_layout);
        homeButton = findViewById(R.id.homeButton);
        paymentButton = findViewById(R.id.paymentButton);
        rideHistoryButton = findViewById(R.id.rideHistoryButton);
        logoutButton = findViewById(R.id.logoutButton);
        hamburgerMenuButton = findViewById(R.id.hamburgerMenuButton);
        profileTitle = findViewById(R.id.profileTitle);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        userNameEditText = findViewById(R.id.userNameEditText);
        editDetailsButton = findViewById(R.id.editDetailsButton);
        saveDetailsButton = findViewById(R.id.saveDetailsButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userPreferences = new UserPreferences(this);

        // Load user data from SharedPreferences
        String firstName = userPreferences.getFirstName();
        String lastName = userPreferences.getLastName();
        String email = userPreferences.getEmail();
        String mobileNumber = userPreferences.getMobileNumber();
        String username = userPreferences.getUsername();

        // Set initial values
        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        mobileNumberEditText.setText(mobileNumber);
        userNameEditText.setText(username);

        // Initially set EditTexts to non-editable
        setEditMode(false);

        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(UserProfile.this, SelectVehicle.class);
            startActivity(homeIntent);
            finish();
        });

        paymentButton.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(UserProfile.this, Payment.class);
            startActivity(paymentIntent);
            finish();
        });

        rideHistoryButton.setOnClickListener(v -> {
            Intent rideHistoryIntent = new Intent(UserProfile.this, RideHistory.class);
            startActivity(rideHistoryIntent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(UserProfile.this, LandingPage.class);
            startActivity(logoutIntent);
            finish();
        });

        hamburgerMenuButton.setOnClickListener(v -> openDrawer());

        editDetailsButton.setOnClickListener(v -> {
            if (isEditing) {
                // Save changes
                saveUserDetails();
            } else {
                // Switch to edit mode
                setEditMode(true);
            }
            isEditing = !isEditing;
        });
    }

    private void setEditMode(boolean isEditing) {
        firstNameEditText.setEnabled(isEditing);
        lastNameEditText.setEnabled(isEditing);
        mobileNumberEditText.setEnabled(isEditing);
        userNameEditText.setEnabled(isEditing);
        editDetailsButton.setText(isEditing ? R.string.save_details : R.string.edit_details);
    }

    private void saveUserDetails() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String username = userNameEditText.getText().toString().trim();

        // Update SharedPreferences
        userPreferences.setFirstName(firstName);
        userPreferences.setLastName(lastName);
        userPreferences.setMobileNumber(mobileNumber);
        userPreferences.setUsername(username);

        // Update database
        String userId = userPreferences.getUserId(); // Ensure you have userId in SharedPreferences
        if (userId != null) {
            databaseReference.child(userId).child("firstName").setValue(firstName);
            databaseReference.child(userId).child("lastName").setValue(lastName);
            databaseReference.child(userId).child("mobileNumber").setValue(mobileNumber);
            databaseReference.child(userId).child("username").setValue(username)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserProfile.this, "Details updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfile.this, "Failed to update details", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(UserProfile.this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        // Switch back to non-editable mode
        setEditMode(false);
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
