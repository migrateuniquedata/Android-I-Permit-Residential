
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VerifyVisitorOTPResponseModel {

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
        @SerializedName("id")
        private int mId;
        @SerializedName("otp")
        private String mOtp;

        public String getCreatedTime() {
            return mCreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime = createdTime;
        }

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

}
