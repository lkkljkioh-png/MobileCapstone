package com.tp.licenseList;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface LicenseDao {
    @Query("SELECT * FROM licenseList")
    List<License> getAll(); // 150개 데이터를 한 번에 다 가져오는 함수

    @Query("SELECT * FROM licenseList WHERE " +
            "(name LIKE '%' || :searchQuery || '%' OR " +
            "category LIKE '%' || :searchQuery || '%' OR " +
            "qualification LIKE '%' || :searchQuery || '%') " +


            "AND (" +

            "(:checkWritten = 0 AND :checkPractical = 0) OR " +
            "(:checkWritten = 1 AND :checkPractical = 1) OR " +
            "(:checkWritten = 1 AND :checkPractical = 0 AND form LIKE '%필기%') OR " +
            "(:checkWritten = 0 AND :checkPractical = 1 AND form LIKE '%실기%')" +
            ")")
    List<License> searchLicense(String searchQuery, int checkWritten, int checkPractical);
}
