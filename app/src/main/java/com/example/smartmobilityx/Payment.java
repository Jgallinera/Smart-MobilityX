package com.example.smartmobilityx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.IOException;

public class Payment extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView userAccountTextView;
    private TextView selectvehicleButton;
    private TextView rideHistoryButton;
    private TextView logoutButton;
    private ImageButton hamburgerMenuButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        drawerLayout = findViewById(R.id.drawer_layout);
        userAccountTextView = findViewById(R.id.userAccountTextView);
        selectvehicleButton = findViewById(R.id.selectvehicleButton);
        rideHistoryButton = findViewById(R.id.rideHistoryButton);
        logoutButton = findViewById(R.id.logoutButton);
        hamburgerMenuButton = findViewById(R.id.hamburgerMenuButton);

        userAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        selectvehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, SelectVehicle.class);
                startActivity(intent);
                finish();
            }
        });

        rideHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, RideHistory.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, LandingPage.class);
                startActivity(intent);
                finish();
            }
        });

        hamburgerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        Button addPaymentMethodButton = findViewById(R.id.addPaymentMethodButton);
        addPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the payment processing code
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"data\":{\"attributes\":{\"amount\":300,\"description\":\"Payment description\",\"remarks\":\"Payment remarks\"}}}");
                Request request = new Request.Builder()
                        .url("https://api.paymongo.com/v1/links")
                        .post(body)
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .addHeader("authorization", "Basic c2tfdGVzdF9vdnc5OVoxYzZ2aGRxTEhqWG9VZVkyeUI6")
                        .build();

                // Make the network call in a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (Response response = client.newCall(request).execute()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            // Handle the response
                            String responseBody = response.body().string();
                            String paymentUrl = parsePaymentUrl(responseBody);

                            // Open the payment URL in an external browser
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
                            startActivity(browserIntent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Method to parse the payment URL from the response body
    private String parsePaymentUrl(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.getJSONObject("data").getJSONObject("attributes").getString("checkout_url");
        } catch (Exception e) {
            e.printStackTrace();
            return "https://example.com/error";  // Fallback URL in case of an error
        }
    }
}
