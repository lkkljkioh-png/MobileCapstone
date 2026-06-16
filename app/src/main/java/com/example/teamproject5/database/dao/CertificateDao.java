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

    @Query("SELECT * FROM licenseList")
    List<CertificateEntity> getAll();

    @Query("SELECT * FROM licenseList WHERE " +
            "(name LIKE '%' || :searchQuery || '%' OR " +
            "category LIKE '%' || :searchQuery || '%' OR " +
            "qualification LIKE '%' || :searchQuery || '%') " +


            "AND (" +

            "(:checkWritten = 0 AND :checkPractical = 0) OR " +
            "(:checkWritten = 1 AND :checkPractical = 1) OR " +
            "(:checkWritten = 1 AND :checkPractical = 0 AND form LIKE '%필기%') OR " +
            "(:checkWritten = 0 AND :checkPractical = 1 AND form LIKE '%실기%')" +
            ")")
    List<CertificateEntity> searchCertificate(String searchQuery, int checkWritten, int checkPractical);
}