package com.example.teamproject5.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.teamproject5.database.dao.CertificateDao;
import com.example.teamproject5.database.entity.CertificateEntity;

@Database(
        entities = {
                CertificateEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class LicenseDatabase extends RoomDatabase {

    // DAO
    public abstract CertificateDao certificateDao();

    // 싱글톤
    private static LicenseDatabase INSTANCE;

    public static LicenseDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LicenseDatabase.class,
                            "licensedb.db"
                    )
                    .createFromAsset("licensedb.db")
                    .allowMainThreadQueries()   // 테스트용
                    .build();
        }

        return INSTANCE;
    }
}