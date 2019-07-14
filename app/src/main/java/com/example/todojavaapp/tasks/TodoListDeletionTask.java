package com.example.todojavaapp.tasks;

import android.os.AsyncTask;

import com.example.todojavaapp.data.TodoListDao;
import com.example.todojavaapp.data.TodoListItem;
import com.example.todojavaapp.utils.Action;

/**
 * Deletes the given items, and then runs the given Action on the
 * Main thread.
 */
public class TodoListDeletionTask extends AsyncTask<TodoListItem, Void, Void> {

    private final TodoListDao todoListDao;
    private final Action onPostExecuteAction;

    public TodoListDeletionTask(TodoListDao todoListDao, Action onPostExecuteAction) {
        this.todoListDao = todoListDao;
        this.onPostExecuteAction = onPostExecuteAction;
    }

    @Override
    protected Void doInBackground(TodoListItem... items) {
        for (TodoListItem item : items) {
            if (item != null) todoListDao.deleteTodoListItem(item);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        onPostExecuteAction.invoke();
    }
}
