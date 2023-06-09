package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class FirstLetterCapitalizedValidator implements Validator {

    private final String text;

    public FirstLetterCapitalizedValidator(String text) {
        this.text = text;
    }

    @Override
    public ValidatorResult validate() {
        if (!Character.isUpperCase(text.charAt(0))) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_first_letter_must_be_capitalized),
                    ErrorType.FIRST_LETTER_MUST_BE_CAPITALIZED);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
