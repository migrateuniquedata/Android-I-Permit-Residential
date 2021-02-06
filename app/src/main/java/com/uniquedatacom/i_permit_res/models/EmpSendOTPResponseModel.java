
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpSendOTPResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private int mStatus;

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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public class Result {

        @SerializedName("e_id")
        private int mEId;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("otp")
        private String mOtp;

        public int getEId() {
            return mEId;
        }

        public void setEId(int eId) {
            mEId = eId;
        }

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

    

}
