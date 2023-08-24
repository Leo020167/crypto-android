package com.bitcnew.config;

public class Configs {

    public static final String[] flavors = {
            "tradingview", "leadercoin", "aicoin", "fwdetsc", "fwdetsc1", "fwdetsc2", "fwdetsc3", "tyresd"
    };

    public static boolean inFlavors(String flavor) {
        for (String item : flavors) {
            if (null != flavor && flavor.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

}
