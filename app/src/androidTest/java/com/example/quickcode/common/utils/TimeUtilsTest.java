package com.example.quickcode.common.utils;

import static org.junit.Assert.assertNotNull;

import android.util.Log;

import org.junit.Test;

import java.util.Date;

public class TimeUtilsTest {

    private static final String TAG = "TimeUtilsTest";

    @Test
    public void formatDate() {
        Date date = TimeUtils.formatDate("2023-07-11 19:14:02");
        Log.d(TAG, "formatDate: " + date.getTime());
        assertNotNull(date);
    }
}