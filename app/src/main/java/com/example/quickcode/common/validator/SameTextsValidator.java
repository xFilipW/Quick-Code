package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class SameTextsValidator implements Validator {

    private final String password;
    private final String confirmPassword;

    public SameTextsValidator(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @Override
    public ValidatorResult validate() {
        if (!password.equals(confirmPassword)) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_passwords_are_not_the_same),
                    ErrorType.PASSWORDS_ARE_NOT_THE_SAME);
        } else {
            return new ValidatorResult.Success();
        }
    }
}
