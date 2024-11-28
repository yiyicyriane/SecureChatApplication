package com.chat.util;

public class CurrentViewContext {
    private static CurrentViewContext instance;
    private Object currentView;

    private CurrentViewContext() {}

    public static synchronized CurrentViewContext getInstance() {
        if (instance == null) {
            instance = new CurrentViewContext();
        }
        return instance;
    }

    public Object getCurrentView() {
        return currentView;
    }

    public void setCurrentView(Object currentView) {
        this.currentView = currentView;
    }
}
