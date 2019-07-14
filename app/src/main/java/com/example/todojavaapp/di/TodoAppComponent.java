package com.example.todojavaapp.di;

import com.example.todojavaapp.data.TodoListDao;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = TodoAppModule.class)
public interface TodoAppComponent {
    TodoListDao todoListDao();
}
