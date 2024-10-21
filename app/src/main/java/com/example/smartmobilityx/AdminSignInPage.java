package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSignInPage extends AppCompatActivity {

    private static final String TAG = "AdminSignInPage";

    private EditText userNameEmailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordPrompt;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signin);

        userNameEmailEditText = findViewById(R.id.UserNameEmailTF);
        passwordEditText = findViewById(R.id.PasswordTF);
        loginButton = findViewById(R.id.LoginBtn);
        forgotPasswordPrompt = findViewById(R.id.forgotPasswordPrompt);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smxdatabase-40256-default-rtdb.firebaseio.com/");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgotPasswordPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToForgotPasswordPage();
            }
        });

    }

    private void loginUser() {
        String usernameEmail = userNameEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (usernameEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(AdminSignInPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbEmail = userSnapshot.child("email").getValue(String.class);
                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if ((dbEmail != null && dbEmail.equals(usernameEmail))) {
                        userFound = true;
                        if (dbPassword != null && dbPassword.equals(password)) {
                            Toast.makeText(AdminSignInPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Login successful, navigating to AdminDashboard page");
                            navigateToAdminDashboardPage();
                            return;
                        } else {
                            Toast.makeText(AdminSignInPage.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Invalid password entered");
                            return;
                        }
                    }
                }
                if (!userFound) {
                    Toast.makeText(AdminSignInPage.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(AdminSignInPage.this, "Failed to login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToAdminDashboardPage() {
        Log.d(TAG, "Navigating to Admin Dashboard page");
        Intent intent = new Intent(AdminSignInPage.this, AdminDashboard.class);
        startActivity(intent);
        finish();
    }

    private void navigateToForgotPasswordPage() {
        Log.d(TAG, "Navigating to Forgot Password page");
        Intent intent = new Intent(AdminSignInPage.this, ForgotPassword.class);
        startActivity(intent);
    }

}
