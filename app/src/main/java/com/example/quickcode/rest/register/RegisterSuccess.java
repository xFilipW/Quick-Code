package com.example.quickcode.rest.register;

public class RegisterSuccess implements RegisterStatus {
    private RegisterResponse response;

    public RegisterSuccess(RegisterResponse registerResponse) {
        this.response = registerResponse;
    }

    public RegisterResponse getResponse() {
        return response;
    }
}
