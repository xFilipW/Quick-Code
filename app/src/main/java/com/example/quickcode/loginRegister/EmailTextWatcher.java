package com.example.quickcode.loginRegister;

import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.databinding.SignupTabFragmentBinding;

class EmailTextWatcher extends SimpleTextWatcher {

    private final BindingProvider<SignupTabFragmentBinding> bindingBindingProvider;

    public EmailTextWatcher(
            BindingProvider<SignupTabFragmentBinding> bindingBindingProvider) {

        this.bindingBindingProvider = bindingBindingProvider;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        SignupTabFragmentBinding binding = bindingBindingProvider.provide();
        binding.textInputLayoutEmail.setError(null);
    }
}
