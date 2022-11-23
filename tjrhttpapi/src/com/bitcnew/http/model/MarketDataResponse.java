package com.bitcnew.http.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketDataResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public class Data {
        @SerializedName("tab")
        @Expose
        private String tab;
        @SerializedName("quotes")
        @Expose
        private List<Quote> quotes = null;

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public List<Quote> getQuotes() {
            return quotes;
        }

        public void setQuotes(List<Quote> quotes) {
            this.quotes = quotes;
        }
        public class Quote {
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("amountSort")
            @Expose
            private String amountSort;
            @SerializedName("balance")
            @Expose
            private String balance;
            @SerializedName("balanceSort")
            @Expose
            private String balanceSort;
            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("high")
            @Expose
            private String high;
            @SerializedName("low")
            @Expose
            private String low;
            @SerializedName("marketType")
            @Expose
            private String marketType;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("openMarketStr")
            @Expose
            private String openMarketStr;
            @SerializedName("price")
            @Expose
            private String price;
            @SerializedName("rate")
            @Expose
            private String rate;
            @SerializedName("sortNum")
            @Expose
            private Integer sortNum;
            @SerializedName("symbol")
            @Expose
            private String symbol;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getAmountSort() {
                return amountSort;
            }

            public void setAmountSort(String amountSort) {
                this.amountSort = amountSort;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getBalanceSort() {
                return balanceSort;
            }

            public void setBalanceSort(String balanceSort) {
                this.balanceSort = balanceSort;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getMarketType() {
                return marketType;
            }

            public void setMarketType(String marketType) {
                this.marketType = marketType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOpenMarketStr() {
                return openMarketStr;
            }

            public void setOpenMarketStr(String openMarketStr) {
                this.openMarketStr = openMarketStr;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public Integer getSortNum() {
                return sortNum;
            }

            public void setSortNum(Integer sortNum) {
                this.sortNum = sortNum;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }
        }
    }
}
