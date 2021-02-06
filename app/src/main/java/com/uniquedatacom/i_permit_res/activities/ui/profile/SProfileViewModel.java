package com.uniquedatacom.i_permit_res.activities.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}