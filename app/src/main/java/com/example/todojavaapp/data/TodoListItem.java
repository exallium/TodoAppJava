package com.example.todojavaapp.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * TodoListItem is an unmodifiable database entity.
 * To 'modify', we utilize a copy builder, via buildUpon.
 * Then, it's up to you to save it to the database, which will replace
 * the previous copy and notify any listeners.
 */
@Entity
public final class TodoListItem {

    @PrimaryKey(autoGenerate = true)
    public final long id;

    @ColumnInfo(name = "is_checked")
    public final boolean isChecked;

    public final String message;

    public TodoListItem(long id, boolean isChecked, String message) {
        this.id = id;
        this.isChecked = isChecked;
        this.message = message;
    }

    public Builder buildUpon() {
        return new Builder(this);
    }

    @NonNull
    @Override
    public String toString() {
        return "[id:" +
                id +
                ",msg:" +
                message +
                ",check:" +
                isChecked +
                "]";
    }

    /**
     * Creates a TodoListItem with default values set.
     */
    public static TodoListItem createDefault() {
        return new Builder().build();
    }

    /**
     * TodoListItem Builder which utilizes sane defaults
     */
    public static class Builder {

        private static final long DEFAULT_ID = 0;
        private static final boolean DEFAULT_CHECKED = false;
        private static final String DEFAULT_MESSAGE = "";
        private static final TodoListItem DEFAULT_TODO = new TodoListItem(
                DEFAULT_ID,
                DEFAULT_CHECKED,
                DEFAULT_MESSAGE
        );

        private final TodoListItem from;

        private Boolean isChecked;
        private String message;
        private Long id;

        public Builder() {
            this(DEFAULT_TODO);
        }

        public Builder(TodoListItem from) {
            this.from = from;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder isChecked(boolean isChecked) {
            this.isChecked = isChecked;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public TodoListItem build() {
            return new TodoListItem(
                    resolveValue(id, from.id),
                    resolveValue(isChecked, from.isChecked),
                    resolveValue(message, from.message)
            );
        }

        private <A> A resolveValue(@Nullable A setByUser, @Nullable A setInFrom) {
            if (setByUser != null) return setByUser;
            return setInFrom;
        }

    }

}
