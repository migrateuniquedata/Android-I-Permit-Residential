
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ForgotPasswordRequestModel {

    @SerializedName("value")
    private String mValue;

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
