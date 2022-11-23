package com.bitcnew.module.home.bean;

import java.io.Serializable;
import java.util.List;

public class InviteHomeBean implements Serializable {

    /**
     * inviteList : [{"amount":"80.000","inviteCode":"122345","inviteUserId":"2","status":"0","userId":"2009462"},{"amount":"100.000","inviteCode":"122345","inviteUserId":"3","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"122345","inviteUserId":"4","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"8HKHLD","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"SADM9L","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"AFLN57","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"185ZHD","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"1RYTPB","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"3RA1SI","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"F1YSUF","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"6HPY3P","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"JO6ARJ","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"XBJ7A7","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"TSCPZD","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"3JFDLT","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"UK17YW","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"DO7PKQ","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"WMLS9F","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"DX5G90","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"FPTQSA","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"ODSQO0","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"1U7VIC","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"VZJUJ8","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"T4N5NN","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"TF90GE","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"4Q9HQQ","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"D6G6ZZ","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"P6AI05","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"WYKDOX","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"3UA3D0","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"CY7D24","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"SG1UDK","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"0XKFUS","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"ENU0U0","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"VOREAO","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"0GXQUJ","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"I2UDWZ","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"9B2ZTR","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"FDROFF","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"16B1UN","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"MV0N1C","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"BD1DFA","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"XA58J5","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"KOJA03","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"26DNT8","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"K01O9L","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"O5977X","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"26GWB6","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"BM6NYP","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"4J1BQN","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"A3UTDB","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"H5X1W6","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"L3KA3E","status":"0","userId":"2009462"},{"amount":"0.000","inviteCode":"WUGX4F","status":"0","userId":"2009462"}]
     * inviteCount : 54
     * teamCount : 0
     * inviteCodePrice : 10
     * sumAmount : 0
     */

    private String inviteCount;
    private String teamCount;
    private String inviteCodePrice;
    private String sumAmount;
    private List<InviteListBean> inviteList;

    public String getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(String inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(String teamCount) {
        this.teamCount = teamCount;
    }

    public String getInviteCodePrice() {
        return inviteCodePrice;
    }

    public void setInviteCodePrice(String inviteCodePrice) {
        this.inviteCodePrice = inviteCodePrice;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public List<InviteListBean> getInviteList() {
        return inviteList;
    }

    public void setInviteList(List<InviteListBean> inviteList) {
        this.inviteList = inviteList;
    }

    public static class InviteListBean {
        /**
         * amount : 80.000
         * inviteCode : 122345
         * inviteUserId : 2
         * status : 0
         * userId : 2009462
         */

        private String amount;
        private String inviteCode;
        private String inviteUserId;
        private String status;
        private String userId;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getInviteUserId() {
            return inviteUserId;
        }

        public void setInviteUserId(String inviteUserId) {
            this.inviteUserId = inviteUserId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
