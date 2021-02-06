package com.uniquedatacom.i_permit_res.activities.ui.shome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}