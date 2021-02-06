
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SecurityProfileResponseModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private Long mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Result> getResult() {
        return mResult;
    }

    public void setResult(List<Result> result) {
        mResult = result;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }


    public class Result {

        @SerializedName("community_id")
        private String mCompanyId;
        @SerializedName("department")
        private String mDepartment;
        @SerializedName("emp_code")
        private String mEmpCode;
        @SerializedName("name")
        private String mName;
        @SerializedName("user_pic")
        private String mUserPic;

        public String getCompanyId() {
            return mCompanyId;
        }

        public void setCompanyId(String companyId) {
            mCompanyId = companyId;
        }

        public String getDepartment() {
            return mDepartment;
        }

        public void setDepartment(String department) {
            mDepartment = department;
        }

        public String getEmpCode() {
            return mEmpCode;
        }

        public void setEmpCode(String empCode) {
            mEmpCode = empCode;
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

    }

}
