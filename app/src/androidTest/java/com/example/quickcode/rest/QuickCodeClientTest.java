package com.example.quickcode.rest;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.quickcode.rest.register.RegisterResponse;
import com.example.quickcode.rest.verify.VerifyResponse;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Response;

public class QuickCodeClientTest {

    private static final String TAG = "QuickCodeClientTest";

    @Test
    public void shouldRegisterWithSuccess() throws InterruptedException {
        QuickCodeClient instance = QuickCodeClient.getInstance();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        Boolean[] success = new Boolean[]{false};

        executor.execute(() -> {
            Call<RegisterResponse> register = instance.register("filip", "dsadsa", "filip@niepodam.pl");
            try {
                Response<RegisterResponse> execute = register.execute();
                if (execute.isSuccessful()) {
                    success[0] = true;
                } else {
                    success[0] = false;
                }
            } catch (IOException e) {
                Log.e(TAG, "shouldRegisterWithSuccess: " + e.getMessage());
                e.printStackTrace();
                success[0] = false;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(true, success[0]);
    }

    @Test
    public void shouldVerifyWithSuccess() throws InterruptedException {
        QuickCodeClient instance = QuickCodeClient.getInstance();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        Boolean[] success = new Boolean[]{false};

        executor.execute(() -> {
            Call<VerifyResponse> verify = instance.verify(163, "9982");
            try {
                Response<VerifyResponse> execute = verify.execute();
                if (execute.isSuccessful()) {
                    success[0] = true;
                } else {
                    success[0] = false;
                }
            } catch (IOException e) {
                Log.e(TAG, "shouldVerifyWithSuccess: " + e.getMessage());
                e.printStackTrace();
                success[0] = false;
            } finally {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(true, success[0]);
    }
}