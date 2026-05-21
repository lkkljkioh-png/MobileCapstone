package com.example.teamproject5.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

import com.example.teamproject5.Certificate;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Certificate.class,
                parentColumns = "certId",
                childColumns = "certId",
                onDelete = CASCADE
        )
)

public class QuestionEntity {

    @PrimaryKey(autoGenerate = true)
    public int questionId;

    public int certId;

    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;

    public int answer;
    public String explanation;

    public String type; // "기출", "예상"
}
