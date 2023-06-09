package com.example.quickcode.common.validator;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoSpecialCharValidator implements Validator {

    private final String password;
    private final String specialChars = "[\\W+]";

    public NoSpecialCharValidator(String password) {
        this.password = password;
    }

    @Override
    public ValidatorResult validate() {
        Pattern correctEmailPattern = Pattern.compile(specialChars, Pattern.CASE_INSENSITIVE);
        Matcher matcher = correctEmailPattern.matcher(password);
        if (matcher.find()) {
            return new ValidatorResult.Error(new DeferredText.Resource(R.string.sign_up_page_label_error_special_chars_are_not_allowed),
                    ErrorType.SPECIAL_CHARS_ARE_NOT_ALLOWED);
        } else {
            return new ValidatorResult.Success();
        }
    }

}