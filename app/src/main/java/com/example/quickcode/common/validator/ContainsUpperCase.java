package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class ContainsUpperCase implements Validator {

    private final String password;
    private final String upperCase = "(.*[A-Z].*)";

    public ContainsUpperCase(String password) {
        this.password = password;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.matches(upperCase)) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_password_must_contains_upper_case),
                    ErrorType.PASSWORD_MUST_CONTAINS_UPPER_CASE);
        } else {
            return new ValidatorResult.Success();
        }
    }

}