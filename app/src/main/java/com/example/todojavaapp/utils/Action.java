package com.example.todojavaapp.utils;

/**
 * Represents a single Action to perform.
 *
 * This is a convenience interface, and general use looks like:
 *
 * myThing(() -> blah());
 *
 * As opposed to handing in an actual subclass of Action.
 */
public interface Action {
    void invoke();
}
