package com.example.teamproject5.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int userID;

    public String email;
    public String password;
    public String nickname;
}
