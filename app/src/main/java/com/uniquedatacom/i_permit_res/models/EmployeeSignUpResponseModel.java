
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmployeeSignUpResponseModel {

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
        @SerializedName("e_id")
        private Long mEId;
        @SerializedName("email_id")
        private String mEmailId;
        @SerializedName("emp_code")
        private String mEmpCode;
        @SerializedName("language")
        private String mLanguage;
        @SerializedName("mobile_number")
        private String mMobileNumber;
        @SerializedName("name")
        private String mName;
        @SerializedName("otp")
        private String mOtp;
        @SerializedName("password")
        private String mPassword;

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

        public Long getEId() {
            return mEId;
        }

        public void setEId(Long eId) {
            mEId = eId;
        }

        public String getEmailId() {
            return mEmailId;
        }

        public void setEmailId(String emailId) {
            mEmailId = emailId;
        }

        public String getEmpCode() {
            return mEmpCode;
        }

        public void setEmpCode(String empCode) {
            mEmpCode = empCode;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public void setLanguage(String language) {
            mLanguage = language;
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

        public String getOtp() {
            return mOtp;
        }

        public void setOtp(String otp) {
            mOtp = otp;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

    }


}
