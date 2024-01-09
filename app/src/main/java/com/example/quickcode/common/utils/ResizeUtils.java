package com.example.quickcode.common.utils;

import android.view.View;
import android.view.ViewGroup;

public class ResizeUtils {
    public static void resizeView(int pixelHeight, View view) {
        if (pixelHeight == 0) {
            AnimateUtils.smoothResizeView(view, view.getHeight(), pixelHeight);
        } else {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = pixelHeight;

            view.setLayoutParams(layoutParams);
        }
    }
}
