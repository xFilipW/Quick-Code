package com.example.quickcode.verifyEmail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickcode.databinding.ActivityVerifyEmailBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ForgotBottomSheetDialog";

    private ActivityVerifyEmailBinding binding;
    private VerifyViewModel model;

    private final Handler handler = new Handler();
    private long futureTime = Integer.MIN_VALUE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityVerifyEmailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(VerifyViewModel.class);

        model.setupToolbarBehaviour(binding, this::dismiss);
        model.setTouchListeners(binding);
        model.setFocusListeners(binding);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(onShowDialog -> {
            model.configureDialogOnShow((BottomSheetDialog) onShowDialog);
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            model.setBottomSheetDimDisabled(dialog);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (futureTime == Integer.MIN_VALUE)
                    futureTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);

                long currentTime = System.currentTimeMillis();
                long l = futureTime - currentTime;
                long seconds = l / 1000;

                if (seconds < -1) {
                    handler.removeCallbacksAndMessages(null);
                }

                binding.timer.setText(String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60));

                handler.postDelayed(this, 100);
            }
        };

        handler.postDelayed(runnable, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
