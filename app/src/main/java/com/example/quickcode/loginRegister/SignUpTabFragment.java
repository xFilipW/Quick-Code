package com.example.quickcode.loginRegister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

        signUpUser(getFirstNameText, getEmailText, getPasswordText);

    }

    private void signUpUser(String username, String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showAlert("Successful Sign Up!", "Welcome" + username +"!");
                } else {
                    ParseUser.logOut();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showAlert(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
