package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class ContainsLowerCase implements Validator {

    private final String password;
    private final DeferredText reason;
    private final String lowerCase = "(.*[a-z].*)";

    public ContainsLowerCase(String password, DeferredText reason) {
        this.password = password;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.matches(lowerCase)) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
