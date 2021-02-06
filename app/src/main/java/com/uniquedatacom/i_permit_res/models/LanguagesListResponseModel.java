
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class LanguagesListResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private String mStatus;

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

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }



    public class Result {

        @SerializedName("code")
        private String mCode;
        @SerializedName("name")
        private String mName;

        public String getCode() {
            return mCode;
        }

        public void setCode(String code) {
            mCode = code;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

    }


}
