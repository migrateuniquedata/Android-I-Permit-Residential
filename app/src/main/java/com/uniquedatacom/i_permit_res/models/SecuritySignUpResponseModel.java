
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class SecuritySignUpResponseModel {

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
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("email_id")
        @Expose
        private String emailId;
        @SerializedName("emp_code")
        @Expose
        private String empCode;
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
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("sec_id")
        @Expose
        private Integer secId;
        @SerializedName("type")
        @Expose
        private String type;

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

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getEmpCode() {
            return empCode;
        }

        public void setEmpCode(String empCode) {
            this.empCode = empCode;
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

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getSecId() {
            return secId;
        }

        public void setSecId(Integer secId) {
            this.secId = secId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
