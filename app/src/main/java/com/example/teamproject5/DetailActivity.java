package com.example.teamproject5;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 자격증 상세 정보 Activity
 *
 * 역할:
 *   - 자격증 이름·설명·응시자격·관련직무를 탭 형태로 표시
 *   - 즐겨찾기 토글 (하트 버튼) — CertificateRepository를 통해 앱 전체와 동기화
 *   - 탭 3개: 정보 / 기출문제 / 모의고사 (기출·모의고사는 백엔드 연동 후 구현 예정)
 *
 * Intent로 전달받는 값:
 *   - "cert_name" (String) : 자격증 이름
 *   - "cert_id"   (int)    : 자격증 ID — 즐겨찾기 및 상세 데이터 조회용 (-1이면 미지정)
 *
 * 호출 방법:
 *   Intent intent = new Intent(this, DetailActivity.class);
 *   intent.putExtra("cert_name", certificate.getName());
 *   intent.putExtra("cert_id",   certificate.getId());
 *   startActivity(intent);  // 또는 detailLauncher.launch(intent)
 *
 * 즐겨찾기 저장 방식:
 *   CertificateRepository.toggleFavoriteById() / isFavoriteById() 를 통해
 *   SharedPreferences("favorite_prefs")에 "fav_{id}" 키로 저장
 *   → FavoriteListActivity / CertificateListActivity 등 모든 화면과 동일한 저장소 공유
 *
 * 백엔드 연동 포인트:
 *   - setInfoContent() : 응시자격·관련직무 필드를 API 응답으로 교체
 *   - setQuestionContent() : 기출문제 데이터 API 연동
 *   - setMockContent()     : 모의고사 데이터 API 연동
 */

public class DetailActivity extends AppCompatActivity {

    TextView title, cardTitle, backBtn, heartBtn;
    TextView infoTab, qTab, mockTab;
    TextView descText, qualText, jobText;

    String certName;
    int certId;

    CertificateRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        certName = getIntent().getStringExtra("cert_name");
        certId   = getIntent().getIntExtra("cert_id", -1);

        repository = new CertificateRepository(this);

        title     = findViewById(R.id.top_title);
        cardTitle = findViewById(R.id.card_title);
        backBtn   = findViewById(R.id.back_btn);
        heartBtn  = findViewById(R.id.heart_btn);

        infoTab  = findViewById(R.id.info_tab);
        qTab     = findViewById(R.id.q_tab);
        mockTab  = findViewById(R.id.mock_tab);

        descText = findViewById(R.id.desc_text);
        qualText = findViewById(R.id.qual_text);
        jobText  = findViewById(R.id.job_text);

        title.setText(certName);
        cardTitle.setText(certName);

        backBtn.setOnClickListener(v -> finish());

        updateHeartUI();

        heartBtn.setOnClickListener(v -> {
            if (certId == -1) return;
            repository.toggleFavoriteById(certId);
            updateHeartUI();
        });

        setTabUI(infoTab);
        setInfoContent();

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

    private void updateHeartUI() {
        if (certId == -1) {
            heartBtn.setText("♡");
            return;
        }
        // certId로 직접 조회, 객체 생성 없음
        heartBtn.setText(repository.isFavoriteById(certId) ? "♥" : "♡");
    }

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

    void setInfoContent() {
        if (certId == -1) {
            descText.setText(certName);
            qualText.setText("");
            jobText.setText("");
            return;
        }

        for (Certificate cert : repository.getCertificates()) {
            if (cert.getId() == certId) {
                descText.setText(cert.getDescription());
                qualText.setText("백엔드 연동 후 표시");
                jobText.setText("백엔드 연동 후 표시");
                return;
            }
        }

        descText.setText(certName);
        qualText.setText("");
        jobText.setText("");
    }

    void setQuestionContent() {
        descText.setText("기출문제 준비중");
        qualText.setText("");
        jobText.setText("");
    }

    void setMockContent() {
        descText.setText("모의고사 준비중");
        qualText.setText("");
        jobText.setText("");
    }
}