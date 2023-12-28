package com.example.quickcode.rest.verify;

public class VerifyError implements VerifyStatus {
    private Exception execption;

    public VerifyError(Exception e) {
        this.execption = e;
    }

    public Exception getException() {
        return execption;
    }
}
