
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class VisitorSignUpResponseModel {

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

        @SerializedName("block_number")
        @Expose
        private String blockNumber;
        @SerializedName("body_temperature")
        @Expose
        private String bodyTemperature;
        @SerializedName("community_name")
        @Expose
        private String communityName;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("e_id")
        @Expose
        private Integer eId;
        @SerializedName("flat_number")
        @Expose
        private String flatNumber;
        @SerializedName("mask")
        @Expose
        private Integer mask;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("reason_for_visit")
        @Expose
        private String reasonForVisit;
        @SerializedName("sec_id")
        @Expose
        private Integer secId;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("user_pic")
        @Expose
        private String userPic;
        @SerializedName("visit_id")
        @Expose
        private Integer visitId;
        @SerializedName("visiter_name")
        @Expose
        private String visiterName;
        @SerializedName("visitor_location")
        @Expose
        private String visitorLocation;
        @SerializedName("whom_to_meet")
        @Expose
        private String whomToMeet;

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getBodyTemperature() {
            return bodyTemperature;
        }

        public void setBodyTemperature(String bodyTemperature) {
            this.bodyTemperature = bodyTemperature;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public Integer getEId() {
            return eId;
        }

        public void setEId(Integer eId) {
            this.eId = eId;
        }

        public String getFlatNumber() {
            return flatNumber;
        }

        public void setFlatNumber(String flatNumber) {
            this.flatNumber = flatNumber;
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

        public String getReasonForVisit() {
            return reasonForVisit;
        }

        public void setReasonForVisit(String reasonForVisit) {
            this.reasonForVisit = reasonForVisit;
        }

        public Integer getSecId() {
            return secId;
        }

        public void setSecId(Integer secId) {
            this.secId = secId;
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

        public String getVisiterName() {
            return visiterName;
        }

        public void setVisiterName(String visiterName) {
            this.visiterName = visiterName;
        }

        public String getVisitorLocation() {
            return visitorLocation;
        }

        public void setVisitorLocation(String visitorLocation) {
            this.visitorLocation = visitorLocation;
        }

        public String getWhomToMeet() {
            return whomToMeet;
        }

        public void setWhomToMeet(String whomToMeet) {
            this.whomToMeet = whomToMeet;
        }

    }

}
