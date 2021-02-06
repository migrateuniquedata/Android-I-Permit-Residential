
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SecNotificationsResponseModel {

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

        @SerializedName("message_body")
        private String mMessageBody;
        @SerializedName("message_title")
        private String mMessageTitle;
        @SerializedName("sec_id")
        private Long mSecId;
        @SerializedName("sender")
        private Long mSender;
        @SerializedName("time")
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public Long getSecId() {
            return mSecId;
        }

        public void setSecId(Long secId) {
            mSecId = secId;
        }

        public Long getSender() {
            return mSender;
        }

        public void setSender(Long sender) {
            mSender = sender;
        }

    }



}
