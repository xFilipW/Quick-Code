package com.example.quickcode.loginRegister;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _circle = new MutableLiveData<>(false);
    public LiveData<Boolean> circle = _circle;

    public void hideCircle() {
        _circle.postValue(false);
    }

    public void showCircle() {
        _circle.postValue(true);
    }
}
