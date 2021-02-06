
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpProfileSettingsRequestModel {

    @SerializedName("flat_number")
    private String flat_number;
    @SerializedName("block_number")
    private String block_number;
    @SerializedName("e_id")
    private int mEId;
    @SerializedName("mobile_number")
    private String mobile_number;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("name")
    private String mName;
    @SerializedName("user_pic")
    private String mUserPic;

    public int getEId() {
        return mEId;
    }

    public void setEId(int eId) {
        mEId = eId;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUserPic() {
        return mUserPic;
    }

    public void setUserPic(String userPic) {
        mUserPic = userPic;
    }

    public String getFlat_number() {
        return flat_number;
    }

    public void setFlat_number(String flat_number) {
        this.flat_number = flat_number;
    }

    public String getBlock_number() {
        return block_number;
    }

    public void setBlock_number(String block_number) {
        this.block_number = block_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
}
