package com.bitcnew.module.home.bean;

import java.io.Serializable;
import java.util.List;

public class HomeBannerBean implements Serializable {

    private DigitalAccountBean digitalAccount;
    private String tolAssetsCny;
    private DigitalAccountBean balanceAccount;
    private String tolAssets;
    private List<BannerBean> banner;
    private List<BannerBean> transformers;

    public DigitalAccountBean getDigitalAccount() {
        return digitalAccount;
    }

    public void setDigitalAccount(DigitalAccountBean digitalAccount) {
        this.digitalAccount = digitalAccount;
    }

    public String getTolAssetsCny() {
        return tolAssetsCny;
    }

    public void setTolAssetsCny(String tolAssetsCny) {
        this.tolAssetsCny = tolAssetsCny;
    }

    public DigitalAccountBean getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(DigitalAccountBean balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public String getTolAssets() {
        return tolAssets;
    }

    public void setTolAssets(String tolAssets) {
        this.tolAssets = tolAssets;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<BannerBean> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<BannerBean> transformers) {
        this.transformers = transformers;
    }

    public static class DigitalAccountBean implements Serializable {
        private String accountType;
        private String assets;
        private String assetsCny;
        private String eableBail;
        private String frozenAmount;
        private String frozenBail;
        private String holdAmount;
        private String openBail;
        private String profit;
        private String riskRate;
        private String userId;
        private List<?> entrustList;
        private List<OpenListBean> openList;

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAssets() {
            return assets;
        }

        public void setAssets(String assets) {
            this.assets = assets;
        }

        public String getAssetsCny() {
            return assetsCny;
        }

        public void setAssetsCny(String assetsCny) {
            this.assetsCny = assetsCny;
        }

        public String getEableBail() {
            return eableBail;
        }

        public void setEableBail(String eableBail) {
            this.eableBail = eableBail;
        }

        public String getFrozenAmount() {
            return frozenAmount;
        }

        public void setFrozenAmount(String frozenAmount) {
            this.frozenAmount = frozenAmount;
        }

        public String getFrozenBail() {
            return frozenBail;
        }

        public void setFrozenBail(String frozenBail) {
            this.frozenBail = frozenBail;
        }

        public String getHoldAmount() {
            return holdAmount;
        }

        public void setHoldAmount(String holdAmount) {
            this.holdAmount = holdAmount;
        }

        public String getOpenBail() {
            return openBail;
        }

        public void setOpenBail(String openBail) {
            this.openBail = openBail;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getRiskRate() {
            return riskRate;
        }

        public void setRiskRate(String riskRate) {
            this.riskRate = riskRate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<?> getEntrustList() {
            return entrustList;
        }

        public void setEntrustList(List<?> entrustList) {
            this.entrustList = entrustList;
        }

        public List<OpenListBean> getOpenList() {
            return openList;
        }

        public void setOpenList(List<OpenListBean> openList) {
            this.openList = openList;
        }

        public static class OpenListBean implements Serializable {
            private String buySell;
            private String buySellValue;
            private String closePrice;
            private String dvLossRate;
            private String dvProfitRate;
            private String dvUid;
            private String followUid;
            private String openBail;
            private String openFee;
            private String openHand;
            private String openPrice;
            private String openTime;
            private String orderId;
            private String price;
            private String profit;
            private String profitRate;
            private String stopLossPrice;
            private String stopWinPrice;
            private String symbol;
            private String userId;

            public String getBuySell() {
                return buySell;
            }

            public void setBuySell(String buySell) {
                this.buySell = buySell;
            }

            public String getBuySellValue() {
                return buySellValue;
            }

            public void setBuySellValue(String buySellValue) {
                this.buySellValue = buySellValue;
            }

            public String getClosePrice() {
                return closePrice;
            }

            public void setClosePrice(String closePrice) {
                this.closePrice = closePrice;
            }

            public String getDvLossRate() {
                return dvLossRate;
            }

            public void setDvLossRate(String dvLossRate) {
                this.dvLossRate = dvLossRate;
            }

            public String getDvProfitRate() {
                return dvProfitRate;
            }

            public void setDvProfitRate(String dvProfitRate) {
                this.dvProfitRate = dvProfitRate;
            }

            public String getDvUid() {
                return dvUid;
            }

            public void setDvUid(String dvUid) {
                this.dvUid = dvUid;
            }

            public String getFollowUid() {
                return followUid;
            }

            public void setFollowUid(String followUid) {
                this.followUid = followUid;
            }

            public String getOpenBail() {
                return openBail;
            }

            public void setOpenBail(String openBail) {
                this.openBail = openBail;
            }

            public String getOpenFee() {
                return openFee;
            }

            public void setOpenFee(String openFee) {
                this.openFee = openFee;
            }

            public String getOpenHand() {
                return openHand;
            }

            public void setOpenHand(String openHand) {
                this.openHand = openHand;
            }

            public String getOpenPrice() {
                return openPrice;
            }

            public void setOpenPrice(String openPrice) {
                this.openPrice = openPrice;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getProfitRate() {
                return profitRate;
            }

            public void setProfitRate(String profitRate) {
                this.profitRate = profitRate;
            }

            public String getStopLossPrice() {
                return stopLossPrice;
            }

            public void setStopLossPrice(String stopLossPrice) {
                this.stopLossPrice = stopLossPrice;
            }

            public String getStopWinPrice() {
                return stopWinPrice;
            }

            public void setStopWinPrice(String stopWinPrice) {
                this.stopWinPrice = stopWinPrice;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }

    public static class BannerBean implements Serializable {
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
