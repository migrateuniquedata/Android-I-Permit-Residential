
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ActivityVisitorsEmpResponseModel {

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
        @Expose
        private String duration;
        @SerializedName("e_id")
        @Expose
        private Integer eId;
        @SerializedName("in_time")
        @Expose
        private String inTime;
        @SerializedName("outgoingtime")
        @Expose
        private String outgoingtime;
        @SerializedName("visitor_location")
        @Expose
        private String visitorLocation;
        @SerializedName("visitor_mobile_number")
        @Expose
        private String visitorMobileNumber;
        @SerializedName("visitor_name")
        @Expose
        private String visitorName;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("user_pic")
        @Expose
        private String userPic;
        @SerializedName("whom_to_meet")
        @Expose
        private String whomToMeet;
        @SerializedName("body_temperature")
        @Expose
        private String body_temperature;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public Integer getEId() {
            return eId;
        }

        public void setEId(Integer eId) {
            this.eId = eId;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public String getOutgoingtime() {
            return outgoingtime;
        }

        public void setOutgoingtime(String outgoingtime) {
            this.outgoingtime = outgoingtime;
        }

        public String getVisitorLocation() {
            return visitorLocation;
        }

        public void setVisitorLocation(String visitorLocation) {
            this.visitorLocation = visitorLocation;
        }

        public String getVisitorMobileNumber() {
            return visitorMobileNumber;
        }

        public void setVisitorMobileNumber(String visitorMobileNumber) {
            this.visitorMobileNumber = visitorMobileNumber;
        }

        public String getVisitorName() {
            return visitorName;
        }

        public void setVisitorName(String visitorName) {
            this.visitorName = visitorName;
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

        public String getWhomToMeet() {
            return whomToMeet;
        }

        public void setWhomToMeet(String whomToMeet) {
            this.whomToMeet = whomToMeet;
        }

        public String getBody_temperature() {
            return body_temperature;
        }

        public void setBody_temperature(String body_temperature) {
            this.body_temperature = body_temperature;
        }
    }

}
