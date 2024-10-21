package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "LoginPage";

    private EditText mobileNumberEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpPrompt;
    private TextView forgotPasswordPrompt;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Initialize views
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        passwordEditText = findViewById(R.id.PasswordTF);
        loginButton = findViewById(R.id.LoginBtn);
        signUpPrompt = findViewById(R.id.signUpPrompt);
        forgotPasswordPrompt = findViewById(R.id.forgotPasswordPrompt);

        // Initialize Firebase database reference and authentication
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        userPreferences = new UserPreferences(this);

        // Set click listeners
        loginButton.setOnClickListener(v -> loginUser());
        signUpPrompt.setOnClickListener(v -> navigateToSignUpPage());
        forgotPasswordPrompt.setOnClickListener(v -> navigateToForgotPasswordPage());
    }

    private void loginUser() {
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(mobileNumber, password)) {
            return;
        }

        // Check user in the database
        databaseReference.child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Snapshot exists: " + snapshot.exists());
                if (snapshot.exists()) {
                    // Fetch the user object directly
                    Users user = snapshot.getValue(Users.class);
                    Log.d(TAG, "User: " + user);  // Log the user object for debugging

                    if (user == null) {
                        Toast.makeText(LoginPage.this, "User data is null.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if the user is active
                    if (!user.isActive()) {

                        Toast.makeText(LoginPage.this, "This account has been deactivated.", Toast.LENGTH_SHORT).show();
                    }

                    // Check password
                    String dbPassword = snapshot.child("password").getValue(String.class);
                    Log.d(TAG, "dbPassword: " + dbPassword);  // Log the password for debug

                    // Verify password
                    if (dbPassword != null && dbPassword.equals(password)) {
                        String email = user.getEmail();  // Use the user object to get the email
                        if (email != null) {
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                                Toast.makeText(LoginPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                navigateToHomePage();
                                            } else {
                                                Toast.makeText(LoginPage.this, "Please verify your email.", Toast.LENGTH_LONG).show();
                                                firebaseAuth.signOut();
                                            }
                                        } else {
                                            Toast.makeText(LoginPage.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginPage.this, "Email not found in database.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginPage.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginPage.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginPage.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String mobileNumber, String password) {
        if (mobileNumber.isEmpty() || !Patterns.PHONE.matcher(mobileNumber).matches()) {
            mobileNumberEditText.setError("Valid mobile number is required");
            mobileNumberEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    private void navigateToHomePage() {
        Intent intent = new Intent(LoginPage.this, Maps.class);
        intent.putExtra("USER_MOBILE_NUMBER", mobileNumberEditText.getText().toString().trim());
        startActivity(intent);
        finish();
    }

    private void navigateToSignUpPage() {
        Intent intent = new Intent(LoginPage.this, SignUpPage.class);
        startActivity(intent);
    }

    private void navigateToForgotPasswordPage() {
        Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
        startActivity(intent);
    }
}
