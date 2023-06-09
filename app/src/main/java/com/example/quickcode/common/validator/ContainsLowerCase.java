package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class ContainsLowerCase implements Validator {

    private final String password;
    private final String lowerCase = "(.*[a-z].*)";

    public ContainsLowerCase(String password) {
        this.password = password;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.matches(lowerCase)) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_password_must_contains_lower_case),
                    ErrorType.PASSWORD_MUST_CONTAINS_LOWER_CASE);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
