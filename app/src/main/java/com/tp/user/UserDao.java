package com.tp.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void createUser(User user);

    @Query("SELECT * FROM userdata")
    List<User> getAllUsers();
}