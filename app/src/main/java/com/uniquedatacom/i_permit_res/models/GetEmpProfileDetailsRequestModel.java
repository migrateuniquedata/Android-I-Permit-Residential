
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;
@SuppressWarnings("unused")
public class GetEmpProfileDetailsRequestModel {

    @SerializedName("e_id")
    private Long mEId;

    public Long getEId() {
        return mEId;
    }

    public void setEId(Long eId) {
        mEId = eId;
    }

}
