package com.example.quickcode.loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
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
import com.example.quickcode.common.cleaningEditTexts.PasswordTransformationChecker;
import com.example.quickcode.common.deferred.DeferredText;
import com.example.quickcode.common.helpers.DialogHelper;
import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.common.utils.AnimateUtils;
import com.example.quickcode.common.utils.KeyboardUtils;
import com.example.quickcode.common.utils.NetworkUtils;
import com.example.quickcode.common.validator.CorrectEmailValidator;
import com.example.quickcode.common.validator.IsEmptyValidator;
import com.example.quickcode.common.validator.Validator;
import com.example.quickcode.common.validator.ValidatorHelper;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.databinding.FragmentLoginTabBinding;
import com.example.quickcode.forgotPassword.ForgotBottomSheetDialogFragment;
import com.example.quickcode.main.MainActivity;
import com.example.quickcode.rest.login.LoginError;
import com.example.quickcode.rest.login.LoginFailure;
import com.example.quickcode.rest.login.LoginSuccess;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.LinkedHashMap;
import java.util.List;

public class LoginTabFragment extends Fragment implements CleanUpFragment, CircleStatusListener {

    private static final String TAG = "LoginTabFragment";
    private FragmentLoginTabBinding binding;
    private SimpleTextWatcher emailOrPhoneNumberTextWatcher;
    private SimpleTextWatcher passwordTextWatcher;
    private PasswordTransformationChecker passwordTransformationChecker;
    private SharedViewModel sharedViewModel;
    private LogInViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentLoginTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpTransformationMethodCheckers();
        fieldsAnimations();
        setUpTextWatchers();

        viewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.circle.observe(requireActivity(), isShown -> {
            if (isShown) {
                showCircle();
            } else {
                hideCircle();
            }
        });

        binding.logInButton.setOnClickListener(v -> {
            validateAndLogInUser(binding, viewModel);
            binding.textInputLayoutLoginEmail.clearFocus();
            binding.textInputLayoutLoginPassword.clearFocus();
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimateUtils.animateStatusBarColor(
                        (LoginActivity) requireActivity(),
                        android.R.color.transparent,
                        R.color.lightBlue);

                FragmentManager parentFragmentManager = requireActivity().getSupportFragmentManager();
                BottomSheetDialogFragment fragmentByTag = (BottomSheetDialogFragment) parentFragmentManager.findFragmentByTag(ForgotBottomSheetDialogFragment.TAG);

                if (fragmentByTag != null) {
                    fragmentByTag.dismiss();
                }

                new ForgotBottomSheetDialogFragment().show(parentFragmentManager, ForgotBottomSheetDialogFragment.TAG);
            }
        });
    }

    public void fieldsAnimations() {

        float v = 0;

        binding.textInputLayoutLoginEmail.setTranslationX(800);
        binding.textInputLayoutLoginPassword.setTranslationX(800);
        binding.forgotPassword.setTranslationX(800);
        binding.logInButton.setTranslationX(800);

        binding.layoutOrLogin.setTranslationY(400);
        binding.fabFacebook.setTranslationY(400);
        binding.fabGoogle.setTranslationY(400);
        binding.fabTwitterX.setTranslationY(400);

        binding.textInputLayoutLoginEmail.setAlpha(v);
        binding.textInputLayoutLoginPassword.setAlpha(v);
        binding.forgotPassword.setAlpha(v);
        binding.logInButton.setAlpha(v);
        binding.layoutOrLogin.setAlpha(v);
        binding.fabFacebook.setAlpha(v);
        binding.fabGoogle.setAlpha(v);
        binding.fabTwitterX.setAlpha(v);

        binding.textInputLayoutLoginEmail.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        binding.textInputLayoutLoginPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        binding.forgotPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        binding.logInButton.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();

        binding.layoutOrLogin.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(900).start();
        binding.fabFacebook.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(1100).start();
        binding.fabGoogle.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(1300).start();
        binding.fabTwitterX.animate().translationY(0).alpha(1).setDuration(1200).setStartDelay(1500).start();
    }

    private ValidatorResult validateText(TextInputLayout textInputLayout, List<Validator> validatorList) {
        ValidatorResult validate = ValidatorHelper.validate(validatorList);
        if (validate instanceof ValidatorResult.Error) {
            textInputLayout.setError(((ValidatorResult.Error) validate).getReason(requireContext()));
        } else {
            textInputLayout.setError(null);
        }
        return validate;
    }

    void validateAndLogInUser(FragmentLoginTabBinding binding, LogInViewModel logInViewModel) {
        if (!NetworkUtils.isNetworkAvailable(binding.getRoot().getContext())) {
            DialogHelper.showAlertDialogErrorGeneral(binding.getRoot().getContext(), R.string.dialog_title_no_internet, R.string.dialog_message_no_internet);
            return;
        }

        String getEmailText = binding.loginEmail.getText() != null ? binding.loginEmail.getText().toString() : "";
        String getPasswordText = binding.loginPassword.getText() != null ? binding.loginPassword.getText().toString() : "";

        LinkedHashMap<TextInputLayout, ValidatorResult> validatorResultLinkedHashMap = new LinkedHashMap<>();

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutLoginEmail,
                logInViewModel.validateAndGetResult(binding.textInputLayoutLoginEmail,
                        List.of(new IsEmptyValidator(getEmailText, new DeferredText.Resource(R.string.sign_up_page_label_error_field_required)),
                                new CorrectEmailValidator(getEmailText, new DeferredText.Resource(R.string.sign_up_page_label_error_incorrect_email)))
                )
        );

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutLoginPassword,
                logInViewModel.validateAndGetResult(binding.textInputLayoutLoginPassword,
                        List.of(new IsEmptyValidator(getEmailText, new DeferredText.Resource(R.string.sign_up_page_label_error_field_required)))
                )
        );

        for (TextInputLayout textInputLayout : validatorResultLinkedHashMap.keySet()) {
            ValidatorResult validatorResult = validatorResultLinkedHashMap.get(textInputLayout);
            if (validatorResult instanceof ValidatorResult.Error) {
                textInputLayout.setError(((ValidatorResult.Error) validatorResult).getReason(requireContext()));
                return;
            }
        }

        KeyboardUtils.hideKeyboard(binding.logInButton);

        sharedViewModel.showCircle();

        logInUser(getEmailText, getPasswordText);
    }

    private void logInUser(String email, String password) {
        viewModel.loginUser(email, password, loginStatus -> {
            if (loginStatus instanceof LoginSuccess) {

                long resId = ((LoginSuccess) loginStatus).getResponse().id;
                String resFirstName = ((LoginSuccess) loginStatus).getResponse().first_name;
                String resPassword = ((LoginSuccess) loginStatus).getResponse().password;
                String resEmail = ((LoginSuccess) loginStatus).getResponse().email;
                int resVerified = ((LoginSuccess) loginStatus).getResponse().verified;

                startActivity(new Intent(requireContext(), MainActivity.class));

            } else if (loginStatus instanceof LoginFailure) {
                Log.e(TAG, "Error: " + ((LoginFailure) loginStatus).getError());
                sharedViewModel.hideCircle();
                binding.textInputLayoutLoginEmail.setError("Incorrect e-mail or password");
                binding.textInputLayoutLoginPassword.setError(" ");
            } else {
                Log.wtf(TAG, "Exception: " + ((LoginError) loginStatus).getException().getMessage());
                sharedViewModel.hideCircle();
            }
        });
    }

    private void setUpTextWatchers() {
        addTextWatchers();
    }

    @Override
    public void restoreViews() {
        binding.textInputLayoutLoginEmail.setError(null);
        binding.loginEmail.setText(null);
        binding.textInputLayoutLoginPassword.setError(null);
        binding.loginPassword.setText(null);
    }

    @Override
    public void addTextWatchers() {
        emailOrPhoneNumberTextWatcher = new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textInputLayoutLoginEmail.setError(null);
            }
        };

        passwordTextWatcher = new PasswordTextWatcher();

        binding.loginEmail.addTextChangedListener(emailOrPhoneNumberTextWatcher);
        binding.loginPassword.addTextChangedListener(passwordTextWatcher);
    }

    @Override
    public void removeTextWatchers() {
        binding.loginEmail.removeTextChangedListener(emailOrPhoneNumberTextWatcher);
        binding.loginPassword.removeTextChangedListener(passwordTextWatcher);
    }

    @Override
    public void restorePasswordTransformationMethods() {
        binding.loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding.loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void setUpTransformationMethodCheckers() {
        passwordTransformationChecker = new PasswordTransformationChecker(binding.loginPassword.getTransformationMethod());
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

    private class PasswordTextWatcher extends SimpleTextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TransformationMethod transformationMethod = binding.loginPassword.getTransformationMethod();

            if (passwordTransformationChecker.hasTransformationMethodChanged(transformationMethod)) {
                passwordTransformationChecker.updatePasswordTransformationMethod(transformationMethod);
            }
        }
    }
}
