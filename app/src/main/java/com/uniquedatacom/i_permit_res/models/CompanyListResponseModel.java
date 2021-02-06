
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CompanyListResponseModel {

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
        @SerializedName("company_name")
        private String mCompanyName;
        @SerializedName("created_time")
        private String mCreatedTime;
        @SerializedName("email_id")
        private String mEmailId;

        public String getCompanyId() {
            return mCompanyId;
        }

        public void setCompanyId(String companyId) {
            mCompanyId = companyId;
        }

        public String getCompanyName() {
            return mCompanyName;
        }

        public void setCompanyName(String companyName) {
            mCompanyName = companyName;
        }

        public String getCreatedTime() {
            return mCreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime = createdTime;
        }

        public String getEmailId() {
            return mEmailId;
        }

        public void setEmailId(String emailId) {
            mEmailId = emailId;
        }

    }


}
