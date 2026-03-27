package com.example.teamproject5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {

    public interface OnCertificateActionListener {
        void onFavoriteClick(Certificate certificate, int position);
        void onDetailClick(Certificate certificate);
    }

    private final List<Certificate> certificateList;
    private final OnCertificateActionListener listener;

    public CertificateAdapter(List<Certificate> certificateList, OnCertificateActionListener listener) {
        this.certificateList = certificateList;
        this.listener = listener;
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
        Certificate item = certificateList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.tvCategory.setText(item.getCategory());
        updateFavoriteIcon(holder.btnFavorite, item.isFavorite());

        holder.btnFavorite.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onFavoriteClick(certificateList.get(currentPosition), currentPosition);
            }
        });

        holder.btnGo.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onDetailClick(certificateList.get(currentPosition));
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

    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        button.setImageResource(
                isFavorite
                        ? android.R.drawable.btn_star_big_on
                        : android.R.drawable.btn_star_big_off
        );
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;
        TextView tvCategory;
        ImageButton btnFavorite;
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