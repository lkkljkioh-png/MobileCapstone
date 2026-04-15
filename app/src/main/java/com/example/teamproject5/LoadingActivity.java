package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 스플래시 Activity
 *
 * 역할:
 *   - 앱 시작 시 2초간 로딩 화면 표시
 *   - 로그인 유지 여부 확인 후 분기
 *       로그인 상태 (is_logged_in = true) → MainActivity
 *       미로그인 상태                      → LoginActivity
 *
 * 로그인 상태 저장 위치:
 *   SharedPreferences("user_prefs") → "is_logged_in" (boolean)
 *   LoginActivity 로그인 성공 시 true로 저장
 *
 * 백엔드 연동 포인트:
 *   - 토큰 유효성 검사 API 호출로 교체
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // 로그인 유지 여부 확인
            boolean isLoggedIn = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .getBoolean("is_logged_in", false);

            Intent intent;
            if (isLoggedIn) {
                // 로그인 상태 → 홈으로 바로 이동
                intent = new Intent(LoadingActivity.this, MainActivity.class);
            } else {
                // 미로그인 상태 → 로그인 화면으로 이동
                intent = new Intent(LoadingActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}