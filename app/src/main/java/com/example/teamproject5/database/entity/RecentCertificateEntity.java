package com.example.teamproject5.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_certificate")
public class RecentCertificateEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "certificate_name")
    public String certificateName;

    @ColumnInfo(name = "view_time")
    public long viewTime;

    // Room이 사용할 기본 생성자
    public RecentCertificateEntity() {
    }
}