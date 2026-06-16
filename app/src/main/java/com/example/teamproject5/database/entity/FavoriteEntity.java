package com.example.teamproject5.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "favorite",
        primaryKeys = {"userId", "certId"}
)
public class FavoriteEntity {
    public int userId;

    public int certId;
}