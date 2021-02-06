package com.uniquedatacom.i_permit_res.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class CustomApplication {
//        extends BaseApplication {
    @Nullable
    protected SharedPreferences getCustomSharedPreferences(@NonNull Context base) {
        // If you are planning to store the user language in a custom shared preferences, create
        // and initialize them here.
        // If not, you can safely return "null"

//        return base.getSharedPreferences("myPrefs", MODE_PRIVATE);
        // OR
        return null;
    }
}
