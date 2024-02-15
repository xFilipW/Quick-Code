package com.example.quickcode.rest.register;

public class RegisterSuccess implements RegisterStatus {
    private final RegisterResponse response;

    public RegisterSuccess(RegisterResponse registerResponse) {
        this.response = registerResponse;
    }

    public RegisterResponse getResponse() {
        return response;
    }
}
