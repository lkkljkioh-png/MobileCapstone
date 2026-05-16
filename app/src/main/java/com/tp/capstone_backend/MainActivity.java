package com.tp.capstone_backend;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Thread(() -> {
            try {
                com.tp.licenseList.LicenseDatabase db = com.tp.licenseList.LicenseDatabase.getInstance(this);
                java.util.List<com.tp.licenseList.License> list = db.licenseDao().getAll();
                //프론트엔드 화면 출력 코드
            } catch (Exception e) {
                runOnUiThread(() ->
                        android.widget.Toast.makeText(MainActivity.this, "데이터를 불러오지 못했습니다.", android.widget.Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}