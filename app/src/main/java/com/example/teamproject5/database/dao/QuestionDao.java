package com.example.teamproject5.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.teamproject5.database.entity.QuestionEntity;

import java.util.List;

@Dao
public interface QuestionDao {

    // 문제 추가    // 프로젝트에서 자격증 db를 어떤 식으로 추가할지 안 정했으나 일단 구조는 잡았음.
    @Insert
    void insert(QuestionEntity question);

    // 자격증별 문제 가져오기
    @Query("SELECT * FROM QuestionEntity WHERE certId = :certId")
    List<QuestionEntity> getQuestionsByCert(int certId);

    // 유형별 문제 (기출/예상)
    @Query("SELECT * FROM QuestionEntity WHERE certId = :certId AND type = :type")
    List<QuestionEntity> getQuestionsByType(int certId, String type);
}
