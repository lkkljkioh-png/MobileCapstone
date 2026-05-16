package com.tp.licenseList;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {License.class}, version = 1)
public abstract class LicenseDatabase extends RoomDatabase {
    public abstract LicenseDao licenseDao();
    private static LicenseDatabase instance;

    public static LicenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LicenseDatabase.class, "license_db")
                    .createFromAsset("licensedb.db")
                    .build();
        }
        return instance;
    }
}