package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class ContainsDigit implements Validator {

    private final String password;
    private final DeferredText reason;
    private final String numbers = "(.*[0-9].*)";

    public ContainsDigit(String password, DeferredText reason) {
        this.password = password;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.matches(numbers)) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
