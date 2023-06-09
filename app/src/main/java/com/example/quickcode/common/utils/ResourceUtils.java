package com.example.quickcode.common.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

public class ResourceUtils {
    public static int getColorAttribute(Context context, int referenceColor) {
        TypedValue typedValue = new TypedValue();
        int primaryColor;
        int[] attrs = {referenceColor};

        TypedArray arr = context.obtainStyledAttributes(typedValue.data, attrs);
        primaryColor = arr.getResourceId(0, -1);
        arr.recycle();

        return primaryColor;
    }
}
