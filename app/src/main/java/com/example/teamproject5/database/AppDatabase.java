package com.example.teamproject5.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.teamproject5.database.dao.CertificateDao;
import com.example.teamproject5.database.dao.QuestionDao;
import com.example.teamproject5.database.dao.UserDao;
import com.example.teamproject5.database.entity.CertificateEntity;
import com.example.teamproject5.database.entity.QuestionEntity;
import com.example.teamproject5.database.entity.UserEntity;

@Database(
        entities = {
                UserEntity.class,
                CertificateEntity.class,
                QuestionEntity.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    // DAO 연결
    public abstract UserDao userDao();
    public abstract QuestionDao questionDao();
    public abstract CertificateDao certificateDao();

    // 싱글톤
    private static AppDatabase INSTANCE;

    // DB 가져오기
    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database"
                    )
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}