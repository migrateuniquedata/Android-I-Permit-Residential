
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmployeeLoginResponseModel {

    @SerializedName("login_status")
    private Long mLoginStatus;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private Long mStatus;

    public Long getLoginStatus() {
        return mLoginStatus;
    }

    public void setLoginStatus(Long loginStatus) {
        mLoginStatus = loginStatus;
    }

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
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("e_id")
        @Expose
        private Integer eId;
        @SerializedName("email_id")
        @Expose
        private String emailId;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("language_type")
        @Expose
        private String languageType;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("user_pic")
        @Expose
        private String userPic;
        @SerializedName("verify_status")
        @Expose
        private Boolean verifyStatus;

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

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public Integer getEId() {
            return eId;
        }

        public void setEId(Integer eId) {
            this.eId = eId;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLanguageType() {
            return languageType;
        }

        public void setLanguageType(String languageType) {
            this.languageType = languageType;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

        public Boolean getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(Boolean verifyStatus) {
            this.verifyStatus = verifyStatus;
        }

    }


}
