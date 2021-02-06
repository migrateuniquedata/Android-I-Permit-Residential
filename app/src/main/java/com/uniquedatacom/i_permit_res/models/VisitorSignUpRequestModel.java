
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VisitorSignUpRequestModel {

    @SerializedName("whom_to_meet")
    @Expose
    private String whomToMeet;
    @SerializedName("name_of_the_building")
    @Expose
    private String nameOfTheBuilding;
    @SerializedName("user_pic")
    @Expose
    private String userPic;
    @SerializedName("reason_for_visit")
    @Expose
    private String reasonForVisit;
    @SerializedName("sec_id")
    @Expose
    private Integer secId;
    @SerializedName("visiter_name")
    @Expose
    private String visiterName;
    @SerializedName("flat_number")
    @Expose
    private String flatNumber;
    @SerializedName("block_number")
    @Expose
    private String blockNumber;
    @SerializedName("body_temperature")
    @Expose
    private String bodyTemperature;
    @SerializedName("e_id")
    @Expose
    private Integer eId;
    @SerializedName("mask")
    @Expose
    private Integer mask;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("visitor_location")
    @Expose
    private String visitorLocation;

    public String getWhomToMeet() {
        return whomToMeet;
    }

    public void setWhomToMeet(String whomToMeet) {
        this.whomToMeet = whomToMeet;
    }

    public String getNameOfTheBuilding() {
        return nameOfTheBuilding;
    }

    public void setNameOfTheBuilding(String nameOfTheBuilding) {
        this.nameOfTheBuilding = nameOfTheBuilding;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
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

    public String getVisiterName() {
        return visiterName;
    }

    public void setVisiterName(String visiterName) {
        this.visiterName = visiterName;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

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

    public Integer getEId() {
        return eId;
    }

    public void setEId(Integer eId) {
        this.eId = eId;
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

    public String getVisitorLocation() {
        return visitorLocation;
    }

    public void setVisitorLocation(String visitorLocation) {
        this.visitorLocation = visitorLocation;
    }

}