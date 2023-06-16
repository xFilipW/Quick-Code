package com.example.quickcode.loginRegister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickcode.R;
import com.example.quickcode.common.cleaningEditTexts.CleanUpFragment;
import com.example.quickcode.common.helpers.DialogHelper;
import com.example.quickcode.common.utils.NetworkUtils;
import com.example.quickcode.common.validator.CorrectEmailValidator;
import com.example.quickcode.common.validator.FirstLetterCapitalizedValidator;
import com.example.quickcode.common.validator.IsEmptyValidator;
import com.example.quickcode.common.validator.NoDigitValidator;
import com.example.quickcode.common.validator.NoSpecialCharValidator;
import com.example.quickcode.common.validator.SameTextsValidator;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.databinding.SignupTabFragmentBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class SignUpTabFragment extends Fragment implements CleanUpFragment {

    private static final String TAG = "SignUpTabFragment";

    private SignupTabFragmentBinding binding;
    private SignUpViewModel model;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = SignupTabFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(SignUpViewModel.class);

        model.setupTransformationMethodCheckers(binding);
        model.addTextFilters(binding);
        model.setListeners(v -> validateAndSignUpUser(binding, model), binding);
        model.setTouchListeners(binding);
        model.setFocusListeners(binding);
        model.setImeListeners(binding);

        addTextWatchers();
    }

    @Override
    public void restoreViews() {
        model.cleanViews(binding);
    }

    @Override
    public void addTextWatchers() {
        model.addTextWatchers(binding);
    }

    @Override
    public void removeTextWatchers() {
        model.removeTextWatchers(binding);
    }

    @Override
    public void restorePasswordTransformationMethods() {
        model.restorePasswordTransformationMethods(binding);
    }

    void validateAndSignUpUser(SignupTabFragmentBinding binding, SignUpViewModel signUpViewModel) {
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
                        List.of(new IsEmptyValidator(getFirstNameText),
                                new FirstLetterCapitalizedValidator(getFirstNameText),
                                new NoDigitValidator(getFirstNameText),
                                new NoSpecialCharValidator(getFirstNameText))
                )
        );

        validatorResultLinkedHashMap.put(
                binding.textInputLayoutEmail,
                signUpViewModel.validateAndGetResult(binding.textInputLayoutEmail,
                        List.of(new IsEmptyValidator(getEmailText),
                                new CorrectEmailValidator(getEmailText))
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
                        List.of(new IsEmptyValidator(getConfirmPasswordText),
                                new SameTextsValidator(getPasswordText, getConfirmPasswordText))
                )
        );

        for (TextInputLayout textInputLayout : validatorResultLinkedHashMap.keySet()) {
            ValidatorResult validatorResult = validatorResultLinkedHashMap.get(textInputLayout);
            if (validatorResult instanceof ValidatorResult.Error) {
                binding.scrollView.smoothScrollTo(0, textInputLayout.getTop() - signUpViewModel.getScrollOffsetY(requireContext()));
                return;
            }
        }

        User user = new User(getFirstNameText, getEmailText);

        signUpViewModel.liveEmailValidated.removeObservers(getViewLifecycleOwner());
        signUpViewModel.liveEmailValidated.setValue(null);

        Observer<Boolean> validateObserver = result -> {
            if (Boolean.TRUE.equals(signUpViewModel.liveEmailValidated.getValue())) {
                signUpViewModel.createUserAccount(user, getPasswordText, this);
            }
        };

        signUpViewModel.liveEmailValidated.observe(getViewLifecycleOwner(), validateObserver);

        signUpViewModel.requireEmailNotUsed(user, binding);
    }
}
