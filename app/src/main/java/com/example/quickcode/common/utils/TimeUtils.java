package com.example.quickcode.common.utils;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static Date formatDate(@NonNull String lifetime) {
        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            date = sdf.parse(lifetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
