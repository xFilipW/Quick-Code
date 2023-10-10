package com.example.quickcode.rest.verify;

import com.google.gson.annotations.SerializedName;

public class VerifyResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("error_code")
    public int error_code;
}
