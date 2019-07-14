package com.example.todojavaapp.tasks;

import android.os.AsyncTask;

import com.example.todojavaapp.data.TodoListDao;
import com.example.todojavaapp.data.TodoListItem;

import java.util.HashSet;
import java.util.Set;

/**
 * Updates the Database with the given TodoListItems.
 */
class TodoMessageWriterTask extends AsyncTask<TodoListItem, Void, Void> {

    private final TodoListDao todoListDao;
    private static Set<Long> viewedIds = new HashSet<>();

    TodoMessageWriterTask(TodoListDao todoListDao) {
        this.todoListDao = todoListDao;
    }

    @Override
    protected Void doInBackground(TodoListItem... todoListItems) {

        // If item has already been sent, we can ignore any additional passed items.
        // Passed item beyond first is only a "new line".  This piece of code basically
        // makes sure we don't end up with a race condition.
        if (viewedIds.contains(todoListItems[0].id)) {
            todoListDao.addTodoListItem(todoListItems[0]);
            return null;
        }
        viewedIds.add(todoListItems[0].id);

        for (TodoListItem item : todoListItems) {
            if (item == null) continue;
            todoListDao.addTodoListItem(item);
        }
        return null;
    }
}
