
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VerifyVisitorOTPRequestModel {

    @SerializedName("id")
    private int mId;
    @SerializedName("otp")
    private String mOtp;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getOtp() {
        return mOtp;
    }

    public void setOtp(String otp) {
        mOtp = otp;
    }

}
