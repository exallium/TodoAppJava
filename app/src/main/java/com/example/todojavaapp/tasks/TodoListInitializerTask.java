package com.example.todojavaapp.tasks;

import android.os.AsyncTask;

import com.example.todojavaapp.data.TodoListDao;
import com.example.todojavaapp.data.TodoListItem;

/**
 * Ensures that at least one TodoListItem exists in the database.
 */
public class TodoListInitializerTask extends AsyncTask<Void, Void, Void> {

    private final TodoListDao todoListDao;

    public TodoListInitializerTask(TodoListDao todoListDao) {
        this.todoListDao = todoListDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (todoListDao.todoListItemCount() == 0) {
            todoListDao.addTodoListItem(TodoListItem.createDefault());
        }
        return null;
    }
}
