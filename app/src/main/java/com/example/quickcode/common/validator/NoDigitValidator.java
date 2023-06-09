package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class NoDigitValidator implements Validator {

    private final String text;
    private final String numbers = "(.*[0-9].*)";

    public NoDigitValidator(String text) {
        this.text = text;
    }

    @Override
    public ValidatorResult validate() {
        if (text.matches(numbers)) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_digits_are_not_allowed),
                    ErrorType.DIGITS_ARE_NOT_ALLOWED);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
