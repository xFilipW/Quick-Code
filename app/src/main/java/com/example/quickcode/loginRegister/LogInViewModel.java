package com.example.quickcode.loginRegister;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.example.quickcode.common.validator.Validator;
import com.example.quickcode.common.validator.ValidatorHelper;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.rest.QuickCodeClient;
import com.example.quickcode.rest.login.LoginError;
import com.example.quickcode.rest.login.LoginFailure;
import com.example.quickcode.rest.login.LoginListener;
import com.example.quickcode.rest.login.LoginResponse;
import com.example.quickcode.rest.login.LoginSuccess;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class LogInViewModel extends ViewModel {
    public ValidatorResult validateAndGetResult(TextInputLayout textInputLayout, List<Validator> validatorList) {
        ValidatorResult validate = ValidatorHelper.validate(validatorList);
        if (validate instanceof ValidatorResult.Error) {
            textInputLayout.setError(((ValidatorResult.Error) validate).getReason(textInputLayout.getContext()));
        } else {
            textInputLayout.setError(null);
        }
        return validate;
    }

    public void loginUser(String email, String password, LoginListener loginListener) {
        QuickCodeClient instance = QuickCodeClient.getInstance();

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Call<LoginResponse> login = instance.login(email, password);
            try {
                Response<LoginResponse> execute = login.execute();
                LoginResponse body = execute.body();

                if (execute.isSuccessful()) {
                    handler.post(() -> loginListener.onLogin(new LoginSuccess(body)));
                } else {
                    try {
                        JSONObject jsonObjectError = new JSONObject(execute.errorBody().string());
                        int errorCode = jsonObjectError.optInt("error_code");

                        handler.post(() ->
                                loginListener.onLogin(new LoginFailure(errorCode))
                        );

                    } catch (JSONException e) {
                        handler.post(() -> {
                            loginListener.onLogin(new LoginError(e));
                            e.printStackTrace();
                        });
                    }
                }
            } catch (Exception e) {
                handler.post(() -> {
                            loginListener.onLogin(new LoginError(e));
                            e.printStackTrace();
                        }
                );
            }
        });
    }
}
