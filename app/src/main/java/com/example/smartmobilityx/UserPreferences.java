package com.example.smartmobilityx;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserData(String username, String firstName, String lastName, String email, String mobileNumber, String s) {
        editor.putString("username", username);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("mobileNumber", mobileNumber);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    public String getFirstName() {
        return sharedPreferences.getString("firstName", null);
    }

    public String getLastName() {
        return sharedPreferences.getString("lastName", null);
    }

    public String getEmail() {
        return sharedPreferences.getString("email", null);
    }

    public String getMobileNumber() {
        return sharedPreferences.getString("mobileNumber", null);
    }

    // New methods
    public void setUsername(String username) {
        editor.putString("username", username);
        editor.apply();
    }

    public void setFirstName(String firstName) {
        editor.putString("firstName", firstName);
        editor.apply();
    }

    public void setLastName(String lastName) {
        editor.putString("lastName", lastName);
        editor.apply();
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString("mobileNumber", mobileNumber);
        editor.apply();
    }

    public String getUserId() {
        // Assume userId is stored in SharedPreferences
        return sharedPreferences.getString("userId", null);
    }

    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.apply();
    }
}
