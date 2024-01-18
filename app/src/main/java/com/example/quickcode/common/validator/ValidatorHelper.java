package com.example.quickcode.common.validator;

import android.util.Log;

import java.util.List;

public class ValidatorHelper {

    private static final String TAG = "ValidatorHelper";

    public static ValidatorResult validate(List<Validator> validators) {
        for (Validator validator : validators) {
            ValidatorResult validatorResult = validator.validate();
            if (validatorResult instanceof ValidatorResult.Error) {
                Log.e(TAG, "validatator failed = [" + validator + "]");
                return validatorResult;
            }
        }
        return new ValidatorResult.Success();
    }

}
