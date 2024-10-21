package com.example.smartmobilityx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SignUpPage extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private UserPreferences userPreferences;
    private String licenseImageUrl = null;
    private Bitmap licenseImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup_activity);

        final EditText firstNameEditText = findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = findViewById(R.id.lastNameEditText);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        final Button uploadLicenseButton = findViewById(R.id.uploadLicenseButton);
        final Button signUpButton = findViewById(R.id.signUpButton);
        final TextView signInPrompt = findViewById(R.id.signInPrompt);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("Licenses");
        userPreferences = new UserPreferences(this);

        // Open camera when uploadLicenseButton is clicked
        uploadLicenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameTxt = firstNameEditText.getText().toString();
                final String lastNameTxt = lastNameEditText.getText().toString();
                final String emailTxt = emailEditText.getText().toString();
                final String phoneTxt = phoneNumberEditText.getText().toString();
                final String passwordTxt = passwordEditText.getText().toString();
                final String confirmPasswordTxt = confirmPasswordEditText.getText().toString();

                if (firstNameTxt.isEmpty() || lastNameTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(SignUpPage.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(SignUpPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user != null) {
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpPage.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();

                                                        // Upload license image to Firebase Storage if it's not null
                                                        if (licenseImageBitmap != null) {
                                                            uploadLicenseImage(phoneTxt, firstNameTxt, lastNameTxt, emailTxt, passwordTxt);
                                                        } else {
                                                            saveUserData(phoneTxt, firstNameTxt, lastNameTxt, emailTxt, passwordTxt, null);
                                                        }
                                                    } else {
                                                        Toast.makeText(SignUpPage.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(SignUpPage.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        signInPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            licenseImageBitmap = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, "License image captured", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadLicenseImage(final String phoneTxt, final String firstNameTxt, final String lastNameTxt, final String emailTxt, final String passwordTxt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        licenseImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        final StorageReference licenseRef = storageReference.child(phoneTxt + "_license.jpg");

        licenseRef.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                licenseRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        licenseImageUrl = uri.toString();
                        saveUserData(phoneTxt, firstNameTxt, lastNameTxt, emailTxt, passwordTxt, licenseImageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpPage.this, "License upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(final String phoneTxt, final String firstNameTxt, final String lastNameTxt, final String emailTxt, final String passwordTxt, String licenseUrl) {
        databaseReference.child(phoneTxt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpPage.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Save user data to the database
                    databaseReference.child(phoneTxt).child("firstName").setValue(firstNameTxt);
                    databaseReference.child(phoneTxt).child("lastName").setValue(lastNameTxt);
                    databaseReference.child(phoneTxt).child("email").setValue(emailTxt);
                    databaseReference.child(phoneTxt).child("password").setValue(passwordTxt);

                    // Set isActive to true for new users
                    databaseReference.child(phoneTxt).child("isActive").setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("SignUpPage", "User activated: " + phoneTxt);
                        } else {
                            Log.d("SignUpPage", "Failed to set isActive: " + task.getException().getMessage());
                        }
                    });

                    if (licenseUrl != null) {
                        databaseReference.child(phoneTxt).child("licenseImageUrl").setValue(licenseUrl);
                    }

                    // Save user data to SharedPreferences
                    userPreferences.saveUserData("", firstNameTxt, lastNameTxt, emailTxt, phoneTxt, "");
                    Toast.makeText(SignUpPage.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpPage.this, LoginPage.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpPage.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
