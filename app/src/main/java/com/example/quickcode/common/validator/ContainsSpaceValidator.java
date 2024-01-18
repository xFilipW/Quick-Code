package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class ContainsSpaceValidator implements Validator {

    private final String text;
    private final DeferredText reason;

    public ContainsSpaceValidator(String text, DeferredText reason) {
        this.text = text;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (text.contains(" ")) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
