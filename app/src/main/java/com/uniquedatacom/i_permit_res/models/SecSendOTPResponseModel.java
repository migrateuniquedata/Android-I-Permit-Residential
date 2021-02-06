
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SecSendOTPResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private Long mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Result> getResult() {
        return mResult;
    }

    public void setResult(List<Result> result) {
        mResult = result;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public class Result {

        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("otp")
        private String mOtp;
        @SerializedName("sec_id")
        private Long mSecId;

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

        public Long getSecId() {
            return mSecId;
        }

        public void setSecId(Long secId) {
            mSecId = secId;
        }

    }

}
