
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetSecNameListResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private int mStatus;

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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public class Result {

        @SerializedName("name")
        private String mName;
        @SerializedName("sec_id")
        private int mSecId;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public int getSecId() {
            return mSecId;
        }

        public void setSecId(int secId) {
            mSecId = secId;
        }

    }


}
