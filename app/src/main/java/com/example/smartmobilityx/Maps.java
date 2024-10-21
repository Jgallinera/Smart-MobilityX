package com.example.smartmobilityx;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Maps extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private DrawerLayout drawerLayout;
    private TextView userAccountTextView;
    private TextView homeButton;
    private TextView paymentButton;
    private TextView rideHistoryButton;
    private TextView logoutButton;
    private ImageButton hamburgerMenuButton;
    private GoogleMap googleMap;
    private boolean isPermissionGranted;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng currentLatLng; // Track the user's current location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        userAccountTextView = findViewById(R.id.userAccountTextView);
        homeButton = findViewById(R.id.homeButton);
        paymentButton = findViewById(R.id.paymentButton);
        rideHistoryButton = findViewById(R.id.rideHistoryButton);
        logoutButton = findViewById(R.id.logoutButton);
        hamburgerMenuButton = findViewById(R.id.hamburgerMenuButton);

        userAccountTextView.setOnClickListener(v -> navigateTo(UserProfile.class));
        homeButton.setOnClickListener(v -> navigateTo(SelectVehicle.class));
        paymentButton.setOnClickListener(v -> navigateTo(Payment.class));
        rideHistoryButton.setOnClickListener(v -> navigateTo(RideHistory.class));
        logoutButton.setOnClickListener(v -> navigateTo(LandingPage.class));

        hamburgerMenuButton.setOnClickListener(v -> openDrawer());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();

        if (isPermissionGranted) {
            if (checkGooglePlayServices()) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.maps);
                if (supportMapFragment != null) {
                    supportMapFragment.getMapAsync(this);
                }
            } else {
                Toast.makeText(this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            googleApiAvailability.getErrorDialog(this, result, 201, dialogInterface ->
                    Toast.makeText(Maps.this, "User Cancelled Dialog", Toast.LENGTH_SHORT).show()).show();
        }
        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        isPermissionGranted = true;
                        Toast.makeText(Maps.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        if (checkGooglePlayServices()) {
                            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.maps);
                            if (supportMapFragment != null) {
                                supportMapFragment.getMapAsync(Maps.this);
                            }
                        } else {
                            Toast.makeText(Maps.this, "Google Play Services Not Available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng center = new LatLng(14.558520, 121.017905);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, 15);
        googleMap.animateCamera(cameraUpdate);
        googleMap.setOnInfoWindowClickListener(this);

        // Markers
        LatLng kamagong = new LatLng(14.565659, 121.012591);
        MarkerOptions marker1 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Kamagong Parking")
                .position(kamagong);
        googleMap.addMarker(marker1);

        LatLng malugay = new LatLng(14.562648, 121.016046);
        MarkerOptions marker2 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Malugay Parking")
                .position(malugay);
        googleMap.addMarker(marker2);

        LatLng amorsolo = new LatLng(14.559886, 121.015939);
        MarkerOptions marker3 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Amorsolo Parking")
                .position(amorsolo);
        googleMap.addMarker(marker3);

        LatLng medplaza = new LatLng(14.558474, 121.014265);
        MarkerOptions marker4 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Medical Plaza Parking")
                .position(medplaza);
        googleMap.addMarker(marker4);

        LatLng waltermart = new LatLng(14.551931, 121.014136);
        MarkerOptions marker5 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Waltermart Parking")
                .position(waltermart);
        googleMap.addMarker(marker5);

        LatLng aguirre = new LatLng(14.551931, 121.018750);
        MarkerOptions marker6 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Aguirre Parking")
                .position(aguirre);
        googleMap.addMarker(marker6);

        LatLng ayala = new LatLng(14.554299, 121.024543);
        MarkerOptions marker7 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Ayala Parking")
                .position(ayala);
        googleMap.addMarker(marker7);

        LatLng belair = new LatLng(14.555421, 121.024136);
        MarkerOptions marker8 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Bel Air Parking")
                .position(belair);
        googleMap.addMarker(marker8);

        LatLng towerone = new LatLng(14.556833, 121.022097);
        MarkerOptions marker9 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Tower One Parking")
                .position(towerone);
        googleMap.addMarker(marker9);

        LatLng leviste = new LatLng(14.560052, 121.021711);
        MarkerOptions marker10 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Leviste Rental Station")
                .position(leviste);
        googleMap.addMarker(marker10);

        LatLng ayalagardens = new LatLng(14.557643, 121.025466);
        MarkerOptions marker11 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Ayala Gardens Rental Station")
                .position(ayalagardens);
        googleMap.addMarker(marker11);

        LatLng highstreet = new LatLng(14.552451, 121.021325);
        MarkerOptions marker12 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("High Street Rental Station")
                .position(highstreet);
        googleMap.addMarker(marker12);

        LatLng bonifacio = new LatLng(14.554322, 121.017057);
        MarkerOptions marker13 = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Bonifacio Rental Station")
                .position(bonifacio);
        googleMap.addMarker(marker13);

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.addMarker(new MarkerOptions()
                                    .position(currentLatLng)
                                    .title("You are here")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            CameraUpdate cameraUpdateCurrent = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                            googleMap.animateCamera(cameraUpdateCurrent);
                        } else {
                            Toast.makeText(Maps.this, "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(Maps.this, "Error retrieving location: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(this, "Information Window is clicked", Toast.LENGTH_LONG).show();

        new AlertDialog.Builder(this)
                .setTitle("Smart MobilityX")
                .setMessage("Get Directions?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (currentLatLng != null) {
                        LatLng destinationLatLng = marker.getPosition();
                        String navigationUrl = "https://www.google.com/maps/dir/?api=1&origin="
                                + currentLatLng.latitude + "," + currentLatLng.longitude
                                + "&destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude
                                + "&travelmode=driving"; // You can also use walking, bicycling, etc.
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigationUrl));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    } else {
                        Toast.makeText(Maps.this, "Current location not available", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void navigateTo(Class<?> destinationClass) {
        Intent intent = new Intent(Maps.this, destinationClass);
        startActivity(intent);
    }
}
