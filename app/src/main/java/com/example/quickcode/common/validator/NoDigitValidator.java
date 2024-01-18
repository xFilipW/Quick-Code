package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class NoDigitValidator implements Validator {

    private final String text;
    private final DeferredText reason;
    private final String numbers = "(.*[0-9].*)";

    public NoDigitValidator(String text, DeferredText reason) {
        this.text = text;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (text.matches(numbers)) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
