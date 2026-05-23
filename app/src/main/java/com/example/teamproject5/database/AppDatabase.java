package com.example.teamproject5.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.teamproject5.database.dao.RecentCertificateDAO;
import com.example.teamproject5.database.dao.ScheduleDAO;
import com.example.teamproject5.database.entity.RecentCertificateEntity;
import com.example.teamproject5.database.entity.ScheduleEntity;

@Database(entities = {ScheduleEntity.class, RecentCertificateEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScheduleDAO scheduleDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDB(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "schedule.db"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract RecentCertificateDAO recentCertificateDAO();
}




