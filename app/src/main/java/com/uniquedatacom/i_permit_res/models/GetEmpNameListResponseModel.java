
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetEmpNameListResponseModel {

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

        @SerializedName("e_id")
        private int mEId;
        @SerializedName("name")
        private String mName;

        public int getEId() {
            return mEId;
        }

        public void setEId(int eId) {
            mEId = eId;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

    }

}
