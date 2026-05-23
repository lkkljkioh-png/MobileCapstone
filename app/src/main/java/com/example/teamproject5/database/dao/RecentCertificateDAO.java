package com.example.teamproject5.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.teamproject5.database.entity.RecentCertificateEntity;

import java.util.List;

@Dao
    public interface RecentCertificateDAO {

        @Insert
        void insert(RecentCertificateEntity entity);

        @Query("SELECT * FROM recent_certificate ORDER BY view_time DESC") // RecentCertifiacateEntity(recent_certificate로 명명함)에서 view_time 별로 정렬해서 골라서 객체로 생성
        List<RecentCertificateEntity> getRecentCertificates();             // Room이 자동 생성 (getRecentCertificates)

    }

