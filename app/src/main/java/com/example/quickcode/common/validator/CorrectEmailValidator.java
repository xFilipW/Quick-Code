package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorrectEmailValidator implements Validator {

    private final String correctEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private final String text;

    public CorrectEmailValidator(String text) {
        this.text = text;
    }

    @Override
    public ValidatorResult validate() {
        Pattern correctEmailPattern = Pattern.compile(correctEmail, Pattern.CASE_INSENSITIVE);
        Matcher matcher = correctEmailPattern.matcher(text);
        if (!matcher.matches()) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_incorrect_email),
                    ErrorType.INCORRECT_EMAIL);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
