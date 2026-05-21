package com.example.teamproject5.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.teamproject5.database.entity.UserEntity;

@Dao
public interface UserDao {

    // 회원가입
    @Insert
    void insert(UserEntity user);

    // 로그인
    @Query("SELECT * FROM UserEntity WHERE email = :email and password = :password LIMIT 1")
    UserEntity login(String email, String password);

    // 이메일 중복 체크
    @Query("SELECT * FROM UserEntity WHERE email= :email LIMIT 1")
    UserEntity checkEmail(String email);
}
