package com.bitcnew.config;

public class Configs {

    public static final String[] flavors = {
            "tradingview", "leadercoin", "aicoin", "fwdetsc", "fwdetsc1", "fwdetsc2", "fwdetsc3", "fireglobal", "gliedt", "ecloud", "stdelit"
    };

    public static boolean inFlavors(String flavor) {
        for (String item : flavors) {
            if (null != flavor && (flavor.equalsIgnoreCase(item) || flavor.startsWith(item))) {
                return true;
            }
        }
        return false;
    }

    // 隐藏在线客服
    public static final String[] hideZaiXianKeFuFlavors = {"fwdetsc", "gliedt", "stdelit"};

    public static boolean inHideZaiXianKeFuGliedt(String flavor) {
        for (String item : hideZaiXianKeFuFlavors) {
            if (null != flavor && (flavor.equalsIgnoreCase(item) || flavor.startsWith(item))) {
                return true;
            }
        }
        return false;
    }

    public static final String[] gliedtFlavors = {"fwdetsc", "gliedt", "stdelit"};

    public static boolean inGliedt(String flavor) {
        for (String item : gliedtFlavors) {
            if (null != flavor && (flavor.equalsIgnoreCase(item) || flavor.startsWith(item))) {
                return true;
            }
        }
        return false;
    }

    public static final String[] noFeeFlavors = {"fireglobal", "ecloud", "aicoin", "fwdetsc", "gliedt", "stdelit"};

    public static boolean inNoFeeFlavors(String flavor) {
        for (String item : noFeeFlavors) {
            if (null != flavor && (flavor.equalsIgnoreCase(item) || flavor.startsWith(item))) {
                return true;
            }
        }
        return false;
    }

    // 是否展示 Profit 字段
    public static final String[] showProfitFlavors = {"tradingview", "leadercoin", "fireglobal", "ecloud", "aicoin", "fwdetsc", "gliedt", "stdelit"};
    public static boolean inShowProfitFlavors(String flavor) {
        return _inFlavors(showProfitFlavors, flavor);
    }

    private static boolean _inFlavors(String[] flavors, String flavor) {
        if (null == flavors || flavors.length == 0 || null == flavor) {
            return false;
        }
        for (String item : flavors) {
            if (flavor.equalsIgnoreCase(item) || flavor.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

}
