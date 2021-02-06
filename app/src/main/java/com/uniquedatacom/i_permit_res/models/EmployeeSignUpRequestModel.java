
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmployeeSignUpRequestModel {
    @SerializedName("device_token")
    private String device_token;
    @SerializedName("community_id")
    private String mCompanyId;
    @SerializedName("confirm_password")
    private String mConfirmPassword;
    @SerializedName("email_id")
    private String mEmailId;
    @SerializedName("flat_number")
    private String flat_number;
    @SerializedName("block_number")
    private String block_number;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("language_type")
    private String mLanguageType;
    @SerializedName("mobile_number")
    private String mMobileNumber;
    @SerializedName("name")
    private String mName;
//    @SerializedName("otp")
//    private String mOtp;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("type")
    private String mType;

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }

    public String getConfirmPassword() {
        return mConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        mConfirmPassword = confirmPassword;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String emailId) {
        mEmailId = emailId;
    }

    public String getFlat_number() {
        return flat_number;
    }

    public void setFlat_number(String flat_number) {
        this.flat_number = flat_number;
    }

    public String getBlock_number() {
        return block_number;
    }

    public void setBlock_number(String block_number) {
        this.block_number = block_number;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getLanguageType() {
        return mLanguageType;
    }

    public void setLanguageType(String languageType) {
        mLanguageType = languageType;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

//    public String getOtp() {
//        return mOtp;
//    }
//
//    public void setOtp(String otp) {
//        mOtp = otp;
//    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
