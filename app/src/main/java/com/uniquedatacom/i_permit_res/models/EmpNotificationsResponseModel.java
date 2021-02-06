
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpNotificationsResponseModel {

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
        private Long mEId;
        @SerializedName("message_body")
        private String mMessageBody;
        @SerializedName("message_title")
        private String mMessageTitle;
        @SerializedName("sender")
        private Long mSender;
        @SerializedName("time")
        private String time;


        public Long getEId() {
            return mEId;
        }

        public void setEId(Long eId) {
            mEId = eId;
        }

        public String getMessageBody() {
            return mMessageBody;
        }

        public void setMessageBody(String messageBody) {
            mMessageBody = messageBody;
        }

        public String getMessageTitle() {
            return mMessageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            mMessageTitle = messageTitle;
        }

        public Long getSender() {
            return mSender;
        }

        public void setSender(Long sender) {
            mSender = sender;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }

}
