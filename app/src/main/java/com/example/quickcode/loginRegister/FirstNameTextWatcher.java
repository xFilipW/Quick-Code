package com.example.quickcode.loginRegister;

import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.databinding.SignupTabFragmentBinding;

class FirstNameTextWatcher extends SimpleTextWatcher {

    private final BindingProvider<SignupTabFragmentBinding> bindingProvider;

    public FirstNameTextWatcher(
            BindingProvider<SignupTabFragmentBinding> bindingProvider) {
        this.bindingProvider = bindingProvider;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        SignupTabFragmentBinding binding = bindingProvider.provide();
        binding.textInputLayoutFirstName.setError(null);
    }
}
