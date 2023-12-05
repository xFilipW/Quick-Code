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
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickcode.consts.Consts;
import com.example.quickcode.R;
import com.example.quickcode.common.utils.TimeUtils;
import com.example.quickcode.databinding.ActivityVerifyEmailBinding;
import com.example.quickcode.databinding.FragmentVerifyPinviewBinding;
import com.example.quickcode.databinding.FragmentVerifySuccessBinding;
import com.example.quickcode.loginRegister.CircleStatusListener;
import com.example.quickcode.loginRegister.SignupSharedViewModel;
import com.example.quickcode.loginRegister.SwipeControlListener;
import com.example.quickcode.rest.verify.VerifyError;
import com.example.quickcode.rest.verify.VerifyFailure;
import com.example.quickcode.rest.verify.VerifyStatus;
import com.example.quickcode.rest.verify.VerifySuccess;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Date;
import java.util.Locale;

public class VerifyBottomSheetDialogFragment extends BottomSheetDialogFragment implements CircleStatusListener {

    public static final String TAG = "ForgotBottomSheetDialog";

    private ActivityVerifyEmailBinding binding;
    private VerifyViewModel viewModel;

    private Handler handler;
    private long futureTime = Integer.MIN_VALUE;
    private boolean countdownEnabled = true;
    private SignupSharedViewModel signupSharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityVerifyEmailBinding.inflate(inflater, container, false);
        Window window = getDialog().getWindow();
        if(window != null) {
            window.setStatusBarColor(Color.parseColor("#65CBEF"));
        }
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler(Looper.getMainLooper());

        viewModel = new ViewModelProvider(this).get(VerifyViewModel.class);
        signupSharedViewModel = new ViewModelProvider(requireActivity()).get(SignupSharedViewModel.class);

        updateGreetingsText(viewModel.getDisplayUsername(requireContext()));

        viewModel.setupToolbarBehaviour(binding, this::dismiss);
        viewModel.setTouchListeners(binding);
        viewModel.setFocusListeners(binding);

        setOnClickListeners(binding);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);

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

                handler.post(this::showCircle);
                viewModel.verifyUser(
                        viewModel.getUserId(requireContext()),
                        String.valueOf(fragmentBinding.pinView.getText()),
                        verifyStatus -> {
                            if (verifyStatus instanceof VerifySuccess) {
                                handler.post(this::hideCircle);
                                showLottieResult(verifyStatus);
                            } else if (verifyStatus instanceof VerifyFailure) {
                                Log.e(TAG, "Error: " + ((VerifyFailure) verifyStatus).getError());
                                handler.post(this::hideCircle);
                            } else {
                                Log.wtf(TAG, "Exception: " + ((VerifyError) verifyStatus).getException().getMessage());
                                handler.post(this::hideCircle);
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

    @Override
    public void showCircle() {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);
            fragmentBinding.progressRegister.setVisibility(View.VISIBLE);
            ((SwipeControlListener) requireActivity()).disableViewpagerSwiping();
        }
    }

    @Override
    public void hideCircle() {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);
            fragmentBinding.progressRegister.setVisibility(View.GONE);
            ((SwipeControlListener) requireActivity()).enableViewpagerSwiping();
        }
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                signupSharedViewModel.hideCircle();
            }
        }, 1000);
    }

    private void updateTextLifetime() {
        if (!countdownEnabled)
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

                if (seconds < 0) {
                    handler.removeCallbacksAndMessages(null);
                    return;
                }

                binding.timer.setText(String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60));

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
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
