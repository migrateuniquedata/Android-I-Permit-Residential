
package com.uniquedatacom.i_permit_res.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ResidentDetailsResponseModel {

    @SerializedName("result")
    private List<Result> mResult;
    @SerializedName("status")
    private int mStatus;

    public List<Result> getResult() {
        return mResult;
    }

    public void setResult(List<Result> result) {
        mResult = result;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public class Result {

        @SerializedName("block_number")
        private String mBlockNumber;
        @SerializedName("e_id")
        private int mEId;
        @SerializedName("flat_number")
        private String mFlatNumber;
        @SerializedName("resident_name")
        private String mResidentName;

        public String getBlockNumber() {
            return mBlockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            mBlockNumber = blockNumber;
        }

        public int getEId() {
            return mEId;
        }

        public void setEId(int eId) {
            mEId = eId;
        }

        public String getFlatNumber() {
            return mFlatNumber;
        }

        public void setFlatNumber(String flatNumber) {
            mFlatNumber = flatNumber;
        }

        public String getResidentName() {
            return mResidentName;
        }

        public void setResidentName(String residentName) {
            mResidentName = residentName;
        }
    }

}
