package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageButton hamburgerMenuButton;
    private TextView paymentsButton;
    private TextView rideHistoryButton;
    private TextView logoutButton;
    private RecyclerView usersRecyclerView;
    private UserAdapter usersAdapter;
    private List<Users> userList;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // Initialize UI components
        drawerLayout = findViewById(R.id.drawer_layout);
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        paymentsButton = findViewById(R.id.paymentsButton);
        rideHistoryButton = findViewById(R.id.rideHistoryButton);
        logoutButton = findViewById(R.id.logoutButton);
        hamburgerMenuButton = findViewById(R.id.hamburgerMenuButton);

        // Initialize the user list
        userList = new ArrayList<>();

        // Set up the RecyclerView
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UserAdapter(userList);
        usersRecyclerView.setAdapter(usersAdapter);

        // Initialize Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Load users from Firebase
        fetchUsers();

        // Hamburger Menu Button
        hamburgerMenuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set onClick listeners for sidebar buttons
        paymentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, AdminPayments.class);
            startActivity(intent);
        });

        rideHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboard.this, AdminRideHistory.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            Toast.makeText(AdminDashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the current activity
        });
    }

    private void fetchUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userList.clear(); // Clear the existing user list
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Users user = userSnapshot.getValue(Users.class);
                        if (user != null) {
                            // Set the phone number from the key
                            user.setPhone(userSnapshot.getKey());  // Set the phone field from the key

                            // Activate the user
                            user.setActive(true); // Automatically activate the user

                            // Log the phone number to check if it's correctly fetched
                            Log.d("AdminDashboard", "Fetched user phone: " + user.getPhone());

                            if (user.getPhone() == null || user.getPhone().isEmpty()) {
                                Log.e("AdminDashboard", "User phone is invalid or missing for user: " + user.getUserName());
                            } else {
                                userList.add(user); // Add the user to the list
                                usersRef.child(user.getPhone()).setValue(user) // Update the user in the database
                                        .addOnSuccessListener(aVoid -> Log.d("AdminDashboard", "User activated: " + user.getUserName()))
                                        .addOnFailureListener(e -> Log.e("AdminDashboard", "Error activating user: " + e.getMessage()));
                            }
                        } else {
                            Log.e("AdminDashboard", "User data is null, please check your database structure");
                        }
                    }
                    runOnUiThread(() -> usersAdapter.notifyDataSetChanged());
                } else {
                    Toast.makeText(AdminDashboard.this, "No users found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDashboard.this, "Error fetching users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
