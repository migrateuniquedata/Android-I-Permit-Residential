
package com.uniquedatacom.i_permit_res.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SecurityLoginRequestModel {
    @SerializedName("device_token")
    private String device_token;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("username")
    private String mUsername;

    @SerializedName("language")
    private String language;

    @SerializedName("language_type")
    private String language_type;

    @SerializedName("type")
    private String type;

    @SerializedName("community_id")
    private String community_id;


    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage_type() {
        return language_type;
    }

    public void setLanguage_type(String language_type) {
        this.language_type = language_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany_id() {
        return community_id;
    }

    public void setCompany_id(String community_id) {
        this.community_id = community_id;
    }
}
