package com.example.capstone_2_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    // 상단
    TextView title, cardTitle, backBtn, heartBtn;

    // 탭
    TextView infoTab, qTab, mockTab;

    // 내용
    TextView descText, qualText, jobText;

    String certName;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 🔥 값 받기
        certName = getIntent().getStringExtra("cert_name");

        // 🔥 연결
        title = findViewById(R.id.top_title);
        cardTitle = findViewById(R.id.card_title);
        backBtn = findViewById(R.id.back_btn);
        heartBtn = findViewById(R.id.heart_btn);

        infoTab = findViewById(R.id.info_tab);
        qTab = findViewById(R.id.q_tab);
        mockTab = findViewById(R.id.mock_tab);

        descText = findViewById(R.id.desc_text);
        qualText = findViewById(R.id.qual_text);
        jobText = findViewById(R.id.job_text);

        // 🔥 SharedPreferences
        prefs = getSharedPreferences("favorite", MODE_PRIVATE);

        // 🔥 제목 설정
        title.setText(certName);
        cardTitle.setText(certName);

        // 🔥 뒤로가기
        backBtn.setOnClickListener(v -> finish());

        // 🔥 하트 상태 불러오기
        boolean isLiked = prefs.getBoolean(certName, false);
        if (isLiked) {
            heartBtn.setText("♥");
        } else {
            heartBtn.setText("♡");
        }

        // 🔥 하트 클릭
        heartBtn.setOnClickListener(v -> {
            boolean current = prefs.getBoolean(certName, false);

            SharedPreferences.Editor editor = prefs.edit();

            if (current) {
                heartBtn.setText("♡");
                editor.putBoolean(certName, false);
            } else {
                heartBtn.setText("♥");
                editor.putBoolean(certName, true);
            }

            editor.apply();
        });

        // 🔥 기본 (정보 탭)
        setTabUI(infoTab);
        setInfoContent();

        // 🔥 탭 클릭
        infoTab.setOnClickListener(v -> {
            setTabUI(infoTab);
            setInfoContent();
        });

        qTab.setOnClickListener(v -> {
            setTabUI(qTab);
            setQuestionContent();
        });

        mockTab.setOnClickListener(v -> {
            setTabUI(mockTab);
            setMockContent();
        });
    }

    // 🔥 탭 디자인
    void setTabUI(TextView selected) {
        infoTab.setBackgroundColor(0x00000000);
        qTab.setBackgroundColor(0x00000000);
        mockTab.setBackgroundColor(0x00000000);

        infoTab.setTextColor(0xFF999999);
        qTab.setTextColor(0xFF999999);
        mockTab.setTextColor(0xFF999999);

        selected.setBackgroundColor(0xFF3F51B5);
        selected.setTextColor(0xFFFFFFFF);
    }

    // 🔥 정보
    void setInfoContent() {
        if ("ADsP".equals(certName)) {
            descText.setText("데이터 분석 기초 자격증");
            qualText.setText("제한 없음");
            jobText.setText("데이터 분석가");
        } else if ("정보처리기사".equals(certName)) {
            descText.setText("IT 기본 자격증");
            qualText.setText("관련 학과 필요");
            jobText.setText("개발자");
        } else {
            descText.setText("엑셀 자격증");
            qualText.setText("제한 없음");
            jobText.setText("사무직");
        }
    }

    // 🔥 기출문제
    void setQuestionContent() {
        descText.setText("기출문제 준비중");
        qualText.setText("");
        jobText.setText("");
    }

    // 🔥 모의고사
    void setMockContent() {
        descText.setText("모의고사 준비중");
        qualText.setText("");
        jobText.setText("");
    }
}