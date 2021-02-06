
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SearchResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("status")
    private int mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }



    public class Result {

        @SerializedName("community_id")
        private String mCompanyId;
        @SerializedName("date")
        private String mDate;
        @SerializedName("e_id")
        private int mEId;
        @SerializedName("id")
        private int mId;
        @SerializedName("otp")
        private String mOtp;
        @SerializedName("sec_id")
        private int mSecId;
        @SerializedName("select_time")
        private String mSelectTime;
        @SerializedName("select_week")
        private int mSelectWeek;
        @SerializedName("vehicle_number")
        private String mVehicleNumber;
        @SerializedName("vehicle_type")
        private String mVehicleType;
        @SerializedName("visitor_location")
        private String mVisitorLocation;
        @SerializedName("visitor_mobile_number")
        private String mVisitorMobileNumber;
        @SerializedName("visitor_name")
        private String mVisitorName;
        @SerializedName("visitor_type")
        private int mVisitorType;

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

        public int getEId() {
            return mEId;
        }

        public void setEId(int eId) {
            mEId = eId;
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getOtp() {
            return mOtp;
        }

        public void setOtp(String otp) {
            mOtp = otp;
        }

        public int getSecId() {
            return mSecId;
        }

        public void setSecId(int secId) {
            mSecId = secId;
        }

        public String getSelectTime() {
            return mSelectTime;
        }

        public void setSelectTime(String selectTime) {
            mSelectTime = selectTime;
        }

        public int getSelectWeek() {
            return mSelectWeek;
        }

        public void setSelectWeek(int selectWeek) {
            mSelectWeek = selectWeek;
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

        public int getVisitorType() {
            return mVisitorType;
        }

        public void setVisitorType(int visitorType) {
            mVisitorType = visitorType;
        }

    }

    
}
