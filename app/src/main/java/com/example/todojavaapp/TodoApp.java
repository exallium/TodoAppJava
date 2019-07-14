package com.example.todojavaapp;

import android.app.Application;

import com.example.todojavaapp.di.DaggerTodoAppComponent;
import com.example.todojavaapp.di.Injector;
import com.example.todojavaapp.di.TodoAppModule;

public class TodoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.INSTANCE.setComponent(DaggerTodoAppComponent.builder()
                .todoAppModule(new TodoAppModule(this))
                .build());
    }
}
