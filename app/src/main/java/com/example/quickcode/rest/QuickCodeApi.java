package com.example.quickcode.rest;

import com.example.quickcode.rest.login.LoginResponse;
import com.example.quickcode.rest.register.RegisterResponse;
import com.example.quickcode.rest.verify.VerifyResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface QuickCodeApi {

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(
            @Field("first_name") String first_name,
            @Field("password") String password,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("verify.php")
    Call<VerifyResponse> verify(
            @Field("user_id") long user_id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

}
