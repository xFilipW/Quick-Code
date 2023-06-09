package com.example.quickcode.common.helpers;

import android.text.InputFilter;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputFilterHelper {

    public static void addFilter(TextInputEditText inputEditText, InputFilter inputFilter) {
        List<InputFilter> inputFilters = new ArrayList<>(Arrays.asList(inputEditText.getFilters()));
        inputFilters.add(inputFilter);
        InputFilter[] updatedInputFilterArray = inputFilters.toArray(new InputFilter[0]);
        inputEditText.setFilters(updatedInputFilterArray);
    }
}
