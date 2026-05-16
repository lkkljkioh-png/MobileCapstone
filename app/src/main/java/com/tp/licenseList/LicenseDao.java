package com.tp.licenseList;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface LicenseDao {
    @Query("SELECT * FROM licenseList")
    List<License> getAll(); // 150개 데이터를 한 번에 다 가져오는 함수
}
