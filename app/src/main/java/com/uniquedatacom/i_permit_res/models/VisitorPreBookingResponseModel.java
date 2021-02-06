
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VisitorPreBookingResponseModel {

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
        private String mCompanyId;
        @SerializedName("created_time")
        private String mCreatedTime;
        @SerializedName("date")
        private String mDate;
        @SerializedName("e_id")
        private Long mEId;
        @SerializedName("id")
        private Long mId;
        @SerializedName("sec_id")
        private Long mSecId;
        @SerializedName("select_time")
        private String mSelectTime;
        @SerializedName("select_week")
        private String mSelectWeek;
        @SerializedName("status")
        private Long mStatus;
        @SerializedName("vehicle_number")
        private String mVehicleNumber;
        @SerializedName("vehicle_type")
        private String mVehicleType;
        @SerializedName("visitor_location")
        private String mVisitorLocation;
        @SerializedName("visitor_name")
        private String mVisitorName;
        @SerializedName("visitor_type")
        private Long mVisitorType;
        @SerializedName("body_temperature")
        @Expose
        private String body_temperature;


        public String getCompanyId() {
            return mCompanyId;
        }

        public void setCompanyId(String companyId) {
            mCompanyId = companyId;
        }

        public String getCreatedTime() {
            return mCreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime = createdTime;
        }

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public Long getEId() {
            return mEId;
        }

        public void setEId(Long eId) {
            mEId = eId;
        }

        public Long getId() {
            return mId;
        }

        public void setId(Long id) {
            mId = id;
        }

        public Long getSecId() {
            return mSecId;
        }

        public void setSecId(Long secId) {
            mSecId = secId;
        }

        public String getSelectTime() {
            return mSelectTime;
        }

        public void setSelectTime(String selectTime) {
            mSelectTime = selectTime;
        }

        public String getSelectWeek() {
            return mSelectWeek;
        }

        public void setSelectWeek(String selectWeek) {
            mSelectWeek = selectWeek;
        }

        public Long getStatus() {
            return mStatus;
        }

        public void setStatus(Long status) {
            mStatus = status;
        }

        public String getVehicleNumber() {
            return mVehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            mVehicleNumber = vehicleNumber;
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

        public String getVisitorName() {
            return mVisitorName;
        }

        public void setVisitorName(String visitorName) {
            mVisitorName = visitorName;
        }

        public Long getVisitorType() {
            return mVisitorType;
        }

        public void setVisitorType(Long visitorType) {
            mVisitorType = visitorType;
        }

        public String getBody_temperature() {
            return body_temperature;
        }

        public void setBody_temperature(String body_temperature) {
            this.body_temperature = body_temperature;
        }
    }


}
