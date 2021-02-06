
package com.uniquedatacom.i_permit_res.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpProfileSettingsResponseModel {

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
        @SerializedName("e_id")
        @Expose
        private Integer eId;
        @SerializedName("email_id")
        @Expose
        private String emailId;
        @SerializedName("flat_number")
        @Expose
        private String flatNumber;
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
        @SerializedName("user_pic")
        @Expose
        private String userPic;

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
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

        public String getFlatNumber() {
            return flatNumber;
        }

        public void setFlatNumber(String flatNumber) {
            this.flatNumber = flatNumber;
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

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

    }

}
