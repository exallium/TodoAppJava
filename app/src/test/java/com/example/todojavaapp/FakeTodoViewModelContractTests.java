package com.example.todojavaapp;

import com.example.todojavaapp.data.TodoListItem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class FakeTodoViewModelContractTests {

    private final FakeTodoViewModelContract testSubject = new FakeTodoViewModelContract();

    @Test
    public void givenNoItemsAdded_whenIInitialize_thenIAddASingleItem() {
        // GIVEN
        Assert.assertEquals(0, testSubject.itemCount());

        // WHEN
        testSubject.initialize();

        // THEN
        Assert.assertEquals(1, testSubject.itemCount());
    }

    @Test
    public void givenOneItemAdded_whenISetWithNone_thenIRemoveAll() {
        // GIVEN
        testSubject.initialize();

        // WHEN
        testSubject.setState(new ArrayList<>());

        // THEN
        Assert.assertEquals(0, testSubject.itemCount());
    }

    @Test
    public void givenItemExists_whenIRemoveItem_thenItemNoLongerPresent() {
        // GIVEN
        testSubject.updateItems(TodoListItem.createDefault(), TodoListItem.createDefault());
        TodoListItem item = testSubject.getState().get(0);

        // WHEN
        testSubject.deleteItem(item, () -> {});

        // THEN
        Assert.assertEquals(testSubject.itemCount(), 0);
    }

    @Test
    public void whenIRemoveItem_thenIInvokeAction() {
        // GIVEN

        // This is wrapped in a list to get around the final req.
        final List<Boolean> called = new ArrayList<>();

        // WHEN
        testSubject.deleteItem(TodoListItem.createDefault(),
                () -> called.add(true));

        // THEN
        Assert.assertTrue(called.get(0));
    }

    @Test
    public void whenIUpdateList_thenIAmNotifiedViaLiveData() {
        // GIVEN
        List<TodoListItem> update = new ArrayList<>();
        testSubject.getTodoItems().observeForever(list -> {
            update.clear();
            update.addAll(list);
        });

        // WHEN
        testSubject.updateItems(
                TodoListItem.createDefault(),
                TodoListItem.createDefault(),
                TodoListItem.createDefault(),
                TodoListItem.createDefault()
        );

        // THEN
        Assert.assertEquals(4, update.size());
    }

    @Test
    public void givenANullTodoListItem_whenIUpdateList_thenIDoNotCrash() {
        // GIVEN
        TodoListItem update = null;

        // WHEN
        testSubject.updateItems(update);
    }
}
