package com.example.quickcode.common.validator;

import android.content.Context;

import com.example.quickcode.common.deferred.DeferredText;

public interface ValidatorResult {

    class Error implements ValidatorResult {
        private final DeferredText mReason;
        private final ErrorType mErrorType;

        public Error(DeferredText reason, ErrorType errorType) {
            mReason = reason;
            mErrorType = errorType;
        }

        public String getReason(Context context) {
            return mReason.getText(context);
        }

        public ErrorType getErrorType() {
            return mErrorType;
        }
    }

    class Success implements ValidatorResult {
    }

}
