package com.example.teamproject5.database.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ScheduleEntity {

    @PrimaryKey(autoGenerate = true)
    public int dataID;                  // 일정 번호
    public String userID;               // 일정 저장한 사용자명
    public String scheduleDate;          // 일정 날짜
    public String scheduleTitle;         // 일정명

}
