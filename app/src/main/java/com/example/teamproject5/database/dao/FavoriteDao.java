package com.example.teamproject5.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.teamproject5.database.entity.CertificateEntity;
import com.example.teamproject5.database.entity.FavoriteEntity;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(FavoriteEntity favorite);

    @Delete
    void delete(FavoriteEntity favorite);

    @Query("SELECT * FROM favorite WHERE userId=:userId AND certId=:certId LIMIT 1")
    FavoriteEntity getFavorite(int userId,int certId);

    @Query("SELECT certId FROM favorite WHERE userId=:userId")
    List<Integer> getFavoriteIds(int userId);
}
