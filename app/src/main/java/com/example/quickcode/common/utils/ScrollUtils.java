package com.example.quickcode.common.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.quickcode.loginRegister.ViewProvider;

import java.util.HashMap;

public class ScrollUtils {

    public static final HashMap<View, ViewTreeObserver.OnGlobalLayoutListener> globalLayoutListenerMap = new HashMap<>();
    public static final HashMap<View, Long> timeMap = new HashMap<>();
    private static final String TAG = "ScrollUtils";

    public static void smartScrollTo(ViewProvider viewProvider, String functionName) {
        View provide = viewProvider.provide();

        Long oldTime = timeMap.get(provide);
        if (oldTime == null) {
            Log.d(TAG, "smartScrollTo putting new view: " + provide);
            timeMap.put(provide, System.currentTimeMillis());
        } else {
            Long newTime = System.currentTimeMillis();
            long diffTime = newTime - oldTime;

            // reset time
            timeMap.put(provide, System.currentTimeMillis());

            // prevent double call whole function if time is < 100ms
            int thresholdDelay = 100;

            if (diffTime < thresholdDelay) {
                Log.d(TAG, "smartScrollTo diffTime: " + diffTime + "ms, way too fast!");
                return;
            } else {
                Log.d(TAG, "smartScrollTo diffTime: " + diffTime + "ms, acceptable delay span");
            }
        }

        if (globalLayoutListenerMap.get(provide) == null) {
            ViewTreeObserver.OnGlobalLayoutListener xxx = () -> {
                Log.d(TAG, "smartScrollTo() request loop functionName = [" + functionName + "]");

                Rect rect = new Rect();
                provide.getDrawingRect(rect);
                provide.requestRectangleOnScreen(rect);
            };

            globalLayoutListenerMap.put(provide, xxx);
            provide.getViewTreeObserver().addOnGlobalLayoutListener(xxx);
        }

        provide.requestLayout();
    }

    /**
     * This method should be called for every view that meets all the following conditions:<br>
     * 1. It has been attached using the {@link #smartScrollTo(ViewProvider, String)} function.<br>
     * 2. The view has lost focus.
     */
    public static void cleanup() {
        removeOnGlobalLayoutListeners();

        globalLayoutListenerMap.clear();
        timeMap.clear();
    }

    private static void removeOnGlobalLayoutListeners() {
        for (View view : globalLayoutListenerMap.keySet()) {
            ViewTreeObserver.OnGlobalLayoutListener listener = globalLayoutListenerMap.get(view);
            if (listener != null) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            }
        }
    }
}
