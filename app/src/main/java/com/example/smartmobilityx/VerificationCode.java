package com.example.smartmobilityx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationCode extends AppCompatActivity {

    private EditText codeBox1, codeBox2, codeBox3, codeBox4, codeBox5;
    private TextView resendCodeTextView;
    private Button confirmButton;
    private Handler handler;
    private Runnable runnable;
    private int timeLeftInSeconds = 60; // 60 seconds timer
    private boolean isTimerRunning = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verificationcode);

        codeBox1 = findViewById(R.id.codeBox1);
        codeBox2 = findViewById(R.id.codeBox2);
        codeBox3 = findViewById(R.id.codeBox3);
        codeBox4 = findViewById(R.id.codeBox4);
        codeBox5 = findViewById(R.id.codeBox5);
        resendCodeTextView = findViewById(R.id.resendCodeTextView);
        confirmButton = findViewById(R.id.confirmButton);

        handler = new Handler();

        setUpCodeBoxListeners();
        startResendTimer();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

        resendCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    resendCode();
                }
            }
        });
    }

    private void setUpCodeBoxListeners() {
        codeBox1.setOnKeyListener(new CodeBoxKeyListener(codeBox2));
        codeBox2.setOnKeyListener(new CodeBoxKeyListener(codeBox3));
        codeBox3.setOnKeyListener(new CodeBoxKeyListener(codeBox4));
        codeBox4.setOnKeyListener(new CodeBoxKeyListener(codeBox5));
    }

    private class CodeBoxKeyListener implements View.OnKeyListener {
        private EditText nextBox;

        public CodeBoxKeyListener(EditText nextBox) {
            this.nextBox = nextBox;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                // If key is delete, move focus to previous box
                if (v.getId() == R.id.codeBox2) codeBox1.requestFocus();
                else if (v.getId() == R.id.codeBox3) codeBox2.requestFocus();
                else if (v.getId() == R.id.codeBox4) codeBox3.requestFocus();
                else if (v.getId() == R.id.codeBox5) codeBox4.requestFocus();
            } else if (v.getId() != R.id.codeBox5 && event.getAction() == KeyEvent.ACTION_DOWN) {
                // If a key is pressed, move focus to next box
                nextBox.requestFocus();
            }
            return false;
        }
    }

    private void verifyCode() {
        String code = codeBox1.getText().toString() + codeBox2.getText().toString() +
                codeBox3.getText().toString() + codeBox4.getText().toString() +
                codeBox5.getText().toString();

        if (code.length() == 5) {
            // Handle verification code validation here
            Toast.makeText(this, "Code entered: " + code, Toast.LENGTH_SHORT).show();
            // Navigate to next activity or handle successful verification
            navigateToNextPage();
        } else {
            Toast.makeText(this, "Please enter all digits of the code", Toast.LENGTH_SHORT).show();
        }
    }

    private void startResendTimer() {
        isTimerRunning = true;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (timeLeftInSeconds > 0) {
                    resendCodeTextView.setText("Resend Code in " + timeLeftInSeconds + "s");
                    timeLeftInSeconds--;
                    handler.postDelayed(this, 1000);
                } else {
                    resendCodeTextView.setText("Resend Code");
                    isTimerRunning = false;
                }
            }
        };
        handler.post(runnable);
    }

    private void resendCode() {
        // Handle resend code logic here
        Toast.makeText(this, "Verification code resent", Toast.LENGTH_SHORT).show();
        // Restart the timer after resending the code
        timeLeftInSeconds = 60;
        startResendTimer();
    }

    private void navigateToNextPage() {
        Intent intent = new Intent(this, SetNewPassword.class);
        startActivity(intent);
        finish();
    }
}