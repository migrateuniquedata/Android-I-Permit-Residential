
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpVerifyOTPResponseModel {

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

        @SerializedName("created_time")
        private String mCreatedTime;
        @SerializedName("e_id")
        private int mEId;
        @SerializedName("email_id")
        private String mEmailId;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("name")
        private String mName;
        @SerializedName("otp")
        private String mOtp;
        @SerializedName("password")
        private String mPassword;
        @SerializedName("Verified")
        private int mVerified;

        public String getCreatedTime() {
            return mCreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime = createdTime;
        }

        public int getEId() {
            return mEId;
        }

        public void setEId(int eId) {
            mEId = eId;
        }

        public String getEmailId() {
            return mEmailId;
        }

        public void setEmailId(String emailId) {
            mEmailId = emailId;
        }

        public String getMobileNumber() {
            return mMobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            mMobileNumber = mobileNumber;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getOtp() {
            return mOtp;
        }

        public void setOtp(String otp) {
            mOtp = otp;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        public int getVerified() {
            return mVerified;
        }

        public void setVerified(int verified) {
            mVerified = verified;
        }

    }

}
