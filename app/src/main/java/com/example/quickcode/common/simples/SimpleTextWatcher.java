package com.example.quickcode.common.simples;

import android.text.Editable;
import android.text.TextWatcher;

public class SimpleTextWatcher implements TextWatcher {

    /**
     * Indicates whether this text watcher should watch the text or not.
     */
    private boolean watchable = true;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public boolean isWatchable() {
        return watchable;
    }

    public void setWatchable(boolean watchable) {
        this.watchable = watchable;
    }
}
