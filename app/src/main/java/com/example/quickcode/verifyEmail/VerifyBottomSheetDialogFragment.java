package com.example.quickcode.verifyEmail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickcode.Consts;
import com.example.quickcode.R;
import com.example.quickcode.common.utils.TimeUtils;
import com.example.quickcode.databinding.ActivityVerifyEmailBinding;
import com.example.quickcode.loginRegister.CircleStatusListener;
import com.example.quickcode.rest.register.RegisterError;
import com.example.quickcode.rest.register.RegisterFailure;
import com.example.quickcode.rest.register.RegisterSuccess;
import com.example.quickcode.rest.verify.VerifyError;
import com.example.quickcode.rest.verify.VerifyFailure;
import com.example.quickcode.rest.verify.VerifyListener;
import com.example.quickcode.rest.verify.VerifyStatus;
import com.example.quickcode.rest.verify.VerifySuccess;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ForgotBottomSheetDialog";

    private ActivityVerifyEmailBinding binding;
    private VerifyViewModel viewModel;

    private Handler handler;
    private long futureTime = Integer.MIN_VALUE;
    private boolean countdownEnabled = true;

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

        handler = new Handler(Looper.getMainLooper());

        viewModel = new ViewModelProvider(this).get(VerifyViewModel.class);

        updateGreetingsText(viewModel.getDisplayUsername(requireContext()));

        viewModel.setupToolbarBehaviour(binding, this::dismiss);
        viewModel.setTouchListeners(binding);
        viewModel.setFocusListeners(binding);

        setOnClickListeners(binding);
    }

    private void updateGreetingsText(String displayUsername) {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);
            fragmentBinding.textView1.setText(
                    String.format(Locale.getDefault(),
                            getString(R.string.verify_email_heading_text),
                            displayUsername)
            );
        }
    }

    public void setOnClickListeners(ActivityVerifyEmailBinding binding) {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);
            fragmentBinding.verifyEmailButton.setOnClickListener(v -> {

                handler.post(() -> showCircle());
                viewModel.verifyUser(
                        viewModel.getUserId(requireContext()),
                        String.valueOf(fragmentBinding.pinView.getText()),
                        (VerifyListener) verifyStatus -> {
                            if (verifyStatus instanceof VerifySuccess) {
                                handler.post(() -> hideCircle());
                                showLottieResult(verifyStatus);
                            } else if (verifyStatus instanceof VerifyFailure) {
                                Log.e(TAG, "Error: " + ((VerifyFailure) verifyStatus).getError());
                                handler.post(() -> hideCircle());
                            } else {
                                Log.wtf(TAG, "Exception: " + ((VerifyError) verifyStatus).getException().getMessage());
                                handler.post(() -> hideCircle());
                            }
                        }
                );
            });
        } else {
            // TODO: 10.10.2023
        }
    }

    private void showLottieResult(VerifyStatus verifyStatus) {
        if (verifyStatus instanceof VerifySuccess) {
            View nextView = binding.viewSwitcher.getNextView();
            FragmentVerifySuccessBinding fragmentBinding = FragmentVerifySuccessBinding.bind(nextView);

            binding.viewSwitcher.showNext();
            fragmentBinding.lottie.playAnimation();
            binding.timer.setVisibility(View.GONE);
        } else {
            // TODO: 10.10.2023 not happy path
        }
    }

    public void showCircle() {
        binding.progressVerify.setVisibility(View.VISIBLE);
        requireActivity().getWindow().setStatusBarColor(Color.parseColor("#326577"));
    }

    public void hideCircle() {
        binding.progressVerify.setVisibility(View.GONE);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.lightBlue));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(onShowDialog -> {
            viewModel.configureDialogOnShow((BottomSheetDialog) onShowDialog);
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            viewModel.setBottomSheetDimDisabled(dialog);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler = null;
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTextLifetime();
    }

    private void updateTextLifetime() {
        if (countdownEnabled == false)
            return;

        String lifetime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(Consts.SP_LIFETIME, null);
        if (lifetime == null)
            return;

        Date date = TimeUtils.formatDate(lifetime);
        if (date == null)
            return;

        futureTime = date.getTime();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long l = futureTime - currentTime;
                long seconds = l / 1000;

                if (seconds < -1) {
                    handler.removeCallbacksAndMessages(null);
                    return;
                }

                binding.timer.setText(String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60));

                handler.postDelayed(this, 100);
            }
        };

        handler.postDelayed(runnable, 100);
    }

    private void stopTextLifeTime() {
        countdownEnabled = false;
        binding.timer.setVisibility(View.GONE);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

}
