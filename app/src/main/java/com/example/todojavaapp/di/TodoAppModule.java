package com.example.todojavaapp.di;

import android.content.Context;

import androidx.room.Room;

import com.example.todojavaapp.data.TodoListDao;
import com.example.todojavaapp.data.TodoListDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TodoAppModule {

    private final Context context;

    public TodoAppModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    TodoListDatabase provideTodoListDatabase() {
        return Room.databaseBuilder(context, TodoListDatabase.class, "todo_list").build();
    }

    @Provides
    @Singleton
    TodoListDao provideTodoListDao(TodoListDatabase database) {
        return database.todoListDao();
    }
}
