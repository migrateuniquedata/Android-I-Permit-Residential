
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SendOTPRequestModel {

    @SerializedName("mobile_number")
    private String mMobileNumber;

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

}
