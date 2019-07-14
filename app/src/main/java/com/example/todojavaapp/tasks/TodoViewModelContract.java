package com.example.todojavaapp.tasks;

import androidx.lifecycle.LiveData;

import com.example.todojavaapp.TodoViewModel;
import com.example.todojavaapp.data.TodoListDao;
import com.example.todojavaapp.data.TodoListItem;
import com.example.todojavaapp.utils.Action;

import java.util.List;

public class TodoViewModelContract implements TodoViewModel.Contract {

    private final TodoListDao dao;

    public TodoViewModelContract(TodoListDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<TodoListItem>> getTodoItems() {
        return dao.watchTodoListItems();
    }

    @Override
    public void deleteItem(TodoListItem item, Action afterDeletion) {
        new TodoListDeletionTask(dao, afterDeletion::invoke).execute(item);
    }

    @Override
    public void updateItems(TodoListItem ... item) {
        new TodoMessageWriterTask(dao).execute(item);
    }

    @Override
    public void initialize() {
        new TodoListInitializerTask(dao).execute();
    }
}
