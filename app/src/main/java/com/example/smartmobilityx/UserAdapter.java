package com.example.smartmobilityx;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Users> userList;
    private Context context;

    public UserAdapter(List<Users> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Define the PasswordCallback interface
    public interface PasswordCallback {
        void onPasswordEntered(String password);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView phoneTextView, firstNameTextView, lastNameTextView, emailTextView, userNameTextView;
        Button activateUserButton, deactivateUserButton;
        LinearLayout cardLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            firstNameTextView = itemView.findViewById(R.id.firstNameTextView);
            lastNameTextView = itemView.findViewById(R.id.lastNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            activateUserButton = itemView.findViewById(R.id.activateUserButton);
            deactivateUserButton = itemView.findViewById(R.id.deactivateUserButton);
            cardLayout = itemView.findViewById(R.id.cardLayout);

            // Set click listeners for both buttons
            deactivateUserButton.setOnClickListener(v -> handleDeactivateUser());
            activateUserButton.setOnClickListener(v -> handleActivateUser());
        }

        private void handleDeactivateUser() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Users user = userList.get(position);
                if (isPhoneNumberValid(user.getPhone())) {
                    // Set the user status to inactive
                    user.isActive();
                    // Update the UI immediately after deactivating
                    bind(user); // Refresh the UI for this item
                    updateUserStatus(user); // Update only isActive in the database
                }
            }
        }

        private void handleActivateUser() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Users user = userList.get(position);
                if (isPhoneNumberValid(user.getPhone())) {
                    promptForPassword(password -> {
                        activateUser(user.getPhone(), password);
                        user.setActive(true); // Set the user status to active
                        bind(user); // Update UI immediately
                        updateUserStatus(user); // Update status in the database
                    });
                }
            }
        }

        private boolean isPhoneNumberValid(String phoneNumber) {
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                Toast.makeText(context, "User phone number is invalid", Toast.LENGTH_SHORT).show();
                Log.e("UserAdapter", "Invalid phone number: " + phoneNumber);
                return false;
            }
            return true;
        }

        private void activateUser(String phone, String password) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(phone);
            userRef.child("isActive").setValue(true);
            userRef.child("password").setValue(password); // Ensure you set the password here
            Toast.makeText(context, "User activated successfully", Toast.LENGTH_SHORT).show();
        }

        public void bind(Users user) {
            phoneTextView.setText(user.getPhone());
            firstNameTextView.setText(user.getFirstName());
            lastNameTextView.setText(user.getLastName());
            emailTextView.setText(user.getEmail());
            userNameTextView.setText(user.getUserName());

            // Update UI based on user's active status
            if (user.isActive()) {
                activateUserButton.setVisibility(View.GONE);
                deactivateUserButton.setVisibility(View.VISIBLE);
                cardLayout.setBackgroundColor(context.getResources().getColor(R.color.active_user_color)); // Set active color
            } else {
                deactivateUserButton.setVisibility(View.GONE);
                activateUserButton.setVisibility(View.VISIBLE);
                cardLayout.setBackgroundColor(context.getResources().getColor(R.color.deactivated_user_color)); // Set deactivated color
            }
        }

        private void updateUserStatus(Users user) {
            try {
                String phoneNumber = user.getPhone();
                if (isPhoneNumberValid(phoneNumber)) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("isActive", user.isActive()); // Update to the new active status
                    userRef.updateChildren(updates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "User status updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to update user status", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("UserAdapter", "Error updating user status", e);
                    });
                }
            } catch (Exception e) {
                Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UserAdapter", "Exception occurred", e);
            }
        }

        private void promptForPassword(PasswordCallback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Password");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String password = input.getText().toString();
                callback.onPasswordEntered(password); // Pass the entered password to the callback
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        }
    }
}
