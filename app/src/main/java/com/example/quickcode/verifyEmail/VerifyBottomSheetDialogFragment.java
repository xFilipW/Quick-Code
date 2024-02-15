package com.example.quickcode.verifyEmail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.chaos.view.PinView;
import com.example.quickcode.R;
import com.example.quickcode.common.deferred.DeferredText;
import com.example.quickcode.common.utils.AnimateUtils;
import com.example.quickcode.common.utils.MetricUtils;
import com.example.quickcode.common.utils.ResizeUtils;
import com.example.quickcode.common.utils.TimeUtils;
import com.example.quickcode.common.validator.ContainsSpaceValidator;
import com.example.quickcode.common.validator.IsEmptyValidator;
import com.example.quickcode.common.validator.IsLesserThanValidator;
import com.example.quickcode.common.validator.Validator;
import com.example.quickcode.common.validator.ValidatorHelper;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.consts.Consts;
import com.example.quickcode.databinding.ActivityVerifyEmailBinding;
import com.example.quickcode.databinding.FragmentVerifyPinviewBinding;
import com.example.quickcode.databinding.FragmentVerifySuccessBinding;
import com.example.quickcode.loginRegister.CircleStatusListener;
import com.example.quickcode.loginRegister.LoginActivity;
import com.example.quickcode.loginRegister.SharedViewModel;
import com.example.quickcode.loginRegister.SwipeControlListener;
import com.example.quickcode.rest.verify.VerifyError;
import com.example.quickcode.rest.verify.VerifyFailure;
import com.example.quickcode.rest.verify.VerifyStatus;
import com.example.quickcode.rest.verify.VerifySuccess;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyBottomSheetDialogFragment extends BottomSheetDialogFragment implements CircleStatusListener {

    public static final String TAG = "ForgotBottomSheetDialog";

    private ActivityVerifyEmailBinding binding;
    private VerifyViewModel viewModel;

    private Handler handler;
    private long futureTime = Integer.MIN_VALUE;
    private boolean countdownEnabled = true;
    private SharedViewModel sharedViewModel;
    private Validator[] pinViewValidators;

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
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        updateGreetingsText(viewModel.getDisplayUsername(requireContext()));

        viewModel.setupToolbarBehaviour(binding, this::dismiss);
        viewModel.setFocusListeners(binding);

        setPinViewBehaviour();

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

    private void setPinViewBehaviour() {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);

            fragmentBinding.pinView.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus();

                    ResizeUtils.resizeView(0, fragmentBinding.bottomSpacer);

                    return false;
                }
                return false;
            });
        }
    }

    public void setOnClickListeners(ActivityVerifyEmailBinding binding) {
        View currentView = binding.viewSwitcher.getCurrentView();

        if (currentView.getId() == R.id.switch_view_pin) {
            onClickButtonOnSwitchViewPin(currentView);
        } else {
            // TODO: 10.10.2023
        }
    }

    private void onClickButtonOnSwitchViewPin(View currentView) {
        FragmentVerifyPinviewBinding fragmentBinding = FragmentVerifyPinviewBinding.bind(currentView);

        fragmentBinding.verifyEmailButton.setOnClickListener(view -> {
            int dpTop = MetricUtils.toDp(requireContext(), 8);
            int dpBot = MetricUtils.toDp(requireContext(), 12);

            TextView textViewWrongPin = fragmentBinding.textViewWrongPin;
            PinView pinView = fragmentBinding.pinView;

            int errorTextColor = getResources().getColor(R.color.darkerRed);

            String pinViewText = pinView.getText().toString();

            DeferredText.Resource reasonFieldRequired = new DeferredText.Resource(R.string.verify_email_label_error_field_required);
            DeferredText.Resource reasonFillInAllFields = new DeferredText.Resource(R.string.verify_email_label_error_fill_in_all_fields);

            pinViewValidators = new Validator[]{
                    new IsEmptyValidator(pinViewText, reasonFieldRequired),
                    new IsLesserThanValidator(pinViewText, 4, reasonFillInAllFields),
                    new ContainsSpaceValidator(pinViewText, reasonFillInAllFields),
            };

            ValidatorResult validate = ValidatorHelper.validate(Arrays.asList(pinViewValidators));

            if (validate instanceof ValidatorResult.Success) {
                onValidatorResultSuccess(pinView, textViewWrongPin, dpTop, dpBot, errorTextColor);
            } else {
                onValidatorResultError(pinView, textViewWrongPin, dpTop, dpBot, errorTextColor, (ValidatorResult.Error) validate);
            }
        });
    }

    private void onValidatorResultSuccess(PinView pinView, TextView textViewWrongPin, int dpTop, int dpBot, int errorTextColor) {
        textViewWrongPin.setText("");
        handler.post(this::showCircle);
        viewModel.verifyUser(
                viewModel.getUserId(requireContext()),
                String.valueOf(pinView.getText()),
                verifyStatus -> {
                    if (verifyStatus instanceof VerifySuccess) {
                        handler.post(this::hideCircle);
                        showLottieResult(verifyStatus);
                    } else if (verifyStatus instanceof VerifyFailure) {
                        Log.e(TAG, "Error: " + ((VerifyFailure) verifyStatus).getError());
                        handler.post(this::hideCircle);
                        onPinViewCodeInvalid(pinView, textViewWrongPin, dpTop, dpBot, "Code Incorrect", errorTextColor);
                    } else {
                        Log.wtf(TAG, "Exception: " + ((VerifyError) verifyStatus).getException().getMessage());
                        handler.post(this::hideCircle);
                    }
                });
    }

    private void onValidatorResultError(PinView pinView, TextView textViewWrongPin, int dpTop, int dpBot, int errorTextColor, ValidatorResult.Error validate) {
        String reason = validate.getReason(requireContext());
        onPinViewCodeInvalid(pinView, textViewWrongPin, dpTop, dpBot, reason, errorTextColor);
        handler.post(this::hideCircle);
    }

    private void onPinViewCodeInvalid(PinView pinView, TextView textViewWrongPin, int dpTop, int dpBot, String text, int color) {
        textViewWrongPin.setText(text);
        textViewWrongPin.setPadding(0, dpTop, 0, dpBot);
        pinView.setLineColor(color);
    }

    private void showLottieResult(VerifyStatus verifyStatus) {
        if (verifyStatus instanceof VerifySuccess) {
            View nextView = binding.viewSwitcher.getNextView();
            FragmentVerifySuccessBinding fragmentBinding = FragmentVerifySuccessBinding.bind(nextView);

            binding.viewSwitcher.showNext();
            binding.timer.setVisibility(View.GONE);

            fragmentBinding.lottie.playAnimation();
            fragmentBinding.backToLogInButton.setOnClickListener(v -> {
//                dismiss();
//                ((TabListener)requireActivity()).changeTab(index_0);
            });
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
                sharedViewModel.hideCircle();
            }
        }, 1000);
    }

    private void updateTextLifetime() {
        if (!countdownEnabled)
            return;

        int lifetime = PreferenceManager.getDefaultSharedPreferences(requireContext()).getInt(Consts.SP_LIFETIME_MINUTES, -1);
        if (lifetime == -1)
            return;

        Date date = TimeUtils.createDate((int) TimeUnit.MINUTES.toMillis(lifetime));

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


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        AnimateUtils.animateStatusBarColor(
                ((LoginActivity) requireActivity()),
                R.color.lightBlue,
                android.R.color.transparent
        );
    }
}
