package com.example.quickcode.common.validator;

import android.text.TextUtils;

import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;

public class IsEmptyValidator implements Validator {

    private final String text;

    public IsEmptyValidator(String text) {
        this.text = text;
    }

    @Override
    public ValidatorResult validate() {
        if (TextUtils.isEmpty(text)) {
            return new ValidatorResult.Error(
                    new DeferredText.Resource(R.string.sign_up_page_label_error_field_required),
                    ErrorType.FIELD_REQUIRED);
        } else {
            return new ValidatorResult.Success();
        }
    }

}
