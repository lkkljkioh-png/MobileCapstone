package com.example.teamproject5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 캘린더 탭 Activity
 * CalendarFragment 를 호스팅
 *
 * 사용법:
 *   Intent intent = new Intent(this, CalendarActivity.class);
 *   startActivity(intent);
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // CalendarFragment 붙이기
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_calendar, new CalendarFragment())
                    .commit();
        }
    }
}
