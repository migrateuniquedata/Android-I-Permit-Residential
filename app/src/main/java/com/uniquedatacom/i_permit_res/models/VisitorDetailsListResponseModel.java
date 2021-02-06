
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class VisitorDetailsListResponseModel {

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

        @SerializedName("duration")
        private String mDuration;
        @SerializedName("from")
        private String mFrom;
        @SerializedName("in_time")
        private String mInTime;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("name")
        private String mName;
        @SerializedName("out_time")
        private String mOutTime;
        @SerializedName("user_pic")
        private String mUserPic;

        public String getDuration() {
            return mDuration;
        }

        public void setDuration(String duration) {
            mDuration = duration;
        }

        public String getFrom() {
            return mFrom;
        }

        public void setFrom(String from) {
            mFrom = from;
        }

        public String getInTime() {
            return mInTime;
        }

        public void setInTime(String inTime) {
            mInTime = inTime;
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

        public String getOutTime() {
            return mOutTime;
        }

        public void setOutTime(String outTime) {
            mOutTime = outTime;
        }

        public String getUserPic() {
            return mUserPic;
        }

        public void setUserPic(String userPic) {
            mUserPic = userPic;
        }

    }


}
