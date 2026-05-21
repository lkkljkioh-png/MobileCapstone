package com.example.teamproject5.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.teamproject5.database.dao.ScheduleDAO;
import com.example.teamproject5.database.entity.ScheduleEntity;

@Database(entities = {ScheduleEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScheduleDAO scheduleDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDB(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "schedule.db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}

