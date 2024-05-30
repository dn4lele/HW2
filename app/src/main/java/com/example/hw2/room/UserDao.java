package com.example.hw2.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM Users")
    List<UsersTables> getAll();

    //find a user by id
    @Query("SELECT * FROM Users WHERE id = :id")
    UsersTables findUserById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertoneUser(UsersTables usersTables);

    @Query("DELETE FROM Users")
    void deleteAll();


}
