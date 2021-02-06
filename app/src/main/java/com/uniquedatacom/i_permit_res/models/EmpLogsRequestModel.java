
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpLogsRequestModel {

    @SerializedName("e_id")
    private int mEId;

    public int getEId() {
        return mEId;
    }

    public void setEId(int eId) {
        mEId = eId;
    }

}
