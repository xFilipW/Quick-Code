package com.example.quickcode.common.validator;

import android.content.Context;

import com.example.quickcode.common.deferred.DeferredText;

public interface ValidatorResult {

    class Error implements ValidatorResult {
        private final DeferredText mReason;

        public Error(DeferredText reason) {
            mReason = reason;
        }

        public String getReason(Context context) {
            return mReason.getText(context);
        }
    }

    class Success implements ValidatorResult {
    }

}
