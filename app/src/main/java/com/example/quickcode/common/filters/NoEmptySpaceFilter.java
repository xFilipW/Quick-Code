package com.example.quickcode.common.filters;

import android.text.InputFilter;
import android.text.Spanned;

public class NoEmptySpaceFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean containsWhitespace = false;

        for (int i = start; i < end; i++) {
            if (Character.isWhitespace(source.charAt(i))) {
                containsWhitespace = true;
                break;
            }
        }

        if (containsWhitespace) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (!Character.isWhitespace(source.charAt(i))) {
                    stringBuilder.append(source.charAt(i));
                }
            }
            return stringBuilder;
        }

        return null;
    }
}
