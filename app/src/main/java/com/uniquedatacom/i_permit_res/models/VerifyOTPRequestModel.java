
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VerifyOTPRequestModel {

    @SerializedName("mobile_number")
    private String mMobileNumber;
    @SerializedName("otp")
    private String mOtp;

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getOtp() {
        return mOtp;
    }

    public void setOtp(String otp) {
        mOtp = otp;
    }

}
