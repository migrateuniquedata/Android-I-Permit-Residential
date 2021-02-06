
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetSecurityPreApprovedListResponseModel {

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
        private String mBodyTemperature;
        @SerializedName("community_id")
        private String mCompanyId;
        @SerializedName("date")
        private String mDate;
        @SerializedName("duration")
        private String mDuration;
        @SerializedName("in_time")
        private String mInTime;
        @SerializedName("outgoingtime")
        private String mOutgoingtime;
        @SerializedName("select_time")
        private String mSelectTime;
        @SerializedName("select_week")
        private Long mSelectWeek;
        @SerializedName("vehicle_type")
        private String mVehicleType;
        @SerializedName("visitor_location")
        private String mVisitorLocation;
        @SerializedName("visitor_mobile_number")
        private String mVisitorMobileNumber;
        @SerializedName("visitor_name")
        private String mVisitorName;

        public String getBodyTemperature() {
            return mBodyTemperature;
        }

        public void setBodyTemperature(String bodyTemperature) {
            mBodyTemperature = bodyTemperature;
        }

        public String getCompanyId() {
            return mCompanyId;
        }

        public void setCompanyId(String companyId) {
            mCompanyId = companyId;
        }

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public String getDuration() {
            return mDuration;
        }

        public void setDuration(String duration) {
            mDuration = duration;
        }

        public String getInTime() {
            return mInTime;
        }

        public void setInTime(String inTime) {
            mInTime = inTime;
        }

        public String getOutgoingtime() {
            return mOutgoingtime;
        }

        public void setOutgoingtime(String outgoingtime) {
            mOutgoingtime = outgoingtime;
        }

        public String getSelectTime() {
            return mSelectTime;
        }

        public void setSelectTime(String selectTime) {
            mSelectTime = selectTime;
        }

        public Long getSelectWeek() {
            return mSelectWeek;
        }

        public void setSelectWeek(Long selectWeek) {
            mSelectWeek = selectWeek;
        }

        public String getVehicleType() {
            return mVehicleType;
        }

        public void setVehicleType(String vehicleType) {
            mVehicleType = vehicleType;
        }

        public String getVisitorLocation() {
            return mVisitorLocation;
        }

        public void setVisitorLocation(String visitorLocation) {
            mVisitorLocation = visitorLocation;
        }

        public String getVisitorMobileNumber() {
            return mVisitorMobileNumber;
        }

        public void setVisitorMobileNumber(String visitorMobileNumber) {
            mVisitorMobileNumber = visitorMobileNumber;
        }

        public String getVisitorName() {
            return mVisitorName;
        }

        public void setVisitorName(String visitorName) {
            mVisitorName = visitorName;
        }

    }


}
