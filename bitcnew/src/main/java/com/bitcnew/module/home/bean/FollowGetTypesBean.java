package com.bitcnew.module.home.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FollowGetTypesBean implements Serializable {

    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private Data data;
    @SerializedName("msg")
    private String msg;
    @SerializedName("success")
    private String success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public static class Data implements Serializable {
        @SerializedName("showBind")
        private String showBind;
        @SerializedName("types")
        private List<Types> types;

        public String getShowBind() {
            return showBind;
        }

        public void setShowBind(String showBind) {
            this.showBind = showBind;
        }

        public List<Types> getTypes() {
            return types;
        }

        public void setTypes(List<Types> types) {
            this.types = types;
        }
    }

    public static class Types implements Serializable {
        @SerializedName("id")
        private long id;
        @SerializedName("dvUid")
        private long dvUid;
        @SerializedName("profitRate")
        private String profitRate;
        @SerializedName("lossRate")
        private String lossRate;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("remark")
        private String remark;
        @SerializedName("updateTime")
        private String updateTime;
        @SerializedName("limit")
        private String limit;
        @SerializedName("duration")
        private String duration;
        @SerializedName("expireTime")
        private long expireTime;
        @SerializedName("isRenewal")
        private int isRenewal;
        @SerializedName("accountType")
        private int accountType;
        @SerializedName("isBind")
        private int isBind;
        @SerializedName("maxMultiNum")
        private String maxMultiNum;
        @SerializedName("minMultiNum")
        private String minMultiNum;
        @SerializedName("tokenAmount")
        private String tokenAmount;

        public String getTokenAmount() {
            return tokenAmount;
        }

        public void setTokenAmount(String tokenAmount) {
            this.tokenAmount = tokenAmount;
        }

        public String getMaxMultiNum() {
            return maxMultiNum;
        }

        public void setMaxMultiNum(String maxMultiNum) {
            this.maxMultiNum = maxMultiNum;
        }
        public String getMinMultiNum() {
            return minMultiNum;
        }

        public void setMinMultiNum(String minMultiNum) {
            this.minMultiNum = minMultiNum;
        }


        public long getExpireTime(){
            return expireTime;
        }

        public void setExpireTime(long isBind){
            this.expireTime = isBind;
        }

        public int getIsBind(){
            return isBind;
        }

        public void setIsBind(int isBind){
            this.isBind = isBind;
        }

        private boolean isSel;

        public boolean isSel() {
            return isSel;
        }

        public void setSel(boolean sel) {
            isSel = sel;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getDvUid() {
            return dvUid;
        }

        public void setDvUid(long dvUid) {
            this.dvUid = dvUid;
        }

        public String getProfitRate() {
            return profitRate;
        }

        public void setProfitRate(String profitRate) {
            this.profitRate = profitRate;
        }

        public String getLossRate() {
            return lossRate;
        }

        public void setLossRate(String lossRate) {
            this.lossRate = lossRate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getIsRenewal() {
            return isRenewal;
        }

        public void setIsRenewal(int isRenewal) {
            this.isRenewal = isRenewal;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }




    }
}
