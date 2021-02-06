
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VisitorsRequestsListResponseModel {

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

        @SerializedName("in_time")
        private String mInTime;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("user_pic")
        private String mUserPic;
        @SerializedName("visitor_location")
        private String mVisitorLocation;
        @SerializedName("visitor_name")
        private String mVisitorName;
        @SerializedName("visit_id")
        private int visit_id;
        @SerializedName("body_temperature")
        @Expose
        private String body_temperature;

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

        public String getUserPic() {
            return mUserPic;
        }

        public void setUserPic(String userPic) {
            mUserPic = userPic;
        }

        public String getVisitorLocation() {
            return mVisitorLocation;
        }

        public void setVisitorLocation(String visitorLocation) {
            mVisitorLocation = visitorLocation;
        }

        public String getVisitorName() {
            return mVisitorName;
        }

        public void setVisitorName(String visitorName) {
            mVisitorName = visitorName;
        }

        public int getVisit_id() {
            return visit_id;
        }

        public void setVisit_id(int visit_id) {
            this.visit_id = visit_id;
        }

        public String getBody_temperature() {
            return body_temperature;
        }

        public void setBody_temperature(String body_temperature) {
            this.body_temperature = body_temperature;
        }
    }


}
