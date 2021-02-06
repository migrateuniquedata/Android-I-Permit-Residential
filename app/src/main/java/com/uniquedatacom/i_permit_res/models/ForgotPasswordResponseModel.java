
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ForgotPasswordResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("otp")
    private String mOTP;
    @SerializedName("status")
    private Long mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getOTP() {
        return mOTP;
    }

    public void setOTP(String oTP) {
        mOTP = oTP;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
