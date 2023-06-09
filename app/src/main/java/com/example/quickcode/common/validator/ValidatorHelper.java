package com.example.quickcode.common.validator;

import java.util.List;

public class ValidatorHelper {

    public static ValidatorResult validate(List<Validator> validators) {
        for (Validator validator : validators) {
            ValidatorResult validatorResult = validator.validate();
            if (validatorResult instanceof ValidatorResult.Error) {
                return validatorResult;
            }
        }
        return new ValidatorResult.Success();
    }

}
