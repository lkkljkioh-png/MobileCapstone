package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000); // 2000ms = 2초
    }
}