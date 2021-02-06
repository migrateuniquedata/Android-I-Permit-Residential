
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ActivityVisitorsSecResponseModel {

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

        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("in_time")
        @Expose
        private String inTime;
        @SerializedName("outgoingtime")
        @Expose
        private String outgoingtime;
        @SerializedName("sec_id")
        @Expose
        private Integer secId;
        @SerializedName("visitor_location")
        @Expose
        private String visitorLocation;
        @SerializedName("visitor_mobile_number")
        @Expose
        private String visitorMobileNumber;
        @SerializedName("mobile_number")
        @Expose
        private String mobile_number;
        @SerializedName("visitor_name")
        @Expose
        private String visitorName;
        @SerializedName("user_pic")
        @Expose
        private String user_pic;
        @SerializedName("whom_to_meet")
        @Expose
        private String whom_to_meet;
        @SerializedName("body_temperature")
        @Expose
        private String body_temperature;


        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
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

        public Integer getSecId() {
            return secId;
        }

        public void setSecId(Integer secId) {
            this.secId = secId;
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

        public String getUser_pic() {
            return user_pic;
        }

        public void setUser_pic(String user_pic) {
            this.user_pic = user_pic;
        }

        public String getWhom_to_meet() {
            return whom_to_meet;
        }

        public void setWhom_to_meet(String whom_to_meet) {
            this.whom_to_meet = whom_to_meet;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getBody_temperature() {
            return body_temperature;
        }

        public void setBody_temperature(String body_temperature) {
            this.body_temperature = body_temperature;
        }
    }

}
