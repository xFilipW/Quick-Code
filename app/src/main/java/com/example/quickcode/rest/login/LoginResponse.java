package com.example.quickcode.rest.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("id")
    public long id;

    @SerializedName("first_name")
    public String first_name;

    @SerializedName("password")
    public String password;

    @SerializedName("email")
    public String email;

    @SerializedName("verified")
    public int verified;

    @SerializedName("error_code")
    public int error_code;
}
