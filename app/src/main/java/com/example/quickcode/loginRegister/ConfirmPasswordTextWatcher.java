package com.example.quickcode.loginRegister;

import android.text.method.TransformationMethod;

import com.example.quickcode.common.cleaningEditTexts.PasswordTransformationChecker;
import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.databinding.FragmentSignupTabBinding;

class ConfirmPasswordTextWatcher extends SimpleTextWatcher {

    private final BindingProvider<FragmentSignupTabBinding> bindingBindingProvider;
    private final PasswordTransformationChecker passwordTransformationChecker;

    public ConfirmPasswordTextWatcher(
            BindingProvider<FragmentSignupTabBinding> bindingBindingProvider,
            PasswordTransformationChecker passwordTransformationChecker) {
        this.bindingBindingProvider = bindingBindingProvider;
        this.passwordTransformationChecker = passwordTransformationChecker;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        FragmentSignupTabBinding binding = bindingBindingProvider.provide();
        TransformationMethod transformationMethod = binding.confirmPassword.getTransformationMethod();

        if (passwordTransformationChecker.hasTransformationMethodChanged(transformationMethod)) {
            passwordTransformationChecker.updatePasswordTransformationMethod(transformationMethod);
            return;
        }

        binding.textInputLayoutConfirmPassword.setError(null);
    }
}
