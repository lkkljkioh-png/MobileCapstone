package com.example.teamproject5.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.teamproject5.database.entity.ScheduleEntity;
import java.util.List;

@Dao
public interface ScheduleDAO {
    @Insert (onConflict = OnConflictStrategy.FAIL)                               // 저장
    void insert(ScheduleEntity schedule);
    @Update
    void update(ScheduleEntity schedule);                                        // 수정
    @Delete
    void delete(ScheduleEntity schedule);                                        // 삭제
    @Query("SELECT*FROM ScheduleEntity")                                         // 전체 조회 (전체 일정 보여줄 때 사용)
    List<ScheduleEntity> getALL();
    @Query("SELECT*FROM ScheduleEntity WHERE scheduleDate= :scheduleDate")       // 날짜별 조회 (날짜 별 일정 표시할 때 사용)
    List<ScheduleEntity> getByDate(String scheduleDate);
}