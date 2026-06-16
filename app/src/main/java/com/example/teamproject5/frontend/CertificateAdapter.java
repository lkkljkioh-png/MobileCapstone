package com.example.teamproject5.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject5.R;
import com.example.teamproject5.database.AppDatabase;
import com.example.teamproject5.database.entity.CertificateEntity;
import com.example.teamproject5.database.entity.RecentCertificateEntity;

import java.util.List;

/**
 * 자격증 목록 RecyclerView 어댑터
 *
 * 역할:
 *   - 자격증 리스트(List<Certificate>)를 item_certificate.xml 레이아웃으로 화면에 표시
 *   - 각 아이템마다 즐겨찾기 버튼(♡/♥)과 상세보기 버튼 제공
 *   - 버튼 클릭 이벤트를 OnCertificateActionListener 인터페이스로 상위 Activity에 위임
 *
 * 사용하는 곳:
 *   - CertificateListActivity : 전체 자격증 목록
 *   - FavoriteListActivity    : 즐겨찾기된 자격증 목록
 *   - CategoryListActivity    : 카테고리별 자격증 목록
 *
 * 인터페이스 (OnCertificateActionListener):
 *   - onFavoriteClick(certificate, position) : 즐겨찾기 버튼 클릭 시 호출
 *   - onDetailClick(certificate)             : 상세보기 버튼 클릭 시 호출
 *
 * 주요 메서드:
 *   - updateItem(position) : 특정 아이템만 갱신 (즐겨찾기 상태 변경 시 사용)
 *
 * 백엔드 연동 포인트:
 *   - 아이템 레이아웃(item_certificate.xml)에 표시할 필드 추가 시
 *     onBindViewHolder()와 ViewHolder에 필드 추가
 */

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {

    public interface OnCertificateActionListener {
        void onFavoriteClick(CertificateEntity certificate, int position);
        void onDetailClick(CertificateEntity certificate);
    }

    private final List<CertificateEntity> certificateList;
    private final OnCertificateActionListener listener;
    private CertificateRepository repository;
    private int userId;

    public CertificateAdapter(Context context, List<CertificateEntity> certificateList, OnCertificateActionListener listener, int userId) {
        this.certificateList = certificateList;
        this.listener = listener;
        this.userId = userId;
        repository = new CertificateRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_certificate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        CertificateEntity item = certificateList.get(position);

        holder.tvName.setText(item.certName);
        holder.tvDescription.setText(item.note);
        holder.tvCategory.setText(CategoryUtils.toLabel(item.category));
        boolean favorite = repository.isFavorite(userId, item.certId);

        updateFavoriteIcon(holder.btnFavorite, favorite);

        holder.btnFavorite.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();

            if(currentPosition != RecyclerView.NO_POSITION
                    && listener != null){

                listener.onFavoriteClick(
                        certificateList.get(currentPosition),
                        currentPosition);
            }
        });

        holder.btnGo.setOnClickListener(v -> {

            int currentPosition = holder.getBindingAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {

                CertificateEntity selectedCertificate =
                        certificateList.get(currentPosition);

                RecentCertificateEntity entity =
                        new RecentCertificateEntity();

                entity.certificateName =
                        selectedCertificate.certName;

                entity.viewTime =
                        System.currentTimeMillis();

                AppDatabase.getInstance(v.getContext())
                        .recentCertificateDAO()
                        .insert(entity);

                listener.onDetailClick(selectedCertificate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return certificateList == null ? 0 : certificateList.size();
    }

    public void updateItem(int position) {
        if (position >= 0 && position < certificateList.size()) {
            notifyItemChanged(position);
        }
    }

    private void updateFavoriteIcon(TextView button, boolean isFavorite) {
        button.setText(isFavorite ? "♥" : "♡");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;
        TextView tvCategory;
        TextView btnFavorite;
        Button btnGo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnGo = itemView.findViewById(R.id.btnGo);
        }
    }
}