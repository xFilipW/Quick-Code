package com.example.quickcode.loginRegister;

import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.databinding.FragmentSignupTabBinding;

class FirstNameTextWatcher extends SimpleTextWatcher {

    private final BindingProvider<FragmentSignupTabBinding> bindingProvider;

    public FirstNameTextWatcher(
            BindingProvider<FragmentSignupTabBinding> bindingProvider) {
        this.bindingProvider = bindingProvider;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        FragmentSignupTabBinding binding = bindingProvider.provide();
        binding.textInputLayoutFirstName.setError(null);
    }
}
