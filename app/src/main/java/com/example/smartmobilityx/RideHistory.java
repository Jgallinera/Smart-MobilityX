package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartmobilityx.R;

import java.util.ArrayList;
import java.util.List;

public class RideHistory extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView userAccountTextView;
    private TextView selectvehicleButton;
    private TextView paymentButton;
    private TextView rideHistoryButton;

    private TextView logoutButton;
    private ImageButton hamburgerMenuButton;
    private RecyclerView recyclerView;
    private RideHistoryAdapter adapter;
    private List<RideHistoryItem> rideHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_history);

        drawerLayout = findViewById(R.id.drawer_layout);
        userAccountTextView = findViewById(R.id.userAccountTextView);
        selectvehicleButton = findViewById(R.id.selectvehicleButton);
        paymentButton = findViewById(R.id.paymentButton);
        rideHistoryButton = findViewById(R.id.rideHistoryButton);
        logoutButton = findViewById(R.id.logoutButton);
        hamburgerMenuButton = findViewById(R.id.hamburgerMenuButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rideHistoryList = new ArrayList<>();
        // Add sample data
        rideHistoryList.add(new RideHistoryItem("Sample Start Point 1", "Sample End Point 1", "15 mins", "$20"));
        // Add more sample data or fetch from a server/database

        adapter = new RideHistoryAdapter(rideHistoryList);
        recyclerView.setAdapter(adapter);

        // Set click listeners for sidebar buttons
        userAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UserProfile
                Intent intent = new Intent(RideHistory.this, UserProfile.class);
                startActivity(intent);
            }
        });

        selectvehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideHistory.this, SelectVehicle.class);
                startActivity(intent);
                finish();
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideHistory.this, Payment.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideHistory.this, LandingPage.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });

        // Set click listener for the hamburger menu button
        hamburgerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(); // Open the sidebar
            }
        });
    }

    // Handle the opening and closing of the sidebar
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    // Static inner class for the data model
    public class RideHistoryItem {
        private String startPoint;
        private String endPoint;
        private String travelTime;
        private String amountPaid;

        public RideHistoryItem(String startPoint, String endPoint, String travelTime, String amountPaid) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.travelTime = travelTime;
            this.amountPaid = amountPaid;
        }

        public String getStartPoint() {
            return startPoint;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public String getTravelTime() {
            return travelTime;
        }

        public String getAmountPaid() {
            return amountPaid;
        }
    }

    // Static inner class for RecyclerView adapter
    public static class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.ViewHolder> {
        private List<RideHistoryItem> rideHistoryList;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView startPointTextView;
            public TextView endPointTextView;
            public TextView travelTimeTextView;
            public TextView amountPaidTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                startPointTextView = itemView.findViewById(R.id.startPointTextView);
                endPointTextView = itemView.findViewById(R.id.endPointTextView);
                travelTimeTextView = itemView.findViewById(R.id.travelTimeTextView);
                amountPaidTextView = itemView.findViewById(R.id.paymentAmountTextView);
            }
        }

        public RideHistoryAdapter(List<RideHistoryItem> rideHistoryList) {
            this.rideHistoryList = rideHistoryList;
        }

        @NonNull
        @Override
        public RideHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_history_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RideHistoryItem item = rideHistoryList.get(position);
            holder.startPointTextView.setText("Start Point: " + item.getStartPoint());
            holder.endPointTextView.setText("End Point: " + item.getEndPoint());
            holder.travelTimeTextView.setText("Travel Time: " + item.getTravelTime());
            holder.amountPaidTextView.setText("Amount Paid: " + item.getAmountPaid());
        }

        @Override
        public int getItemCount() {
            return rideHistoryList.size();
        }
    }
}