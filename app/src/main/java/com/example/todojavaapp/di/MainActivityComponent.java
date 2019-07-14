package com.example.todojavaapp.di;

import com.example.todojavaapp.TodoViewModel;

import dagger.Component;

@PerActivity
@Component(dependencies = TodoAppComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    TodoViewModel viewModel();
}
