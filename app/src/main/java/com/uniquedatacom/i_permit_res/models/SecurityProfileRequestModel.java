
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SecurityProfileRequestModel {

    @SerializedName("sec_id")
    private int mSecId;

    public int getSecId() {
        return mSecId;
    }

    public void setSecId(int secId) {
        mSecId = secId;
    }

}
