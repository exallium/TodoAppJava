package com.example.todojavaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todojavaapp.data.TodoListItem;
import com.example.todojavaapp.utils.Action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FakeTodoViewModelContract implements TodoViewModel.Contract {

    private static long id = 0;
    private Map<Long, TodoListItem> itemStore = new LinkedHashMap<>();

    private MutableLiveData<List<TodoListItem>> itemLiveData = new MutableLiveData<>();

    @Override
    public LiveData<List<TodoListItem>> getTodoItems() {
        return itemLiveData;
    }

    @Override
    public void deleteItem(TodoListItem item, Action afterDeletion) {
        itemStore.remove(item.id);
        updateLiveData();
        afterDeletion.invoke();
    }

    @Override
    public void updateItems(TodoListItem... item) {
        for (TodoListItem it : item) {
            if (it == null) continue;
            if (it.id == 0) {
                id = id + 1;
                itemStore.put(id, it.buildUpon().setId(id).build());
            } else {
                itemStore.put(it.id, it);
            }
        }
        updateLiveData();
    }

    @Override
    public void initialize() {
        if (itemStore.size() == 0) {
            updateItems(TodoListItem.createDefault());
        }
    }

    public void setState(List<TodoListItem> items) {
        itemStore.clear();

        TodoListItem[] arr = new TodoListItem[items.size()];
        items.toArray(arr);

        updateItems(arr);
    }

    public List<TodoListItem> getState() {
        return itemLiveData.getValue();
    }

    public int itemCount() {
        return itemStore.size();
    }

    private void updateLiveData() {
        itemLiveData.setValue(new ArrayList<>(itemStore.values()));
    }
}
