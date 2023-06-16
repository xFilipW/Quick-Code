package com.example.quickcode.loginRegister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quickcode.R;
import com.example.quickcode.common.cleaningEditTexts.PasswordTransformationChecker;
import com.example.quickcode.common.filters.NoEmptySpaceFilter;
import com.example.quickcode.common.helpers.DialogHelper;
import com.example.quickcode.common.helpers.InputFilterHelper;
import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.common.utils.ResourceUtils;
import com.example.quickcode.common.utils.ScrollUtils;
import com.example.quickcode.common.validator.ContainsDigit;
import com.example.quickcode.common.validator.ContainsLowerCase;
import com.example.quickcode.common.validator.ContainsSpecialChar;
import com.example.quickcode.common.validator.ContainsUpperCase;
import com.example.quickcode.common.validator.FirebaseEmailValidator;
import com.example.quickcode.common.validator.PasswordLengthValidator;
import com.example.quickcode.common.validator.Validator;
import com.example.quickcode.common.validator.ValidatorHelper;
import com.example.quickcode.common.validator.ValidatorResult;
import com.example.quickcode.databinding.SignupTabFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    final MutableLiveData<Boolean> liveEmailValidated = new MutableLiveData<>(null);

    private SimpleTextWatcher firstNameTextWatcher;
    private SimpleTextWatcher emailTextWatcher;
    private SimpleTextWatcher passwordTextWatcher;
    private SimpleTextWatcher confirmPasswordTextWatcher;
    private PasswordTransformationChecker passwordTransformationChecker;
    private PasswordTransformationChecker confirmPasswordTransformationChecker;

    void addTextFilters(SignupTabFragmentBinding binding) {
        InputFilterHelper.addFilter(binding.firstName, new NoEmptySpaceFilter());
        InputFilterHelper.addFilter(binding.email, new NoEmptySpaceFilter());
        InputFilterHelper.addFilter(binding.password, new NoEmptySpaceFilter());
        InputFilterHelper.addFilter(binding.confirmPassword, new NoEmptySpaceFilter());
    }

    void setListeners(View.OnClickListener onClickListener, SignupTabFragmentBinding binding) {
        binding.signUpButton.setOnClickListener(onClickListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    void setTouchListeners(SignupTabFragmentBinding binding) {
        binding.password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ScrollUtils.smartScrollTo(() -> binding.passwordRequirements, "touch");
            }
            return false;
        });

        binding.confirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ScrollUtils.smartScrollTo(() -> binding.confirmPassword, "touch");
            }
            return false;
        });

        binding.scrollView.setOnTouchListener((v, event) -> {
            View focusedChild = binding.getRoot().getFocusedChild();
            if (focusedChild != null) {
                focusedChild.clearFocus();
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void setFocusListeners(SignupTabFragmentBinding binding) {
        binding.password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ScrollUtils.smartScrollTo(() -> binding.passwordRequirements, "focus");
            } else {
                ScrollUtils.cleanup();
            }
        });

        binding.confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ScrollUtils.smartScrollTo(() -> binding.confirmPassword, "focus");
            } else {
                ScrollUtils.cleanup();
            }
        });
    }

    void setImeListeners(SignupTabFragmentBinding binding) {
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

    void cleanViews(SignupTabFragmentBinding binding) {
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

    void addTextWatchers(SignupTabFragmentBinding binding) {
        BindingProvider<SignupTabFragmentBinding> bindingProvider = () -> binding;

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

    void setupTransformationMethodCheckers(SignupTabFragmentBinding binding) {
        passwordTransformationChecker = new PasswordTransformationChecker(binding.password.getTransformationMethod());
        confirmPasswordTransformationChecker = new PasswordTransformationChecker(binding.confirmPassword.getTransformationMethod());
    }

    void removeTextWatchers(SignupTabFragmentBinding binding) {
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

    ValidatorResult validatePasswordAndSetError(CharSequence charSequence, int colorSuccess, int colorFailure, SignupTabFragmentBinding binding) {
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

    void requireEmailNotUsed(User user, final SignupTabFragmentBinding binding) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ValidatorResult validate = new FirebaseEmailValidator(user.getEmail(), snapshot.getChildren()).validate();
                if (validate instanceof ValidatorResult.Success) {
                    liveEmailValidated.setValue(true);
                } else {
                    binding.textInputLayoutEmail.setError(((ValidatorResult.Error) validate).getReason(binding.getRoot().getContext()));
                    liveEmailValidated.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DialogHelper.showAlertDialogErrorGeneral(binding.getRoot().getContext(), R.string.dialog_title_request_error, R.string.dialog_message_request_error);
                liveEmailValidated.setValue(false);
            }
        });
    }

    void storeUserData(User user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = firebaseDatabase.getReference("user");

        HashMap<String, Object> data = new HashMap<>();
        data.put(user.getUid(), user.toMap());

        databaseRef.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: stored");
                } else {
                    Log.e(TAG, "onComplete: not stored");
                }
            }
        });
    }

    void createUserAccount(User user, String password, SignUpTabFragment signUpTabFragment) {
        FirebaseAuth instance = FirebaseAuth.getInstance();
        instance.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(signUpTabFragment.requireActivity(), task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:success");
                user.setUid(instance.getCurrentUser().getUid());
                storeUserData(user);
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                task.getException().printStackTrace();
            }
        });
    }

    int getScrollOffsetY(Context context) {
        return (int) (12 * context.getResources().getDisplayMetrics().density);
    }

    void restorePasswordTransformationMethods(SignupTabFragmentBinding binding1) {
        binding1.password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding1.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding1.confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding1.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
