package com.example.quickcode.rest.verify;

public class VerifySuccess implements VerifyStatus {
    private final VerifyResponse response;

    public VerifySuccess(VerifyResponse registerResponse) {
        this.response = registerResponse;
    }

    public VerifyResponse getResponse() {
        return response;
    }
}
