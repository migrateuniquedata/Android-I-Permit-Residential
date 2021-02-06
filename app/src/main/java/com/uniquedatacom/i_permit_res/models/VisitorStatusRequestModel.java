
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VisitorStatusRequestModel {

    @SerializedName("status")
    private int mStatus;
    @SerializedName("visit_id")
    private int mVisitId;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getVisitId() {
        return mVisitId;
    }

    public void setVisitId(int visitId) {
        mVisitId = visitId;
    }

}
