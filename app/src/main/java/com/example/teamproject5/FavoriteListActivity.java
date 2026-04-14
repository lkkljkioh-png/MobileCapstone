package com.example.teamproject5;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 즐겨찾기 자격증 목록 Activity
 *
 * - MY 탭의 즐겨찾기 화살표 버튼 클릭 시 진입
 * - 즐겨찾기된 자격증만 필터링해서 표시
 *
 * 백엔드 연동 시: loadFavoriteCertificates()에서 API 응답으로 교체
 */
public class FavoriteListActivity extends AppCompatActivity
        implements CertificateAdapter.OnCertificateActionListener {

    private RecyclerView recyclerView;
    private CertificateAdapter adapter;
    private List<Certificate> favoriteList;
    private CertificateRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        repository = new CertificateRepository(this);

        initViews();
        loadFavoriteCertificates();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewFavorites);

        if (recyclerView == null) {
            Toast.makeText(this, getString(R.string.error_recycler_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 전체 자격증 중 즐겨찾기된 항목만 필터링합니다.
     * 백엔드 연동 시 이 메서드를 수정하세요.
     */
    private void loadFavoriteCertificates() {
        List<Certificate> allList = repository.getCertificates();
        favoriteList = new ArrayList<>();

        for (Certificate cert : allList) {
            if (cert.isFavorite()) {
                favoriteList.add(cert);
            }
        }

        adapter = new CertificateAdapter(favoriteList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFavoriteClick(Certificate certificate, int position) {
        boolean newState = repository.toggleFavorite(certificate);

        if (newState) {
            // 즐겨찾기 추가 - 아이템 갱신
            adapter.updateItem(position);
            Toast.makeText(this, certificate.getName() + " " + getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
        } else {
            // 즐겨찾기 해제 - 목록에서 즉시 제거
            favoriteList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, certificate.getName() + " " + getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetailClick(Certificate certificate) {
        Toast.makeText(this, certificate.getName() + " 상세 페이지로 이동", Toast.LENGTH_SHORT).show();

    }
}
