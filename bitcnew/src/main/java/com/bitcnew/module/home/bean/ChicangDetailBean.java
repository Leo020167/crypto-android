package com.bitcnew.module.home.bean;

import java.io.Serializable;

public class ChicangDetailBean implements Serializable {

    /**
     * data : {"symbol":"LTC","amount":"10","last":"56.03","availableAmount":"10","rate":"-1.02","price":"54.62000000","priceDecimals":"2","profitRate":"2.58","profit":"14.100"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * symbol : LTC
         * amount : 10
         * last : 56.03
         * availableAmount : 10
         * rate : -1.02
         * price : 54.62000000
         * priceDecimals : 2
         * profitRate : 2.58
         * profit : 14.100
         */

        private String symbol;
        private String amount;
        private String last;
        private String availableAmount;
        private String rate;
        private String price;
        private String priceDecimals;
        private String profitRate;
        private String profit;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getAvailableAmount() {
            return availableAmount;
        }

        public void setAvailableAmount(String availableAmount) {
            this.availableAmount = availableAmount;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceDecimals() {
            return priceDecimals;
        }

        public void setPriceDecimals(String priceDecimals) {
            this.priceDecimals = priceDecimals;
        }

        public String getProfitRate() {
            return profitRate;
        }

        public void setProfitRate(String profitRate) {
            this.profitRate = profitRate;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }
    }
}
