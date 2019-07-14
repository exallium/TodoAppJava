package com.example.todojavaapp.utils;

/**
 * Constants for View Presentation. These are considered to be at the same logical
 * layer as ViewModels, and are used to help control lower level view properties (like focus)
 * in a way that allows us to not have to utilize boolean flags or whatnot.
 */
public class PresentationConstants {

    /**
     * Constant when no view should be focused
     */
    public static final int NO_FOCUS = -1;

    private PresentationConstants() {}
}
