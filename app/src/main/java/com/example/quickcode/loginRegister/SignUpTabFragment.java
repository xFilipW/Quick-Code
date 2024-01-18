package com.example.quickcode.loginRegister;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickcode.R;
import com.example.quickcode.common.cleaningEditTexts.CleanUpFragment;
import com.example.quickcode.common.deferred.DeferredText;
import com.example.quickcode.common.helpers.DialogHelper;
import com.example.quickcode.common.utils.AnimateUtils;
import com.example.quickcode.common.utils.KeyboardUtils;
import com.example.quickcode.common.utils.NetworkUtils;
import com.example.quickcode.common.utils.ResizeUtils;
import com.example.quickcode.common.validator.CorrectEmailValidator;
import com.example.quickcode.common.validator.FirstLetterCapitalizedValidator;
import com.example.quickcode.common.validator.IsEmptyValidator;
import com.example.quickcode.common.validator.NoDigitValidator;
import com.example.quickcode.common.validator.NoSpecialCharValidator;
import com.example.quickcode.common.validator.SameTextsValidator;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.databinding.FragmentSignupTabBinding;
import com.example.quickcode.rest.register.RegisterError;
import com.example.quickcode.rest.register.RegisterFailure;
import com.example.quickcode.rest.register.RegisterSuccess;
import com.example.quickcode.verifyEmail.VerifyBottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class SignUpTabFragment extends Fragment implements CleanUpFragment, CircleStatusListener {

    private static final String TAG = "SignUpTabFragment";

    private FragmentSignupTabBinding binding;
    private SignUpViewModel viewModel;
    private SignupSharedViewModel signupSharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentSignupTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        signupSharedViewModel = new ViewModelProvider(requireActivity()).get(SignupSharedViewModel.class);
        signupSharedViewModel.circle.observe(requireActivity(), isShown -> {
            if (isShown) {
                showCircle();
            } else {
                hideCircle();
            }
        });

        viewModel.setupTransformationMethodCheckers(binding);
        viewModel.addTextFilters(binding);

        binding.signUpButton.setOnClickListener(v -> {
            validateAndSignUpUser(binding, viewModel);
        });

        viewModel.setFocusListeners(binding);
        viewModel.setTouchListeners(binding);
        KeyboardUtils.setImeListeners(binding.confirmPassword);
        KeyboardUtils.setOnKeyboardShowed(binding, () -> ResizeUtils.resizeView(0, binding.bottomSpacer));

        addTextWatchers();
    }

    @Override
    public void restoreViews() {
        viewModel.cleanViews(binding);
    }

    @Override
    public void addTextWatchers() {
        viewModel.addTextWatchers(binding);
    }

    @Override
    public void removeTextWatchers() {
        viewModel.removeTextWatchers(binding);
    }

    @Override
    public void restorePasswordTransformationMethods() {
        viewModel.restorePasswordTransformationMethods(binding);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    void validateAndSignUpUser(FragmentSignupTabBinding binding, SignUpViewModel signUpViewModel) {
        if (!NetworkUtils.isNetworkAvailable(binding.getRoot().getContext())) {
            DialogHelper.showAlertDialogErrorGeneral(binding.getRoot().getContext(), R.string.dialog_title_no_internet, R.string.dialog_message_no_internet);
            return;
        }

        String getFirstNameText = Objects.requireNonNull(binding.firstName.getText()).toString();
        String getEmailText = Objects.requireNonNull(binding.email.getText()).toString();
        String getPasswordText = Objects.requireNonNull(binding.password.getText()).toString();
        String getConfirmPasswordText = Objects.requireNonNull(binding.confirmPassword.getText()).toString();

        LinkedHashMap<TextInputLayout, ValidatorResult> validatorResultLinkedHashMap = new LinkedHashMap<>();

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutFirstName,
                signUpViewModel.validateAndGetResult(binding.textInputLayoutFirstName,
                        List.of(new IsEmptyValidator(getFirstNameText, new DeferredText.Resource(R.string.sign_up_page_label_error_field_required)),
                                new FirstLetterCapitalizedValidator(getFirstNameText, new DeferredText.Resource(R.string.sign_up_page_label_error_first_letter_must_be_capitalized)),
                                new NoDigitValidator(getFirstNameText, new DeferredText.Resource(R.string.sign_up_page_label_error_digits_are_not_allowed)),
                                new NoSpecialCharValidator(getFirstNameText, new DeferredText.Resource(R.string.sign_up_page_label_error_special_chars_are_not_allowed)))
                )
        );

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutEmail,
                signUpViewModel.validateAndGetResult(binding.textInputLayoutEmail,
                        List.of(new IsEmptyValidator(getEmailText, new DeferredText.Resource(R.string.sign_up_page_label_error_field_required)),
                                new CorrectEmailValidator(getEmailText, new DeferredText.Resource(R.string.sign_up_page_label_error_incorrect_email)))
                )
        );

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutPassword,
                signUpViewModel.validatePasswordAndSetError(
                        getPasswordText, R.color.darkerGreen, R.color.darkerRed, binding
                )
        );

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutConfirmPassword,
                signUpViewModel.validateAndGetResult(binding.textInputLayoutConfirmPassword,
                        List.of(new IsEmptyValidator(getConfirmPasswordText, new DeferredText.Resource(R.string.sign_up_page_label_error_field_required)),
                                new SameTextsValidator(getPasswordText, getConfirmPasswordText, new DeferredText.Resource(R.string.sign_up_page_label_error_passwords_are_not_the_same)))
                )
        );

        for (TextInputLayout textInputLayout : validatorResultLinkedHashMap.keySet()) {
            ValidatorResult validatorResult = validatorResultLinkedHashMap.get(textInputLayout);
            if (validatorResult instanceof ValidatorResult.Error) {
                binding.scrollView.smoothScrollTo(0, textInputLayout.getTop() - signUpViewModel.getScrollOffsetY(requireContext()));
                return;
            }
        }

        KeyboardUtils.hideKeyboard(binding.signUpButton);

        signupSharedViewModel.showCircle();

        registerUser(getFirstNameText, getEmailText, getPasswordText);
    }

    private void registerUser(String username, String email, String password) {
        viewModel.registerUser(username, email, password, registerStatus -> {
            if (registerStatus instanceof RegisterSuccess) {
                long userId = ((RegisterSuccess) registerStatus).getResponse().user_id;
                int lifeTimeMinutes = ((RegisterSuccess) registerStatus).getResponse().lifetime_minutes;

                viewModel.saveUserId(requireContext(), userId);
                viewModel.saveLifetime(requireContext(), lifeTimeMinutes);
                viewModel.saveUsername(requireContext(), username);

                showVerifyBottomSheetDialogFragment();
            } else if (registerStatus instanceof RegisterFailure) {
                Log.e(TAG, "Error: " + ((RegisterFailure) registerStatus).getError());
                signupSharedViewModel.hideCircle();
            } else {
                Log.wtf(TAG, "Exception: " + ((RegisterError) registerStatus).getException().getMessage());
                signupSharedViewModel.hideCircle();
            }
        });
    }

    private void showVerifyBottomSheetDialogFragment() {
        AnimateUtils.animateStatusBarColor(
                (LoginActivity) requireActivity(),
                android.R.color.transparent,
                R.color.lightBlue);

        FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
        BottomSheetDialogFragment fragmentByTag = (BottomSheetDialogFragment) supportFragmentManager.findFragmentByTag(VerifyBottomSheetDialogFragment.TAG);
        if (fragmentByTag == null) {
            fragmentByTag = new VerifyBottomSheetDialogFragment();
        } else {
            fragmentByTag.dismissAllowingStateLoss();
        }
        fragmentByTag.show(supportFragmentManager, VerifyBottomSheetDialogFragment.TAG);
    }

    @Override
    public void showCircle() {
        binding.progressRegister.setVisibility(View.VISIBLE);
        ((SwipeControlListener) requireActivity()).disableViewpagerSwiping();
    }

    @Override
    public void hideCircle() {
        binding.progressRegister.setVisibility(View.GONE);
        ((SwipeControlListener) requireActivity()).enableViewpagerSwiping();
    }

}
