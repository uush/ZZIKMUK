package com.cookandroid.zzikmuk.FoodMaterialDB;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao
{
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    @Delete
    void delete(MainData mainData);

    @Delete
    void reset(List<MainData> mainData);

    @Query("UPDATE `Food Material` SET mName = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    @Query("UPDATE `Food Material` SET sOnOff = :onOff WHERE ID = :sID")
    void switch_update(boolean onOff, int sID);

    @Query("SELECT * FROM `Food Material`")
    List<MainData> getAll();
}