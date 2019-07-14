package com.example.todojavaapp;

import com.example.todojavaapp.data.TodoListItem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.todojavaapp.utils.PresentationConstants.NO_FOCUS;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class TodoViewModelTest {

    private final FakeTodoViewModelContract contract = new FakeTodoViewModelContract();

    private final TodoViewModel testSubject = new TodoViewModel(contract);

    @Test
    public void whenIInitialize_thenIInitializeContract() {
        // THEN
        Assert.assertEquals(1, contract.itemCount());
    }

    @Test
    public void whenIInitialize_thenIExpectNO_FOCUS() {
        // GIVEN
        List<Integer> positions = new ArrayList<>();
        testSubject.focusedPosition.observeForever(positions::add);

        // THEN
        Assert.assertEquals(positions.get(positions.size() - 1).intValue(), NO_FOCUS);
    }

    @Test
    public void whenIUpdateLastPosition_thenICreateNewItem() {
        // WHEN
        testSubject.onUpdateMessage(0, "Hello, World!");

        // THEN
        List<TodoListItem> state = contract.getState();
        Assert.assertEquals(state.size(), 2);
        Assert.assertEquals(state.get(0).message, "Hello, World!");
        Assert.assertEquals(state.get(1).message, "");
    }

    @Test
    public void whenIUpdateNonLastPosition_thenIDoNotCreateNewItem() {
        // GIVEN
        testSubject.onUpdateMessage(0, "Hello, World!");

        // WHEN
        testSubject.onUpdateMessage(0, "Hello, World!!");

        // THEN
        List<TodoListItem> state = contract.getState();
        Assert.assertEquals(state.size(), 2);
        Assert.assertEquals(state.get(0).message, "Hello, World!!");
        Assert.assertEquals(state.get(1).message, "");
    }

    @Test
    public void whenIToggleChecked_thenIExpectProperStateUpdate() {
        // WHEN
        testSubject.onToggleCheck(0);

        // THEN
        List<TodoListItem> state = contract.getState();
        Assert.assertEquals(state.size(), 1);
        Assert.assertTrue(state.get(0).isChecked);
    }

    @Test
    public void whenIUpdateFocus_thenIExpectNotificationFromFocusedLiveData() {
        // GIVEN
        List<Integer> positions = new ArrayList<>();
        testSubject.focusedPosition.observeForever(positions::add);

        // WHEN
        testSubject.onTapUnfocused(0);

        // THEN
        Assert.assertEquals(positions.get(positions.size() - 1).intValue(), 0);
    }

    @Test
    public void whenIDelete_thenIExpectNO_FOCUS() {
        // GIVEN
        List<Integer> positions = new ArrayList<>();
        testSubject.focusedPosition.observeForever(positions::add);

        // WHEN
        testSubject.onDelete(0);

        // THEN
        Assert.assertEquals(positions.get(positions.size() - 1).intValue(), NO_FOCUS);
    }

    @Test
    public void whenIDelete_thenIExpectItemDeletedFromState() {
        // GIVEN
        testSubject.onUpdateMessage(0, "0");
        testSubject.onUpdateMessage(1, "1");
        testSubject.onUpdateMessage(2, "2");

        // WHEN
        testSubject.onDelete(1);

        // THEN (Remember IDs are 1-indexed)
        List<TodoListItem> state = contract.getState();
        for (TodoListItem item : state) {
            if (item.id == 2) Assert.fail("Item with ID 2 exists.");
        }
    }
}