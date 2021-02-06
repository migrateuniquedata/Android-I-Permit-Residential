
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetNameListRequestModel {

    @SerializedName("community_id")
    private String mCompanyId;

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }

}
