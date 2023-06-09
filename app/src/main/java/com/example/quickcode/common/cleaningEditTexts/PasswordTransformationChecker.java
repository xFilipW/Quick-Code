package com.example.quickcode.common.cleaningEditTexts;

import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;

public class PasswordTransformationChecker {
    private boolean isPasswordTransformationMethod;

    public PasswordTransformationChecker(TransformationMethod transformationMethod) {
        updatePasswordTransformationMethod(transformationMethod);
    }

    public void updatePasswordTransformationMethod(TransformationMethod transformationMethod) {
        this.isPasswordTransformationMethod = isPasswordTransformationMethod(transformationMethod);
    }

    private boolean isPasswordTransformationMethod(TransformationMethod transformationMethod) {
        return transformationMethod instanceof PasswordTransformationMethod;
    }

    public boolean hasTransformationMethodChanged(TransformationMethod newTransformationMethod) {
        boolean passwordTransformationMethod = isPasswordTransformationMethod(newTransformationMethod);
        return isBoolDifferent(passwordTransformationMethod);
    }

    private boolean isBoolDifferent(boolean isPasswordTransformationMethod) {
        return this.isPasswordTransformationMethod != isPasswordTransformationMethod;
    }
}