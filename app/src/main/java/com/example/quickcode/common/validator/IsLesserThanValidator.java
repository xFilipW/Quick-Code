package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class IsLesserThanValidator implements Validator {

    private final String text;
    private final int lesserThan;
    private final DeferredText reason;

    public IsLesserThanValidator(String text, int lesserThan, DeferredText reason) {
        this.text = text;
        this.lesserThan = lesserThan;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (text.length() < lesserThan) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
