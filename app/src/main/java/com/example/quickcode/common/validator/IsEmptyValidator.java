package com.example.quickcode.common.validator;

import android.text.TextUtils;

import com.example.quickcode.common.deferred.DeferredText;

public class IsEmptyValidator implements Validator {

    private final String text;
    private final DeferredText reason;

    public IsEmptyValidator(String text, DeferredText reason) {
        this.text = text;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (TextUtils.isEmpty(text)) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
