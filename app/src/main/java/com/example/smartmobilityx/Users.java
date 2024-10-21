package com.example.smartmobilityx;

public class Users {
    private String phone;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive; // Keep this field
    private String password; // Add this field
    private String userName; // Add this field

    // Default constructor required for calls to DataSnapshot.getValue(Users.class)
    public Users() {
        // No-arg constructor for Firebase
    }

    // Parameterized constructor
    public Users(String phone, String firstName, String lastName, String email, String userName, String password) {
        this.phone = phone;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true; // Initialize as active
        this.password = password; // Initialize password
        this.userName = userName; // Initialize userName
    }

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = true;
    }

    public String getPassword() {
        return password; // Add this getter
    }

    public void setPassword(String password) {
        this.password = password; // Add this setter
    }

    public String getUserName() {
        return userName; // Add this getter
    }

    public void setUserName(String userName) {
        this.userName = userName; // Add this setter
    }
}
