
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmployeeLoginRequestModel {
    @SerializedName("device_token")
    private String device_token;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("language_type")
    private String mLanguageType;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("type")
    private String mType;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("community_id")
    private String community_id;


    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getLanguageType() {
        return mLanguageType;
    }

    public void setLanguageType(String languageType) {
        mLanguageType = languageType;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getCompany_id() {
        return community_id;
    }

    public void setCompany_id(String community_id) {
        this.community_id = community_id;
    }
}
