package com.example.smartmobilityx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanLicense extends CaptureActivity {

    private Button captureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_license);

        captureBtn = findViewById(R.id.captureButton);

        // Button click redirects to ScanLicense first and then to ScannerActivity
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ScanLicense activity
                Intent scanIntent = new Intent(ScanLicense.this, ScanLicense.class);
                startActivity(scanIntent);

                // After ScanLicense, redirect to ScannerActivity
                Intent scannerIntent = new Intent(ScanLicense.this, ScannerActivity.class);
                startActivity(scannerIntent);
            }
        });
    }
}
