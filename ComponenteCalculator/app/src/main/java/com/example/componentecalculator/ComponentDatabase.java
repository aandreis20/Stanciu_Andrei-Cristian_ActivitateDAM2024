package com.example.componentecalculator;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Component.class}, version = 1)
public abstract class ComponentDatabase extends RoomDatabase {
    public abstract ComponentDAO getDaoObject();
}
