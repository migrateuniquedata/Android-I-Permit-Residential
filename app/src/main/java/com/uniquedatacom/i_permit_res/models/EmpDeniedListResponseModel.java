
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpDeniedListResponseModel {

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

        @SerializedName("body_temperature")
        @Expose
        private String bodyTemperature;
        @SerializedName("in_time")
        @Expose
        private String inTime;
        @SerializedName("mask")
        @Expose
        private Integer mask;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("user_pic")
        @Expose
        private String userPic;
        @SerializedName("visit_id")
        @Expose
        private Integer visitId;
        @SerializedName("visitor_location")
        @Expose
        private String visitorLocation;
        @SerializedName("visitor_name")
        @Expose
        private String visitorName;
        @SerializedName("whom_to_meet")
        @Expose
        private String whomToMeet;

        public String getBodyTemperature() {
            return bodyTemperature;
        }

        public void setBodyTemperature(String bodyTemperature) {
            this.bodyTemperature = bodyTemperature;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public Integer getMask() {
            return mask;
        }

        public void setMask(Integer mask) {
            this.mask = mask;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

        public Integer getVisitId() {
            return visitId;
        }

        public void setVisitId(Integer visitId) {
            this.visitId = visitId;
        }

        public String getVisitorLocation() {
            return visitorLocation;
        }

        public void setVisitorLocation(String visitorLocation) {
            this.visitorLocation = visitorLocation;
        }

        public String getVisitorName() {
            return visitorName;
        }

        public void setVisitorName(String visitorName) {
            this.visitorName = visitorName;
        }

        public String getWhomToMeet() {
            return whomToMeet;
        }

        public void setWhomToMeet(String whomToMeet) {
            this.whomToMeet = whomToMeet;
        }

    }
}
