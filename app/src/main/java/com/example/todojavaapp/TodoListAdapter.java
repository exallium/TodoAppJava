package com.example.todojavaapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todojavaapp.data.TodoListItem;

import java.util.Collections;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;
import static com.example.todojavaapp.utils.PresentationConstants.NO_FOCUS;

/**
 * TodoListAdapter manages rendering of TodoListItems in a recycler view
 * Also manages focus state for these items, as to prevent rebinds from occurring
 * erroneously.
 * <p>
 * Focused could probably be adapted as a data-item property, but this seemed like
 * a less roundabout way.
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<TodoListItem> data = Collections.emptyList();
    private int focused = NO_FOCUS;

    private final Listener listener;

    public TodoListAdapter(Listener listener) {
        this.listener = listener;
    }

    @MainThread
    public void updateData(List<TodoListItem> newData) {
        List<TodoListItem> oldData = data;
        data = newData;
        Differ differ = new Differ(oldData, newData, focused);
        DiffUtil.calculateDiff(differ).dispatchUpdatesTo(this);
    }

    @MainThread
    public void updateFocused(int focused) {
        if (this.focused == focused) return;
        if (this.focused != NO_FOCUS)
            notifyItemChanged(this.focused);
        this.focused = focused;
        if (focused != NO_FOCUS && focused != this.data.size() - 1)
            notifyItemChanged(focused);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position), position == data.size() - 1, position == focused);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Ensures that the focused and last items in the list are always the todo_focused type.
     * We use layout ids as view types here to avoid needing a switch inside onCreateViewHolder.
     */
    @Override
    public int getItemViewType(int position) {
        return position == focused || position == data.size() - 1 ? R.layout.todo_focused : R.layout.todo_unfocused;
    }

    /**
     * Manages a single item rendered in the RecyclerView.  All input / output is routed
     * through here for the item.  Thus, it has a reference to a Listener (see below)
     */
    static class ViewHolder extends RecyclerView.ViewHolder implements TextWatcher, CompoundButton.OnCheckedChangeListener {

        private final CheckBox check;
        private final TextView input;
        private final ImageView remove;

        private final Listener listener;

        public ViewHolder(@NonNull View itemView, Listener listener) {
            super(itemView);
            this.listener = listener;

            check = itemView.findViewById(R.id.check);
            input = itemView.findViewById(R.id.input);
            remove = itemView.findViewById(R.id.remove);

            // We set up two listeners here on input.  The first is to allow
            // us to focus on a TextView (turning it into the edit text)
            // The second does two things.  1, it updates the focus position which
            // Captures an edge case for the last row (always shows as an EditText)
            // 2, it makes sure that when we focus on an edit text, we set the cursor
            // to the end.

            input.setOnClickListener((v) -> {
                if (getAdapterPosition() == NO_POSITION) return;
                if (!v.onCheckIsTextEditor()) {
                    listener.setFocus(getAdapterPosition());
                }
            });

            input.setOnFocusChangeListener((v, f) -> {
                if (getAdapterPosition() == NO_POSITION) return;
                if (f) {
                    listener.setFocus(getAdapterPosition());
                    if (v.onCheckIsTextEditor()) {
                        EditText editText = (EditText) v;
                        editText.setSelection(editText.length());
                    }
                }
            });

            if (remove != null)
                remove.setOnClickListener(v -> {
                    if (getAdapterPosition() != NO_POSITION)
                        listener.onRemove(getAdapterPosition());
                });
        }

        void bind(TodoListItem todoListItem, boolean isLast, boolean shouldHaveFocus) {

            // Unset and then set listeners in order to avoid them being invoked when
            // we programatically update their state.
            check.setOnCheckedChangeListener(null);
            check.setChecked(todoListItem.isChecked);
            check.setOnCheckedChangeListener(this);

            input.removeTextChangedListener(this);
            input.setText(todoListItem.message);
            input.addTextChangedListener(this);

            if (shouldHaveFocus) {
                input.requestFocus();
            }

            // The last item in the list should NOT have focus.  Again, we do this
            // here as a slight optimization compared to say, keeping track of whether
            // to display the remove button as part of our state.
            if (remove != null) {
                remove.setVisibility(isLast ? View.INVISIBLE : View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do Nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do Nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (getAdapterPosition() == NO_POSITION) return;
            listener.onTextChanged(getAdapterPosition(), s.toString());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (getAdapterPosition() == NO_POSITION) return;
            listener.toggleChecked(getAdapterPosition());
        }

    }

    /**
     * Listener will be notified whenever a line item is modified by user input.
     */
    interface Listener {
        void onRemove(int position);

        void onTextChanged(int position, String message);

        void toggleChecked(int position);

        void setFocus(int position);
    }

    /**
     * Compares old and new TodoListItems, and includes a early-exit in areContentsTheSame
     * to prevent re-binding a focused item.
     */
    private static class Differ extends DiffUtil.Callback {

        private final List<TodoListItem> oldList;
        private final List<TodoListItem> newList;
        private final int focused;

        public Differ(List<TodoListItem> oldList, List<TodoListItem> newList, int focused) {
            this.oldList = oldList;
            this.newList = newList;
            this.focused = focused;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

            if (oldItemPosition == focused) return true;

            TodoListItem oldItem = oldList.get(oldItemPosition);
            TodoListItem newItem = newList.get(newItemPosition);

            return oldItem.id == newItem.id
                    && oldItem.isChecked == newItem.isChecked
                    && oldItem.message.equals(newItem.message)
                    ;
        }
    }

}
