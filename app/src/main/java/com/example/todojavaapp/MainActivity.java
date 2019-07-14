package com.example.todojavaapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todojavaapp.di.DaggerMainActivityComponent;
import com.example.todojavaapp.di.Injector;
import com.example.todojavaapp.di.MainActivityComponent;
import com.example.todojavaapp.di.MainActivityModule;

public class MainActivity extends AppCompatActivity
        implements TodoListAdapter.Listener {

    private TodoViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MainActivityComponent component = DaggerMainActivityComponent.builder()
                .todoAppComponent(Injector.INSTANCE.getComponent())
                .mainActivityModule(new MainActivityModule(this))
                .build();

        viewModel = component.viewModel();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        TodoListAdapter adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel.focusedPosition.observe(this, adapter::updateFocused);
        viewModel.todoItems.observe(this, adapter::updateData);

    }

    // The following are simple pass-through methods.  Alternatives to doing this
    // include creating a private anonymous class, implementing TodoListAdapter.Listener
    // directly on ViewModel, or splitting things into 4 interfaces and passing view
    // references.  I chose this approach for a few reasons:
    // 1. It's a small number of methods and VERY straightforward.
    //      These are all simple pass-thrus which in my mind doesn't really denote "logic"
    // 2. If I split everything up, I now need to plumb all 4 into my ViewHolder
    //      as all of these are, in the end, being used in the same spot.
    // 3. Implementing the interface on the ViewModel violates my dependency arrows.
    //      i.e. I should be able to pull ViewModel into a separate module without breaking.

    @Override
    public void onRemove(int position) {
        viewModel.onDelete(position);
    }

    @Override
    public void onTextChanged(int position, String message) {
        viewModel.onUpdateMessage(position, message);
    }

    @Override
    public void toggleChecked(int position) {
        viewModel.onToggleCheck(position);
    }

    @Override
    public void setFocus(int position) {
        viewModel.onTapUnfocused(position);
    }
}
