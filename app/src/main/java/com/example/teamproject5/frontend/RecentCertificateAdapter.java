package com.example.teamproject5.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject5.R;
import com.example.teamproject5.database.entity.RecentCertificateEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentCertificateAdapter
        extends RecyclerView.Adapter<RecentCertificateAdapter.ViewHolder> {

    private List<RecentCertificateEntity> list = new ArrayList<>();

    public void setList(List<RecentCertificateEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_certificate, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        RecentCertificateEntity item = list.get(position);

        holder.txtName.setText(item.certificateName);

        String time =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.KOREA
                ).format(new Date(item.viewTime));

        holder.txtTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtCertificateName);
            txtTime = itemView.findViewById(R.id.txtViewTime);
        }
    }
}