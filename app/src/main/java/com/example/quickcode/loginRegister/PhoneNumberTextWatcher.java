package com.example.quickcode.loginRegister;

import android.text.Editable;
import android.util.Log;

import com.example.quickcode.common.simples.SimpleTextWatcher;
import com.example.quickcode.common.utils.StringUtils;
import com.example.quickcode.databinding.SignupTabFragmentBinding;

class PhoneNumberTextWatcher extends SimpleTextWatcher {

    private static final String TAG = "PhoneNumberTextWatcher";

    private final BindingProvider<SignupTabFragmentBinding> bindingProvider;

    public PhoneNumberTextWatcher(
            BindingProvider<SignupTabFragmentBinding> bindingProvider) {
        this.bindingProvider = bindingProvider;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        SignupTabFragmentBinding binding = bindingProvider.provide();
        binding.textInputLayoutPhoneNumber.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isWatchable()) {
            setWatchable(true);
            return;
        }

        setWatchable(false);

        String toGroupsAndJoin = StringUtils.splitToGroupsAndJoin(3, s.toString(), " ");
        s.replace(0, s.length(), toGroupsAndJoin);

        Log.d(TAG, "afterTextChanged() returned: " + s);
    }

}
