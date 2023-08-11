package com.cookandroid.material.UserDB;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;

import com.cookandroid.material.FoodMaterialDB.MainData;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void insert(UserData userData);

    @Delete
    void delete(UserData userData);

//    @Delete
//    void reset(List<UserData> userData);

    @Query("UPDATE `table_user` SET mName = :sText WHERE sOnOff = 1")
    void update(String sText);

//    @Query("UPDATE `table_user` SET sOnOff = :onOff WHERE ID = :sID")
//    void switch_update(boolean onOff, int sID);

    @Query("SELECT * FROM `table_user`")
    List<UserData> getAll();
}
