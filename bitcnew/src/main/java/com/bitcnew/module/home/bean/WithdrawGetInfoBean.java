package com.bitcnew.module.home.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WithdrawGetInfoBean implements Serializable {

    @SerializedName("balance")
    private double balance;
    @SerializedName("infos")
    private List<Infos> infos;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Infos> getInfos() {
        return infos;
    }

    public void setInfos(List<Infos> infos) {
        this.infos = infos;
    }

    public static class Infos implements Serializable {
        @SerializedName("address")
        private String address;
        @SerializedName("fee")
        private double fee;
        @SerializedName("image")
        private String image;
        @SerializedName("type")
        private String type;
        private boolean isSel;

        public boolean isSel() {
            return isSel;
        }

        public void setSel(boolean sel) {
            isSel = sel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
