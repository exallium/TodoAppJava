package com.example.todojavaapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {TodoListItem.class})
public abstract class TodoListDatabase extends RoomDatabase {
    abstract public TodoListDao todoListDao();
}
