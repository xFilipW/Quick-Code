package com.example.quickcode.rest.register;

public class RegisterFailure implements RegisterStatus {
    private final int error;

    public RegisterFailure(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
