package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 전체 자격증 목록 Activity
 *
 * 역할:
 *   - CertificateRepository에서 전체 자격증 리스트를 불러와 RecyclerView로 표시
 *   - 즐겨찾기 버튼 클릭 시 상태 토글 및 아이콘 즉시 갱신
 *   - 상세보기 버튼 클릭 시 DetailActivity로 이동
 *   - DetailActivity에서 즐겨찾기 변경 후 돌아오면 리스트 아이콘 자동 갱신
 *
 * 진입 경로:
 *   - MainActivity 홈 화면 "전체 자격증" 버튼
 *   - 사이드 메뉴 "목록" 항목
 *
 * 즐겨찾기 갱신 방식:
 *   - 리스트 내 버튼 클릭 → onFavoriteClick() → repository.toggleFavorite() → updateItem()으로 해당 아이템만 갱신
 *   - DetailActivity 다녀온 후 → detailLauncher 콜백 → notifyDataSetChanged()로 전체 갱신
 *
 * 백엔드 연동 포인트:
 *   - loadCertificates() : DummyData 대신 API 응답으로 교체
 *   - onFavoriteClick()  : toggleFavorite() 내부에 서버 즐겨찾기 API 호출 추가
 */

public class CertificateListActivity extends AppCompatActivity
        implements CertificateAdapter.OnCertificateActionListener {

    private RecyclerView recyclerView;
    private CertificateAdapter adapter;
    private List<Certificate> certificateList;
    private CertificateRepository repository;

    // DetailActivity에서 돌아올 때 결과를 받기 위한 런처
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        // DetailActivity에서 돌아오면 즐겨찾기 아이콘 갱신
                        adapter.notifyDataSetChanged();
                    }
            );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_list);

        repository = new CertificateRepository(this);

        initViews();
        loadCertificates();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewCertificates);

        if (recyclerView == null) {
            Toast.makeText(this, getString(R.string.error_recycler_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 뒤로가기 버튼
        android.widget.TextView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void loadCertificates() {
        certificateList = repository.getCertificates();
        adapter = new CertificateAdapter(certificateList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFavoriteClick(Certificate certificate, int position) {
        boolean newState = repository.toggleFavorite(certificate);
        adapter.updateItem(position);

        String message = newState
                ? certificate.getName() + " " + getString(R.string.favorite_added)
                : certificate.getName() + " " + getString(R.string.favorite_removed);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailClick(Certificate certificate) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("cert_name", certificate.getName());
        intent.putExtra("cert_id", certificate.getId());

        // startActivity() 대신 detailLauncher로 실행 → 돌아올 때 아이콘 자동 갱신
        detailLauncher.launch(intent);
    }
}