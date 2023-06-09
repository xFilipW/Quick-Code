package com.example.quickcode.testingtools;

import android.view.View;

import androidx.test.espresso.IdlingResource;

public class ViewIdlingResource implements IdlingResource {

    private final String resourceName;
    private final long startTime;
    private final long waitingTime;
    private final View view;
    private ResourceCallback resourceCallback;

    public ViewIdlingResource(String resourceName, long waitingTime, View view) {
        this.resourceName = resourceName;
        this.startTime = System.currentTimeMillis();
        this.waitingTime = waitingTime;
        this.view = view;
    }

    @Override
    public String getName() {
        return resourceName;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = System.currentTimeMillis() - startTime >= waitingTime && view != null;
        if (idle) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
