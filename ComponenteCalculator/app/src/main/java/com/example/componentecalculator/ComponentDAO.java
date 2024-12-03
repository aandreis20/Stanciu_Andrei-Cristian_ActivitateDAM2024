package com.example.componentecalculator;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ComponentDAO {
    @Insert
    void insertComponent(Component component);

    @Query("Select * from Components")
    List<Component> getComponents();

    @Delete
    void deleteComponent(Component component);
}
