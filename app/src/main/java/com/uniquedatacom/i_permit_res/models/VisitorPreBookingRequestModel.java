
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")

//"visitor_type": 0 --> once
//        "visitor_type": 1--> permit

/*sunday-0
        monday-1
        tuesday-2
        wed-3
        thu-4
        fri-5
        sat-6 */
public class VisitorPreBookingRequestModel {

    @SerializedName("community_id")
    private String mCompanyId;
    @SerializedName("date")
    private String mDate;
    @SerializedName("e_id")
    private int mEId;
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
    @SerializedName("visitor_name")
    private String mVisitorName;
    @SerializedName("visitor_type")
    private int mVisitorType;
    @SerializedName("visitor_mobile_number")
    private String visitor_mobile_number;

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

    public String getVisitor_mobile_number() {
        return visitor_mobile_number;
    }

    public void setVisitor_mobile_number(String visitor_mobile_number) {
        this.visitor_mobile_number = visitor_mobile_number;
    }
}
