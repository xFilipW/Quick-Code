package com.example.quickcode.testingtools;

import android.util.Log;
import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.concurrent.TimeoutException;

public class WaitUntilVisibleAction implements ViewAction {

    private final long timeout;

    public WaitUntilVisibleAction(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public Matcher<View> getConstraints() {
        return Matchers.any(View.class);
    }

    @Override
    public void perform(UiController uiController, View view) {
        long endTime = System.currentTimeMillis() + timeout;

        do {
            if (view.getVisibility() == View.VISIBLE) {
                Log.i(TAG, "perform: view luckily found");
                return;
            }
            Log.w(TAG, "perform: view not found yet");
            uiController.loopMainThreadForAtLeast(50);
        } while (System.currentTimeMillis() < endTime);

        throw new PerformException.Builder()
                .withActionDescription(getDescription())
                .withCause(new TimeoutException(String.format("Waited %d milliseconds", timeout)))
                .withViewDescription(HumanReadables.describe(view))
                .build();
    }

    @Override
    public String getDescription() {
        return String.format("wait up to %d milliseconds for the view to become visible", timeout);
    }

    private static final String TAG = "WaitUntilVisibleAction";
}
