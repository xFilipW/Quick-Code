package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class SameTextsValidator implements Validator {

    private final String password;
    private final String confirmPassword;
    private final DeferredText reason;

    public SameTextsValidator(String password, String confirmPassword, DeferredText reason) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.equals(confirmPassword)) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }
}
