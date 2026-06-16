package com.example.teamproject5.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject5.R;
import com.example.teamproject5.database.entity.CertificateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리별 자격증 목록 Activity
 *
 * - 홈 화면 카테고리 버튼 클릭 시 진입
 * - Intent로 전달받은 category_id에 해당하는 자격증만 표시
 * - UI는 CertificateListActivity와 동일
 *
 * 사용법 (MainActivity에서 호출):
 *   Intent intent = new Intent(this, CategoryListActivity.class);
 *   intent.putExtra(EXTRA_CATEGORY_ID, "category_tech");
 *   startActivity(intent);
 *
 * 백엔드 연동 시: loadCertificatesByCategory()에서 API 호출로 교체
 */
public class CategoryListActivity extends AppCompatActivity
        implements CertificateAdapter.OnCertificateActionListener {

    /** Intent extra 키 - 카테고리 ID 전달용 */
    public static final String EXTRA_CATEGORY_ID = "category_id";

    private RecyclerView recyclerView;
    private CertificateAdapter adapter;
    private List<CertificateEntity> categoryList;
    private CertificateRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        repository = new CertificateRepository(this);

        // Intent에서 카테고리 ID 수신
        String categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);

        initViews(categoryId);
        loadCertificatesByCategory(categoryId);
    }

    private void initViews(String categoryId) {
        recyclerView = findViewById(R.id.recyclerViewCategory);

        if (recyclerView == null) {
            Toast.makeText(this, getString(R.string.error_recycler_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 뒤로가기 버튼
        android.widget.TextView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // 상단 타이틀을 카테고리 이름으로 설정
        android.widget.TextView tvTitle = findViewById(R.id.tv_category_list_title);
        if (tvTitle != null && categoryId != null) {
            tvTitle.setText(getCategoryLabel(categoryId));
        }
    }

    /**
     * category_id에 해당하는 자격증만 필터링합니다.
     * 백엔드 연동 시 이 메서드를 수정하세요.
     */
    private void loadCertificatesByCategory(String categoryId) {
        List<CertificateEntity> allList = repository.getCertificates();
        categoryList = new ArrayList<>();

        if (categoryId != null) {

            List<String> dbCategories =
                    CategoryUtils.getDbCategories(categoryId);

            for (CertificateEntity cert : allList) {
                if (dbCategories.contains(cert.category)) {
                    categoryList.add(cert);
                }
            }
        }

        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", 0);

        adapter = new CertificateAdapter(this, categoryList, this, userId);
        recyclerView.setAdapter(adapter);
    }

    /**
     * category_id → 화면에 표시할 한글 이름 변환
     * strings.xml의 카테고리 문자열과 동기화되어 있습니다.
     */
    private String getCategoryLabel(String categoryId) {
        switch (categoryId) {
            case "category_tech":     return getString(R.string.category_tech);
            case "category_industry": return getString(R.string.category_industry);
            case "category_business": return getString(R.string.category_business);
            case "category_primary":  return getString(R.string.category_primary);
            case "category_creative": return getString(R.string.category_creative);
            case "category_service":  return getString(R.string.category_service);
            default:                  return getString(R.string.category_title);
        }
    }

    @Override
    public void onFavoriteClick(CertificateEntity certificate, int position) {
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id",0);
        boolean newState = repository.toggleFavorite(userId, certificate.certId);
        adapter.updateItem(position);

        String message = newState
                ? certificate.certName + " " + getString(R.string.favorite_added)
                : certificate.certName + " " + getString(R.string.favorite_removed);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailClick(CertificateEntity certificate) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("cert_name", certificate.certName);
        intent.putExtra("cert_id", certificate.certId);
        startActivity(intent);
    }
}
