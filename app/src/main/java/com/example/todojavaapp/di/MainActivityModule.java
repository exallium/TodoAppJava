package com.example.todojavaapp.di;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.todojavaapp.TodoViewModel;
import com.example.todojavaapp.data.TodoListDao;
import com.example.todojavaapp.tasks.TodoViewModelContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    private final AppCompatActivity mainActivity;

    public MainActivityModule(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @PerActivity
    TodoViewModel.Contract provideTodoViewModelContract(TodoListDao todoListDao) {
        return new TodoViewModelContract(todoListDao);
    }

    @Provides
    @PerActivity
    TodoViewModel.Factory provideTodoViewModelFactory(TodoViewModel.Contract contract) {
        return new TodoViewModel.Factory(contract);
    }

    /**
     * Note that this is unscoped.  ViewModelProviders will handle the lifecycle of
     * TodoViewModel for us, so there's no sense in double-caching it.
     */
    @Provides
    TodoViewModel provideTodoViewModel(TodoViewModel.Factory factory) {
        return ViewModelProviders.of(mainActivity, factory).get(TodoViewModel.class);
    }
}
