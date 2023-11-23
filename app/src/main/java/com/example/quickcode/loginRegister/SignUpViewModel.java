package com.example.quickcode.loginRegister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.ViewModel;

import com.example.quickcode.R;
import com.example.quickcode.common.cleaningEditTexts.PasswordTransformationChecker;
import com.example.quickcode.common.filters.NoEmptySpaceFilter;
import com.example.quickcode.common.helpers.InputFilterHelper;
import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.common.utils.ResourceUtils;
import com.example.quickcode.common.validator.ContainsDigit;
import com.example.quickcode.common.validator.ContainsLowerCase;
import com.example.quickcode.common.validator.ContainsSpecialChar;
import com.example.quickcode.common.validator.ContainsUpperCase;
import com.example.quickcode.common.validator.PasswordLengthValidator;
import com.example.quickcode.common.validator.Validator;
import com.example.quickcode.common.validator.ValidatorHelper;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.consts.Consts;
import com.example.quickcode.databinding.FragmentSignupTabBinding;
import com.example.quickcode.rest.QuickCodeClient;
import com.example.quickcode.rest.register.RegisterError;
import com.example.quickcode.rest.register.RegisterFailure;
import com.example.quickcode.rest.register.RegisterListener;
import com.example.quickcode.rest.register.RegisterResponse;
import com.example.quickcode.rest.register.RegisterSuccess;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    private SimpleTextWatcher firstNameTextWatcher;
    private SimpleTextWatcher emailTextWatcher;
    private SimpleTextWatcher passwordTextWatcher;
    private SimpleTextWatcher confirmPasswordTextWatcher;
    private PasswordTransformationChecker passwordTransformationChecker;
    private PasswordTransformationChecker confirmPasswordTransformationChecker;

    void addTextFilters(FragmentSignupTabBinding binding) {
        InputFilterHelper.addFilter(binding.firstName, new NoEmptySpaceFilter());
        InputFilterHelper.addFilter(binding.email, new NoEmptySpaceFilter());
        InputFilterHelper.addFilter(binding.password, new NoEmptySpaceFilter());
        InputFilterHelper.addFilter(binding.confirmPassword, new NoEmptySpaceFilter());
    }

    void setListeners(View.OnClickListener onClickListener, FragmentSignupTabBinding binding) {
        binding.signUpButton.setOnClickListener(onClickListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    void setFocusListeners(FragmentSignupTabBinding binding) {
        binding.textInputLayoutPassword.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                extendDummy(binding, (int) (binding.getRoot().getHeight() * .5f));
                scrollToTopsViewBorder(binding, binding.textInputLayoutPassword);
            } else {
                extendDummy(binding, 0);
            }
        });

        binding.confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                extendDummy(binding, (int) (binding.getRoot().getHeight() * .5f));
                scrollToTopsViewBorder(binding, binding.textInputLayoutConfirmPassword);
            } else {
                extendDummy(binding, 0);
            }
        });
    }

    private static void scrollToTopsViewBorder(FragmentSignupTabBinding binding, final TextInputLayout targetView) {
        binding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.smoothScrollTo(0, targetView.getTop());
            }
        });
    }



    public static void extendDummy(FragmentSignupTabBinding binding, int binding1) {
        ViewGroup.LayoutParams layoutParams = binding.dummy.getLayoutParams();
        layoutParams.height = binding1;
        binding.dummy.setLayoutParams(layoutParams);
    }

    void setImeListeners(FragmentSignupTabBinding binding) {
        binding.confirmPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus();
                return false;
            }
            return false;
        });
    }

    ValidatorResult validateAndGetResult(TextInputLayout textInputLayout, List<Validator> validatorList) {
        ValidatorResult validate = ValidatorHelper.validate(validatorList);
        if (validate instanceof ValidatorResult.Error) {
            textInputLayout.setError(((ValidatorResult.Error) validate).getReason(textInputLayout.getContext()));
        } else {
            textInputLayout.setError(null);
        }
        return validate;
    }

    void setStyleToValidTextView(TextView textView, @ColorRes int colorRes, Context context) {
        int color = ContextCompat.getColor(context, colorRes);
        textView.setTextColor(color);
        ColorStateList colorList = ColorStateList.valueOf(color);
        TextViewCompat.setCompoundDrawableTintList(textView, colorList);
    }

    void setEditTextStyleOnValidatorResult(ValidatorResult lengthValidator, TextView textView, ValidatorResult validatorResult, int colorSuccess, int colorFailure) {
        if (lengthValidator instanceof ValidatorResult.Success) {
            setStyleToValidTextView(textView, colorSuccess, textView.getContext());
        } else if (!(validatorResult instanceof ValidatorResult.Success)) {
            setStyleToValidTextView(textView, colorFailure, textView.getContext());
        }
    }

    void cleanViews(FragmentSignupTabBinding binding) {
        binding.textInputLayoutFirstName.setError(null);
        binding.firstName.setText(null);

        binding.textInputLayoutEmail.setError(null);
        binding.email.setText(null);

        binding.textInputLayoutPassword.setError(null);
        binding.password.setText(null);

        Context context = binding.getRoot().getContext();
        int textColorTertiary = ResourceUtils.getColorAttribute(context, android.R.attr.textColorTertiary);

        setStyleToValidTextView(binding.checkCharacterLength, textColorTertiary, context);
        setStyleToValidTextView(binding.checkSpecialChar, textColorTertiary, context);
        setStyleToValidTextView(binding.checkLowercase, textColorTertiary, context);
        setStyleToValidTextView(binding.checkUpperCase, textColorTertiary, context);
        setStyleToValidTextView(binding.checkDigit, textColorTertiary, context);

        binding.textInputLayoutConfirmPassword.setError(null);
        binding.confirmPassword.setText(null);

        binding.scrollView.scrollTo(0, 0);
    }

    void addTextWatchers(FragmentSignupTabBinding binding) {
        BindingProvider<FragmentSignupTabBinding> bindingProvider = () -> binding;

        firstNameTextWatcher = new FirstNameTextWatcher(bindingProvider);
        emailTextWatcher = new EmailTextWatcher(bindingProvider);
        passwordTextWatcher = new PasswordTextWatcher(bindingProvider, passwordTransformationChecker, text -> {
            int colorAttribute = ResourceUtils.getColorAttribute(binding.getRoot().getContext(), android.R.attr.textColorTertiary);
            validatePasswordAndSetError(text, R.color.darkerGreen, colorAttribute, binding);
            binding.textInputLayoutPassword.setError(null);
        });
        confirmPasswordTextWatcher = new ConfirmPasswordTextWatcher(bindingProvider, confirmPasswordTransformationChecker);

        binding.firstName.addTextChangedListener(firstNameTextWatcher);
        binding.email.addTextChangedListener(emailTextWatcher);
        binding.password.addTextChangedListener(passwordTextWatcher);
        binding.confirmPassword.addTextChangedListener(confirmPasswordTextWatcher);
    }

    void setupTransformationMethodCheckers(FragmentSignupTabBinding binding) {
        passwordTransformationChecker = new PasswordTransformationChecker(binding.password.getTransformationMethod());
        confirmPasswordTransformationChecker = new PasswordTransformationChecker(binding.confirmPassword.getTransformationMethod());
    }

    void removeTextWatchers(FragmentSignupTabBinding binding) {
        binding.firstName.removeTextChangedListener(firstNameTextWatcher);
        binding.email.removeTextChangedListener(emailTextWatcher);
        binding.password.removeTextChangedListener(passwordTextWatcher);
        binding.confirmPassword.removeTextChangedListener(confirmPasswordTextWatcher);
    }

    ValidatorResult validateEditTextValueAndSetStyle(int colorSuccess, int colorFailure, Validator validator, TextView checkEditText) {
        ValidatorResult validatorResult = validator.validate();
        setEditTextStyleOnValidatorResult(validatorResult, checkEditText, validatorResult, colorSuccess, colorFailure);
        return validatorResult;
    }

    ValidatorResult validatePasswordAndSetError(CharSequence charSequence, int colorSuccess, int colorFailure, FragmentSignupTabBinding binding) {
        ArrayList<ValidatorResult> validatorResultArrayList = new ArrayList<>();
        String text = charSequence.toString();

        ValidatorResult lengthValidatorResult = validateEditTextValueAndSetStyle(colorSuccess,
                colorFailure,
                new PasswordLengthValidator(text, 6),
                binding.checkCharacterLength);
        validatorResultArrayList.add(lengthValidatorResult);

        ValidatorResult containsSpecialCharValidator = validateEditTextValueAndSetStyle(colorSuccess,
                colorFailure,
                new ContainsSpecialChar(text),
                binding.checkSpecialChar);
        validatorResultArrayList.add(containsSpecialCharValidator);

        ValidatorResult containsLowerCaseValidator = validateEditTextValueAndSetStyle(colorSuccess,
                colorFailure,
                new ContainsLowerCase(text),
                binding.checkLowercase);
        validatorResultArrayList.add(containsLowerCaseValidator);

        ValidatorResult containsUpperCaseValidator = validateEditTextValueAndSetStyle(colorSuccess,
                colorFailure,
                new ContainsUpperCase(text),
                binding.checkUpperCase);
        validatorResultArrayList.add(containsUpperCaseValidator);

        ValidatorResult containsDigitValidator = validateEditTextValueAndSetStyle(colorSuccess,
                colorFailure,
                new ContainsDigit(text),
                binding.checkDigit);
        validatorResultArrayList.add(containsDigitValidator);

        for (ValidatorResult validatorResult : validatorResultArrayList) {
            if (validatorResult instanceof ValidatorResult.Error) {
                binding.textInputLayoutPassword.setError(" ");
                return validatorResult;
            }
        }

        binding.textInputLayoutPassword.setError(null);
        return new ValidatorResult.Success();
    }

    int getScrollOffsetY(Context context) {
        return (int) (12 * context.getResources().getDisplayMetrics().density);
    }

    void restorePasswordTransformationMethods(FragmentSignupTabBinding binding1) {
        binding1.password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding1.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding1.confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding1.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void registerUser(String username, String email, String password, RegisterListener registerListener) {
        QuickCodeClient instance = QuickCodeClient.getInstance();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            Call<RegisterResponse> register = instance.register(username, password, email);
            try {
                Response<RegisterResponse> execute = register.execute();
                RegisterResponse body = execute.body();

                if (execute.isSuccessful()) {
                    registerListener.onRegister(new RegisterSuccess(body));
                } else {
                    registerListener.onRegister(new RegisterFailure(body.error_code));
                }
            } catch (Exception e) {
                registerListener.onRegister(new RegisterError(e));
                e.printStackTrace();
            }
        });
    }

    public void saveUserId(Context context, long userId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong(Consts.SP_USER_ID, userId)
                .apply();
    }

    public void saveLifetime(Context context, String lifetime) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(Consts.SP_LIFETIME, lifetime)
                .apply();
    }

    public void saveUsername(Context context, String username) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(Consts.SP_USERNAME, username)
                .apply();
    }
}
