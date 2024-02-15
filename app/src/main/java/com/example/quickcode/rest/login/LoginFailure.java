package com.example.quickcode.rest.login;

public class LoginFailure implements LoginStatus {
    private final int error;

    public LoginFailure(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
