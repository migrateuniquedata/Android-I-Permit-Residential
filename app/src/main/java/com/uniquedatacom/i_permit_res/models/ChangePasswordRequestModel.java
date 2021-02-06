
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ChangePasswordRequestModel {

    @SerializedName("confirm_password")
    private String mConfirmPassword;
    @SerializedName("new_password")
    private String mNewPassword;
    @SerializedName("value")
    private String mValue;

    public String getConfirmPassword() {
        return mConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        mConfirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String newPassword) {
        mNewPassword = newPassword;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
