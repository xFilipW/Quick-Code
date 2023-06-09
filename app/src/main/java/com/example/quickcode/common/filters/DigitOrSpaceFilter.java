package com.example.quickcode.common.filters;

import android.text.InputFilter;
import android.text.Spanned;

public class DigitOrSpaceFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(source.charAt(i)) && source.charAt(i) != " ".charAt(0)) {
                return "";
            }
        }
        return null;
    }
}
