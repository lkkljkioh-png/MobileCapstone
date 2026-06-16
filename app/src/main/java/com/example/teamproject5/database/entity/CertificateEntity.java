package com.example.teamproject5.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "licenseList")
public class CertificateEntity {

    @PrimaryKey

    @ColumnInfo(name = "id")
    public int certId;
    @ColumnInfo(name = "name")
    public String certName;             // 자격증 이름
    public String category;             // 자격증 분야
    public String exdate;
    public String qualification;
    public String form;
    public String note;
}
