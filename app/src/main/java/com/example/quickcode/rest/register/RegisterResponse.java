package com.example.quickcode.rest.register;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("user_id")
    public long user_id;

    @SerializedName("lifetime_minutes")
    public int lifetime_minutes;

    @SerializedName("error_code")
    public int error_code;
}
