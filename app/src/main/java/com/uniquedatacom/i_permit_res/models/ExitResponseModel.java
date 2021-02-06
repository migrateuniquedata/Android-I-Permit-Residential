
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ExitResponseModel {

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

        @SerializedName("outgoingtime")
        private String mOutgoingtime;
        @SerializedName("visit_id")
        private int mVisitId;

        public String getOutgoingtime() {
            return mOutgoingtime;
        }

        public void setOutgoingtime(String outgoingtime) {
            mOutgoingtime = outgoingtime;
        }

        public int getVisitId() {
            return mVisitId;
        }

        public void setVisitId(int visitId) {
            mVisitId = visitId;
        }

    }

}
