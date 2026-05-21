package com.example.teamproject5.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CertificateEntity {

    @PrimaryKey(autoGenerate = true)
    public int certID;

    public String certName;             // 자격증 이름
    public String category;             // 자격증 분야
}
