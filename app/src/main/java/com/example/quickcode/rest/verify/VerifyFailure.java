package com.example.quickcode.rest.verify;

public class VerifyFailure implements VerifyStatus {
    private final int error;

    public VerifyFailure(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
