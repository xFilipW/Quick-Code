package com.example.quickcode.rest;

import com.example.quickcode.rest.register.RegisterResponse;
import com.example.quickcode.rest.verify.VerifyResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuickCodeClient {

    private static QuickCodeClient INSTANCE;
    private final QuickCodeApi api;
    private String info = "Initial info class";

    private QuickCodeClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/Quick-Code-API/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(QuickCodeApi.class);
    }

    public static QuickCodeClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuickCodeClient();
        }

        return INSTANCE;
    }

    public Call<RegisterResponse> register(String firstName, String password, String email) {
        return api.register(firstName, password, email);
    }

    public Call<VerifyResponse> verify(long user_id, String token) {
        return api.verify(user_id, token);
    }

}
