
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpLogsResponseModel {

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
        @SerializedName("in_time")
        @Expose
        private String createdTime;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("outgoingtime")
        @Expose
        private String outgoingtime;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("user_pic")
        @Expose
        private String userPic;
        @SerializedName("visit_id")
        @Expose
        private Integer visitId;
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

        public boolean getVerify_status() {
            return verify_status;
        }

        public void setVerify_status(boolean verify_status) {
            this.verify_status = verify_status;
        }
        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getOutgoingtime() {
            return outgoingtime;
        }

        public void setOutgoingtime(String outgoingtime) {
            this.outgoingtime = outgoingtime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
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
    }

}
