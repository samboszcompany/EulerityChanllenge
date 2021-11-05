package com.example.euleritychanllenge.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to EC app \n This page still developing, please go to other page and test the function");
    }

    public LiveData<String> getText() {
        return mText;
    }
}