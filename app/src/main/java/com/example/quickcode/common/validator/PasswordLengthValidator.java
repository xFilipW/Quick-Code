package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class PasswordLengthValidator implements Validator {

    private final String text;
    private final int minLength;

    public PasswordLengthValidator(String text, int minLength) {
        this.text = text;
        this.minLength = minLength;
    }

    @Override
    public ValidatorResult validate() {
        if (text.length() < minLength) {
            return new ValidatorResult.Error(new DeferredText.Const("Minimum " + minLength + " characters"),
                    ErrorType.PASSWORD_LENGTH_IS_TOO_SHORT);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
