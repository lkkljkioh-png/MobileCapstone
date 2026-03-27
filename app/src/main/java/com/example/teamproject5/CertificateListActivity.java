package com.example.teamproject5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CertificateListActivity extends AppCompatActivity
        implements CertificateAdapter.OnCertificateActionListener {

    private RecyclerView recyclerView;
    private CertificateAdapter adapter;
    private List<Certificate> certificateList;
    private CertificateRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_list);

        repository = new CertificateRepository();

        initViews();
        loadCertificates();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewCertificates);

        if (recyclerView == null) {
            Toast.makeText(this, "RecyclerView를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                ? certificate.getName() + " 즐겨찾기 추가"
                : certificate.getName() + " 즐겨찾기 해제";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailClick(Certificate certificate) {
        Toast.makeText(this, certificate.getName() + " 상세 페이지로 이동", Toast.LENGTH_SHORT).show();

        // 나중에 상세 화면 만들면 id 전달
        // Intent intent = new Intent(this, CertificateDetailActivity.class);
        // intent.putExtra("certificate_id", certificate.getId());
        // startActivity(intent);
    }
}