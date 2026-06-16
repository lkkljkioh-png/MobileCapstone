package com.example.teamproject5.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject5.R;
import com.example.teamproject5.database.entity.CertificateEntity;

import java.util.List;

/**
 * 즐겨찾기 자격증 목록 Activity
 *
 * 역할:
 *   - 전체 자격증 중 즐겨찾기된 항목만 필터링해서 RecyclerView로 표시
 *   - 즐겨찾기 버튼 클릭 시 해제되면 해당 아이템을 목록에서 즉시 제거
 *   - 상세보기 버튼 클릭 시 DetailActivity로 이동
 *   - DetailActivity에서 즐겨찾기 변경 후 돌아오면 목록 전체 재필터링
 *
 * 즐겨찾기 갱신 방식:
 *   - 리스트 내 버튼 클릭 → onFavoriteClick()
 *       추가 시 : updateItem()으로 해당 아이템만 갱신
 *       해제 시 : favoriteList에서 제거 → notifyItemRemoved()로 즉시 사라짐
 *   - DetailActivity 다녀온 후 → detailLauncher 콜백 → refreshFavoriteList()로 전체 재필터링
 *
 * 백엔드 연동 포인트:
 *   - loadFavoriteCertificates() : DummyData 대신 서버 즐겨찾기 목록 API로 교체
 *   - onFavoriteClick()          : toggleFavorite() 내부에 서버 즐겨찾기 API 호출 추가
 */

public class FavoriteListActivity extends AppCompatActivity
        implements CertificateAdapter.OnCertificateActionListener {

    private RecyclerView recyclerView;
    private CertificateAdapter adapter;
    private List<CertificateEntity> favoriteList;
    private CertificateRepository repository;
    private int userId;

    // DetailActivity에서 돌아올 때 결과를 받기 위한 런처
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        // DetailActivity에서 돌아오면 무조건 리스트 갱신
                        refreshFavoriteList();
                    }
            );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        repository = new CertificateRepository(this);

        userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", 0);

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
        // 뒤로가기 버튼
        android.widget.TextView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void loadFavoriteCertificates() {

        favoriteList = repository.getFavoriteCertificates(userId);

        adapter = new CertificateAdapter(this, favoriteList, this, userId
        );

        recyclerView.setAdapter(adapter);
    }

    private void refreshFavoriteList() {
        favoriteList.clear();
        favoriteList.addAll(repository.getFavoriteCertificates(userId));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFavoriteClick(CertificateEntity certificate, int position) {
        boolean newState = repository.toggleFavorite(userId, certificate.certId);

        if (newState) {
            // 즐겨찾기 추가 - 아이템 갱신
            adapter.updateItem(position);
            Toast.makeText(this, certificate.certName + " " + getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
        } else {
            // 즐겨찾기 해제 - 목록에서 즉시 제거 (방어 코드 추가)
            if (position >= 0 && position < favoriteList.size()) {
                favoriteList.remove(position);
                adapter.notifyItemRemoved(position);
            }
            Toast.makeText(this, certificate.certName + " " + getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetailClick(CertificateEntity certificate) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("cert_name", certificate.certName);
        intent.putExtra("cert_id", certificate.certId);
        // startActivity() 대신 detailLauncher로 실행 → 돌아올 때 리스트 자동 갱신
        detailLauncher.launch(intent);
    }
}