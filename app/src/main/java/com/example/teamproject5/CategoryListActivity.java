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
    private List<Certificate> categoryList;
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
        List<Certificate> allList = repository.getCertificates();
        categoryList = new ArrayList<>();

        if (categoryId != null) {
            for (Certificate cert : allList) {
                if (categoryId.equals(cert.getCategory())) {
                    categoryList.add(cert);
                }
            }
        }

        adapter = new CertificateAdapter(categoryList, this);
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
        Toast.makeText(this, certificate.getName() + " 상세 페이지로 이동", Toast.LENGTH_SHORT).show();

        // 상세 화면 구현 후 아래 주석 해제
        // Intent intent = new Intent(this, CertificateDetailActivity.class);
        // intent.putExtra("certificate_id", certificate.getId());
        // startActivity(intent);
    }
}
