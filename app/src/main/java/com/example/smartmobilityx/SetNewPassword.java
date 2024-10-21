package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SetNewPassword extends AppCompatActivity {

    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_password);

        // Initialize views
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPasswordEditText);
        saveButton = findViewById(R.id.saveButton);

        // Set up button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmNewPasswordEditText.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SetNewPassword.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (newPassword.equals(confirmPassword)) {
                    // Proceed with saving the password (add your password update logic here)

                    // Redirect to LoginPage
                    Intent intent = new Intent(SetNewPassword.this, LoginPage.class);
                    startActivity(intent);
                    finish(); // Optionally finish this activity so that it is removed from the back stack
                } else {
                    // Show error message
                    Toast.makeText(SetNewPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
