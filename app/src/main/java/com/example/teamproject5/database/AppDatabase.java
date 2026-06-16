package com.example.teamproject5.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.teamproject5.database.dao.FavoriteDao;
import com.example.teamproject5.database.dao.RecentCertificateDAO;
import com.example.teamproject5.database.dao.ScheduleDAO;
import com.example.teamproject5.database.entity.FavoriteEntity;
import com.example.teamproject5.database.entity.RecentCertificateEntity;
import com.example.teamproject5.database.entity.ScheduleEntity;
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
                QuestionEntity.class,
                ScheduleEntity.class,
                RecentCertificateEntity.class,
                FavoriteEntity.class
        },
        version = 3
)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ScheduleDAO scheduleDAO();

    // DAO 연결
    public abstract UserDao userDao();
    public abstract QuestionDao questionDao();
    public abstract CertificateDao certificateDao();
    public abstract FavoriteDao favoriteDao();

    // 싱글톤
    private static AppDatabase INSTANCE;

    // DB 가져오기
    public static AppDatabase getInstance(Context context) {            // getDB -> getInstance

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database"
                    )
                    .fallbackToDestructiveMigration()   // 테스트용
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

    public abstract RecentCertificateDAO recentCertificateDAO();
}




