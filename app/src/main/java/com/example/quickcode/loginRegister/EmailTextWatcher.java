package com.example.quickcode.loginRegister;

import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.databinding.FragmentSignupTabBinding;

class EmailTextWatcher extends SimpleTextWatcher {

    private final BindingProvider<FragmentSignupTabBinding> bindingBindingProvider;

    public EmailTextWatcher(
            BindingProvider<FragmentSignupTabBinding> bindingBindingProvider) {

        this.bindingBindingProvider = bindingBindingProvider;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        FragmentSignupTabBinding binding = bindingBindingProvider.provide();
        binding.textInputLayoutEmail.setError(null);
    }
}
