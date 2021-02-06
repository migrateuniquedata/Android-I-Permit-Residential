
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ValidateTimeRequestModel {

    @SerializedName("select_time")
    private String mSelectTime;

    public String getSelectTime() {
        return mSelectTime;
    }

    public void setSelectTime(String selectTime) {
        mSelectTime = selectTime;
    }

}
