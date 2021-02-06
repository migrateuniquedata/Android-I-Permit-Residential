
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpPreApprovedResponseModel {

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

        @SerializedName("community_id")
        @Expose
        private String companyId;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("in_time")
        @Expose
        private String inTime;
        @SerializedName("outgoingtime")
        @Expose
        private String outgoingtime;
        @SerializedName("select_time")
        @Expose
        private String selectTime;
        @SerializedName("select_week")
        @Expose
        private Integer selectWeek;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("visitor_location")
        @Expose
        private String visitorLocation;
        @SerializedName("visitor_mobile_number")
        @Expose
        private String visitorMobileNumber;
        @SerializedName("visitor_name")
        @Expose
        private String visitorName;
        @SerializedName("body_temperature")
        @Expose
        private String body_temperature;
        @SerializedName("verify_status")
        @Expose
        private boolean verify_status;


        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

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

        public String getSelectTime() {
            return selectTime;
        }

        public void setSelectTime(String selectTime) {
            this.selectTime = selectTime;
        }

        public Integer getSelectWeek() {
            return selectWeek;
        }

        public void setSelectWeek(Integer selectWeek) {
            this.selectWeek = selectWeek;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
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

        public String getBody_temperature() {
            return body_temperature;
        }

        public void setBody_temperature(String body_temperature) {
            this.body_temperature = body_temperature;
        }

        public boolean isVerify_status() {
            return verify_status;
        }

        public void setVerify_status(boolean verify_status) {
            this.verify_status = verify_status;
        }
    }

}
