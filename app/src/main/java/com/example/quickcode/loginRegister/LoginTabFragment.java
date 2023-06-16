package com.example.quickcode.loginRegister;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.quickcode.common.cleaningEditTexts.CleanUpFragment;
import com.example.quickcode.common.cleaningEditTexts.PasswordTransformationChecker;
import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.common.validator.IsEmptyValidator;
import com.example.quickcode.common.validator.Validator;
import com.example.quickcode.common.validator.ValidatorHelper;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.databinding.LoginTabFragmentBinding;
import com.example.quickcode.forgotPassword.ForgotBottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class LoginTabFragment extends Fragment implements CleanUpFragment {

    private LoginTabFragmentBinding binding;

    private SimpleTextWatcher emailOrPhoneNumberTextWatcher;
    private SimpleTextWatcher passwordTextWatcher;
    private PasswordTransformationChecker passwordTransformationChecker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = LoginTabFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpTransformationMethodCheckers();
        fieldsAnimations();
        setUpTextWatchers();

        binding.logInButton.setOnClickListener(view2 -> validateAndLogInUser());

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        binding.textInputLayoutLoginEmail.setTranslationY(300);
        binding.textInputLayoutLoginPassword.setTranslationY(300);
        binding.forgotPassword.setTranslationY(300);
        binding.logInButton.setTranslationY(300);
        binding.layoutOrLogin.setTranslationY(300);
        binding.fabFacebook.setTranslationY(300);
        binding.fabGoogle.setTranslationY(300);
        binding.fabTwitter.setTranslationY(300);

        binding.textInputLayoutLoginEmail.setAlpha(v);
        binding.textInputLayoutLoginPassword.setAlpha(v);
        binding.forgotPassword.setAlpha(v);
        binding.logInButton.setAlpha(v);
        binding.layoutOrLogin.setAlpha(v);
        binding.fabFacebook.setAlpha(v);
        binding.fabGoogle.setAlpha(v);
        binding.fabTwitter.setAlpha(v);

        binding.textInputLayoutLoginEmail.animate().translationY(1).alpha(1).setDuration(1000).setStartDelay(400).start();
        binding.textInputLayoutLoginPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        binding.forgotPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        binding.logInButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        binding.layoutOrLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        binding.fabFacebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1300).start();
        binding.fabGoogle.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        binding.fabTwitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1500).start();

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

    private void validateAndLogInUser() {

        String getEmailOrPhoneNumber = binding.loginEmail.getText().toString();
        String getPassword = binding.loginPassword.getText().toString();

        boolean success = true;

        ValidatorResult validateEmailAndPhoneNumber = validateText(binding.textInputLayoutLoginEmail, List.of(new IsEmptyValidator(getEmailOrPhoneNumber)));
        if (validateEmailAndPhoneNumber instanceof ValidatorResult.Error) {
            success = false;
        }

        ValidatorResult validatePassword = validateText(binding.textInputLayoutLoginPassword, List.of(new IsEmptyValidator(getPassword)));
        if (validatePassword instanceof ValidatorResult.Error) {
            success = false;
        }

        if (!success) {
            // TODO: 06/06/2023 do the similar stuff as it is for sign up 
        }

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
