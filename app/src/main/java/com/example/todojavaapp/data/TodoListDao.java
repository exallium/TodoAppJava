package com.example.todojavaapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoListDao {

    @Query("SELECT * FROM todolistitem")
    LiveData<List<TodoListItem>> watchTodoListItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTodoListItem(TodoListItem todoListItem);

    @Delete()
    void deleteTodoListItem(TodoListItem todoListItem);

    @Query("SELECT COUNT(*) FROM todolistitem")
    int todoListItemCount();

}
