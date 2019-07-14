package com.example.todojavaapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todojavaapp.data.TodoListItem;
import com.example.todojavaapp.utils.Action;

import java.util.List;

import static com.example.todojavaapp.utils.PresentationConstants.NO_FOCUS;

/**
 * TodoViewModel serves as a link between User Intent and our Model.
 */
public class TodoViewModel extends ViewModel {

    /**
     * Instead of dealing with AsyncTask or the Dao directly, we instead
     * delegate this work to a Contract.  This is similar to the Single-Model-Class
     * topology in MVP, or to a UseCase or Repository in other patterns.
     *
     * It gives us a single point of access to the rest of our system, and is focused
     * (following the ISP) so that we can do exactly what we need to and nothing more.
     */
    public interface Contract {
        LiveData<List<TodoListItem>> getTodoItems();
        void deleteItem(TodoListItem item, Action afterDeletion);
        void updateItems(TodoListItem ... item);
        void initialize();
    }

    private final Contract contract;

    private MutableLiveData<Integer> internalFocusedPosition = new MutableLiveData<>();
    public LiveData<Integer> focusedPosition = internalFocusedPosition;

    public final LiveData<List<TodoListItem>> todoItems;

    public TodoViewModel(Contract contract) {
        this.contract = contract;
        internalFocusedPosition.setValue(NO_FOCUS);
        todoItems = contract.getTodoItems();
        contract.initialize();
    }

    public void onTapUnfocused(int position) {
        internalFocusedPosition.setValue(position);
    }

    public void onUpdateMessage(int position, String message) {
        TodoListItem[] toAdd = new TodoListItem[2];
        toAdd[0] = todoItems
                .getValue().get(position)
                .buildUpon()
                .message(message)
                .build();

        if (position == todoItems.getValue().size() - 1) {
            toAdd[1] = TodoListItem.createDefault();
        }
        contract.updateItems(toAdd);
    }

    public void onToggleCheck(int position) {
        TodoListItem item = todoItems.getValue().get(position);
        contract.updateItems(item
                .buildUpon()
                .isChecked(!item.isChecked)
                .build());
    }

    public void onDelete(int position) {
        contract.deleteItem(todoItems.getValue().get(position),
                () -> internalFocusedPosition.setValue(NO_FOCUS));
    }

    /**
     * This factory will be passed to ViewModelProviders.of, and can be
     * injected via Dagger (see MainActivityModule and MainActivityComponent)
     */
    public static class Factory implements ViewModelProvider.Factory {

        private final TodoViewModel.Contract contract;

        public Factory(Contract contract) {
            this.contract = contract;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TodoViewModel(contract);
        }
    }
}
