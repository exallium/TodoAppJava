package com.example.todojavaapp.di;

/**
 * Injector serves as a one-stop shop to get or set the TodoAppComponent instance.
 * This instance should be set exactly once from the TodoApp class.
 */
public enum Injector {
    INSTANCE;

    private TodoAppComponent component;

    public void setComponent(TodoAppComponent component) {
        this.component = component;
    }

    public TodoAppComponent getComponent() {
        return component;
    }
}
