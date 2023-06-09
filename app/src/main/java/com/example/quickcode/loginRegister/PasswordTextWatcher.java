package com.example.quickcode.loginRegister;

import android.text.method.TransformationMethod;

import com.example.quickcode.common.cleaningEditTexts.PasswordTransformationChecker;
import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.databinding.SignupTabFragmentBinding;

class PasswordTextWatcher extends SimpleTextWatcher {

    private final BindingProvider<SignupTabFragmentBinding> bindingBindingProvider;
    private final PasswordTransformationChecker passwordTransformationChecker;
    private final PasswordTextPostCallback passwordTextPostCallback;

    public PasswordTextWatcher(
            BindingProvider<SignupTabFragmentBinding> bindingBindingProvider,
            PasswordTransformationChecker passwordTransformationChecker,
            PasswordTextPostCallback passwordTextPostCallback) {
        this.bindingBindingProvider = bindingBindingProvider;
        this.passwordTransformationChecker = passwordTransformationChecker;
        this.passwordTextPostCallback = passwordTextPostCallback;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        SignupTabFragmentBinding binding = bindingBindingProvider.provide();
        TransformationMethod transformationMethod = binding.password.getTransformationMethod();

        if (passwordTransformationChecker.hasTransformationMethodChanged(transformationMethod)) {
            passwordTransformationChecker.updatePasswordTransformationMethod(transformationMethod);
            return;
        }

        passwordTextPostCallback.onPostTextChanged(s);
    }


}
