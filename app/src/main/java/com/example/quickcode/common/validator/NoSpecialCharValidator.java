package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoSpecialCharValidator implements Validator {

    private final String password;
    private final DeferredText reason;
    private final String specialChars = "[\\W+]";

    public NoSpecialCharValidator(String password, DeferredText reason) {
        this.password = password;
        this.reason = reason;
    }

    @Override
    public ValidatorResult validate() {
        Pattern correctEmailPattern = Pattern.compile(specialChars, Pattern.CASE_INSENSITIVE);
        Matcher matcher = correctEmailPattern.matcher(password);
        if (matcher.find()) {
            return new ValidatorResult.Error(reason);
        } else {
            return new ValidatorResult.Success();
        }
    }

}