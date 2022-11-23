package com.bitcnew.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransformersBean implements Serializable {

    private String miningRateDesc;
    private String riskRateDesc;
    private List<TransformersBean1> transformers;
    private List<?> banner;

    public static List<TransformersBean> arrayTransformersBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<TransformersBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getMiningRateDesc() {
        return miningRateDesc;
    }

    public void setMiningRateDesc(String miningRateDesc) {
        this.miningRateDesc = miningRateDesc;
    }

    public String getRiskRateDesc() {
        return riskRateDesc;
    }

    public void setRiskRateDesc(String riskRateDesc) {
        this.riskRateDesc = riskRateDesc;
    }

    public List<TransformersBean1> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<TransformersBean1> transformers) {
        this.transformers = transformers;
    }

    public List<?> getBanner() {
        return banner;
    }

    public void setBanner(List<?> banner) {
        this.banner = banner;
    }

    public static class TransformersBean1 implements Serializable {
        private String bannerId;
        private String cls;
        private String createTime;
        private String downTime;
        private String imageUrl;
        private String params;
        private String pkg;
        private String position;
        private String pview;
        private String sortNum;
        private String state;
        private String type;
        private String upTime;
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
