package com.example.quickcode.rest.register;

public class RegisterError implements RegisterStatus {
    private Exception execption;

    public RegisterError(Exception e) {
        this.execption = e;
    }

    public Exception getException() {
        return execption;
    }
}
