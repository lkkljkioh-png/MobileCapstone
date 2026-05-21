package com.example.teamproject5.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.teamproject5.database.entity.CertificateEntity;

import java.util.List;

@Dao
public interface CertificateDao {

    @Insert
    void insert(CertificateEntity certificate);

    @Query("SELECT * FROM CertificateEntity")
    List<CertificateEntity> getAll();
}