package com.example.quickcode.common.validator;

import com.example.quickcode.common.deferred.DeferredText;

public class MobileNumberLengthValidator implements Validator {

    private final int mLength;
    private final String text;

    public MobileNumberLengthValidator(String text, int length) {
        mLength = length;
        this.text = text;
    }

    @Override
    public ValidatorResult validate() {
        if (text.length() < mLength) {
            return new ValidatorResult.Error(new DeferredText.Const("Phone number must be " + mLength + " digits long"),
                    ErrorType.PHONE_NUMBER_IS_TOO_SHORT);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
