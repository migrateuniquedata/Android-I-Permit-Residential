
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ExitRequestModel {

    @SerializedName("visit_id")
    private int mVisitId;
    @SerializedName("id")
    private int mId;

    public int getVisitId() {
        return mVisitId;
    }

    public void setVisitId(int visitId) {
        mVisitId = visitId;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
