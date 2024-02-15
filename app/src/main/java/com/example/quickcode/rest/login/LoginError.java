package com.example.quickcode.rest.login;


public class LoginError implements LoginStatus {
    private final Exception execption;

    public LoginError(Exception e) {
        this.execption = e;
    }

    public Exception getException() {
        return execption;
    }
}
