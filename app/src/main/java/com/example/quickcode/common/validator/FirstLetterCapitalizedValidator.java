package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class FirstLetterCapitalizedValidator implements Validator {

    private final String text;
    private final DeferredText reason;

    public FirstLetterCapitalizedValidator(String text, DeferredText reason) {
        this.text = text;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (!Character.isUpperCase(text.charAt(0))) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
