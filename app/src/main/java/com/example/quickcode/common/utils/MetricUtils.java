package com.example.quickcode.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class MetricUtils {

    public static int toDp(Context context, int px) {
        DisplayMetrics metrics = context
                .getResources()
                .getDisplayMetrics();

        return (int) (px * metrics.density);
    }

}
