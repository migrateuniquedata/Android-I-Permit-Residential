
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VersionRequestModel {

    @SerializedName("version")
    private String mVersion;

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

}
