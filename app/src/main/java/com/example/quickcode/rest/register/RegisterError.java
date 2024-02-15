package com.example.quickcode.rest.register;

public class RegisterError implements RegisterStatus {
    private final Exception execption;

    public RegisterError(Exception e) {
        this.execption = e;
    }

    public Exception getException() {
        return execption;
    }
}
