package com.bitcnew.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgentInfoBean implements Serializable {

    @SerializedName("agent")
    private Agent agent;
    @SerializedName("status")
    private String status;
    @SerializedName("upgradeFlag")
    private boolean upgradeFlag;

    public boolean isUpgradeFlag() {
        return upgradeFlag;
    }

    public void setUpgradeFlag(boolean upgradeFlag) {
        this.upgradeFlag = upgradeFlag;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Agent implements Serializable {
        @SerializedName("inviteCode")
        private String inviteCode;
        @SerializedName("agentType")
        private String agentType;
        @SerializedName("levelId")
        private String levelId;
        @SerializedName("levelName")
        private String levelName;
        @SerializedName("sumCommission")
        private String sumCommission;
        @SerializedName("todayAdd")
        private String todayAdd;
        @SerializedName("todayCommission")
        private String todayCommission;
        @SerializedName("todayTrade")
        private String todayTrade;
        @SerializedName("userId")
        private String userId;
        @SerializedName("yesterdayAdd")
        private String yesterdayAdd;
        @SerializedName("yesterdayCommission")
        private String yesterdayCommission;
        @SerializedName("yesterdayTrade")
        private String yesterdayTrade;

        @SerializedName("sumMyAdd")
        private String sumMyAdd;
        @SerializedName("remainInviteCount")
        private String remainInviteCount;
        @SerializedName("upgradeAmout")
        private String upgradeAmout;
        @SerializedName("upgradeName")
        private String upgradeName;

        public String getSumMyAdd() {
            return sumMyAdd;
        }
        public void setSumMyAdd(String sumMyAdd) {
            this.sumMyAdd = sumMyAdd;
        }

        public String getRemainInviteCount() {
            return remainInviteCount;
        }
        public void setRemainInviteCount(String remainInviteCount) {
            this.remainInviteCount = remainInviteCount;
        }

        public String getUpgradeAmout() {
            return upgradeAmout;
        }
        public void setUpgradeAmout(String upgradeAmout) {
            this.upgradeAmout = upgradeAmout;
        }

        public String getUpgradeName() {
            return upgradeName;
        }
        public void setUpgradeName(String upgradeName) {
            this.upgradeName = upgradeName;
        }




        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getAgentType() {
            return agentType;
        }

        public void setAgentType(String agentType) {
            this.agentType = agentType;
        }

        public String getLevelId() {
            return levelId;
        }

        public void setLevelId(String levelId) {
            this.levelId = levelId;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getSumCommission() {
            return sumCommission;
        }

        public void setSumCommission(String sumCommission) {
            this.sumCommission = sumCommission;
        }

        public String getTodayAdd() {
            return todayAdd;
        }

        public void setTodayAdd(String todayAdd) {
            this.todayAdd = todayAdd;
        }

        public String getTodayCommission() {
            return todayCommission;
        }

        public void setTodayCommission(String todayCommission) {
            this.todayCommission = todayCommission;
        }

        public String getTodayTrade() {
            return todayTrade;
        }

        public void setTodayTrade(String todayTrade) {
            this.todayTrade = todayTrade;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getYesterdayAdd() {
            return yesterdayAdd;
        }

        public void setYesterdayAdd(String yesterdayAdd) {
            this.yesterdayAdd = yesterdayAdd;
        }

        public String getYesterdayCommission() {
            return yesterdayCommission;
        }

        public void setYesterdayCommission(String yesterdayCommission) {
            this.yesterdayCommission = yesterdayCommission;
        }

        public String getYesterdayTrade() {
            return yesterdayTrade;
        }

        public void setYesterdayTrade(String yesterdayTrade) {
            this.yesterdayTrade = yesterdayTrade;
        }
    }
}
