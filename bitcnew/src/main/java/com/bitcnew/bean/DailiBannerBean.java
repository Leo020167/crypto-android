package com.bitcnew.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DailiBannerBean implements Serializable {

    @SerializedName("agentFee")
    private String agentFee;
    @SerializedName("banner")
    private List<Banner> banner;

    public String getAgentFee() {
        return agentFee;
    }

    public void setAgentFee(String agentFee) {
        this.agentFee = agentFee;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public static class Banner implements Serializable {
        @SerializedName("bannerId")
        private String bannerId;
        @SerializedName("cls")
        private String cls;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("downTime")
        private String downTime;
        @SerializedName("imageUrl")
        private String imageUrl;
        @SerializedName("params")
        private String params;
        @SerializedName("pkg")
        private String pkg;
        @SerializedName("position")
        private String position;
        @SerializedName("pview")
        private String pview;
        @SerializedName("sortNum")
        private String sortNum;
        @SerializedName("state")
        private String state;
        @SerializedName("type")
        private String type;
        @SerializedName("upTime")
        private String upTime;
        @SerializedName("videoUrl")
        private String videoUrl;

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getCls() {
            return cls;
        }

        public void setCls(String cls) {
            this.cls = cls;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDownTime() {
            return downTime;
        }

        public void setDownTime(String downTime) {
            this.downTime = downTime;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getPkg() {
            return pkg;
        }

        public void setPkg(String pkg) {
            this.pkg = pkg;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPview() {
            return pview;
        }

        public void setPview(String pview) {
            this.pview = pview;
        }

        public String getSortNum() {
            return sortNum;
        }

        public void setSortNum(String sortNum) {
            this.sortNum = sortNum;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpTime() {
            return upTime;
        }

        public void setUpTime(String upTime) {
            this.upTime = upTime;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }
}
