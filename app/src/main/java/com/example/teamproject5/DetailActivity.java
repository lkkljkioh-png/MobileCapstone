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
 */

public class DetailActivity extends AppCompatActivity {

    private TextView tvCertTitle;
    private TextView tvCardTitle;
    private TextView tvBtnBack;
    private TextView tvBtnHeart;
    private TextView tvTabInfo;
    private TextView tvTabQuestion;
    private TextView tvTabMock;
    private TextView tvDescription;
    private TextView tvQualification;
    private TextView tvJobDescription;

    private String certName;
    private int certId;

    private CertificateRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        certName = getIntent().getStringExtra("cert_name");
        certId   = getIntent().getIntExtra("cert_id", -1);

        repository = new CertificateRepository(this);

        tvCertTitle       = findViewById(R.id.tv_cert_title);
        tvCardTitle   = findViewById(R.id.tv_card_title);
        tvBtnBack     = findViewById(R.id.tv_btn_back);
        tvBtnHeart    = findViewById(R.id.tv_btn_heart);

        tvTabInfo     = findViewById(R.id.tv_tab_info);
        tvTabQuestion = findViewById(R.id.tv_tab_question);
        tvTabMock     = findViewById(R.id.tv_tab_mock);

        tvDescription = findViewById(R.id.tv_description);
        tvQualification = findViewById(R.id.tv_qualification);
        tvJobDescription  = findViewById(R.id.tv_job_description);

        tvCertTitle.setText(certName);
        tvCardTitle.setText(certName);

        tvBtnBack.setOnClickListener(v -> finish());

        updateHeartUi();

        tvBtnHeart.setOnClickListener(v -> {
            if (certId == -1) return;
            repository.toggleFavoriteById(certId);
            updateHeartUi();
        });

        setTabUi(tvTabInfo);
        setInfoContent();

        tvTabInfo.setOnClickListener(v -> {
            setTabUi(tvTabInfo);
            setInfoContent();
        });

        tvTabQuestion.setOnClickListener(v -> {
            setTabUi(tvTabQuestion);
            setQuestionContent();
        });

        tvTabMock.setOnClickListener(v -> {
            setTabUi(tvTabMock);
            setMockContent();
        });
    }

    private void updateHeartUi() {
        if (certId == -1) {
            tvBtnHeart.setText("♡");
            return;
        }
        tvBtnHeart.setText(repository.isFavoriteById(certId) ? "♥" : "♡");
    }

    private void setTabUi(TextView selected) {
        tvTabInfo.setBackgroundColor(0x00000000);
        tvTabQuestion.setBackgroundColor(0x00000000);
        tvTabMock.setBackgroundColor(0x00000000);

        tvTabInfo.setTextColor(0xFF999999);
        tvTabQuestion.setTextColor(0xFF999999);
        tvTabMock.setTextColor(0xFF999999);

        selected.setBackgroundColor(0xFF3F51B5);
        selected.setTextColor(0xFFFFFFFF);
    }

    private void setInfoContent() {
        if (certId == -1) {
            tvDescription.setText(certName);
            tvQualification.setText("");
            tvJobDescription.setText("");
            return;
        }

        for (Certificate cert : repository.getCertificates()) {
            if (cert.getId() == certId) {
                tvDescription.setText(cert.getDescription());
                tvQualification.setText("백엔드 연동 후 표시");
                tvJobDescription.setText("백엔드 연동 후 표시");
                return;
            }
        }

        tvDescription.setText(certName);
        tvQualification.setText("");
        tvJobDescription.setText("");
    }

    private void setQuestionContent() {
        tvDescription.setText("기출문제 준비중");
        tvQualification.setText("");
        tvJobDescription.setText("");
    }

    private void setMockContent() {
        tvDescription.setText("모의고사 준비중");
        tvQualification.setText("");
        tvJobDescription.setText("");
    }
}
