package com.tp.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "userdata")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_serialNumber")
    public Long serialNumber;

    @ColumnInfo(name = "user_name")
    public String name;

    @ColumnInfo(name = "user_id")
    public String id;

    public User() {}

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }
}