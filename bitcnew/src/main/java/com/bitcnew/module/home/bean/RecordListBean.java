package com.bitcnew.module.home.bean;

import com.bitcnew.http.base.TaojinluType;

import java.io.Serializable;
import java.util.List;

public class RecordListBean implements Serializable, TaojinluType {


    /**
     * buttons : 5
     * data : [{"accountType":"balance","amount":"1000.000","balance":"11600.574","createTime":"1657455421","id":"7","inOut":"1","remark":"充值","userId":"2005007"}]
     * pageNo : 1
     * pageSize : 15
     * total : 0
     */

    private String buttons;
    private String pageNo;
    private String pageSize;
    private String total;
    private List<DataBean> data;

    public String getButtons() {
        return buttons;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements TaojinluType {
        /**
         * accountType : balance
         * amount : 1000.000
         * balance : 11600.574
         * createTime : 1657455421
         * id : 7
         * inOut : 1
         * remark : 充值
         * userId : 2005007
         */

        private String accountType;
        private String amount;
        private String balance;
        private String createTime;
        private String id;
        private String inOut;
        private String orderId;
        private String remark;
        private String updateTime;
        private String userId;
        private String agentId;

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInOut() {
            return inOut;
        }

        public void setInOut(String inOut) {
            this.inOut = inOut;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}

