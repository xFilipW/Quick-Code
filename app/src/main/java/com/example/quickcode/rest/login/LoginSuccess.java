package com.example.quickcode.rest.login;

public class LoginSuccess implements LoginStatus {
    private final LoginResponse response;

    public LoginSuccess(LoginResponse loginResponse) {
        this.response = loginResponse;
    }

    public LoginResponse getResponse() {
        return response;
    }
}
