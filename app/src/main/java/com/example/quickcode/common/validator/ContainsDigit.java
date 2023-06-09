package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class ContainsDigit implements Validator {

    private final String password;
    private final String numbers = "(.*[0-9].*)";

    public ContainsDigit(String password) {
        this.password = password;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.matches(numbers)) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_password_must_contains_digit),
                    ErrorType.PASSWORD_MUST_CONTAINS_DIGIT);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
