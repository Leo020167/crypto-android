package com.bitcnew.common.text;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * @author yanjun
 * @since 0.0.1
 */
public class MoneyTextWatcher implements TextWatcher {

    private final int maxDecimalLength;

    private String symbol = "";

    public MoneyTextWatcher() {
        this(2);
    }

    public MoneyTextWatcher(int maxDecimalLength) {
        this(maxDecimalLength, null);
    }

    public MoneyTextWatcher(int maxDecimalLength, String symbol) {
        if (null != symbol) {
            this.symbol = symbol;
        }
        this.maxDecimalLength = maxDecimalLength;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) { // 默认填入0
            return;
        }

        if (!s.subSequence(0, symbol.length()).toString().equals(symbol)) { // 插入符号
            s.insert(0, symbol);
        }
        if (s.toString().equals(symbol)) {
            return;
        }

        if (s.charAt(symbol.length()) == '.') { // 如果输入小数点，则在第一位填入0
            s.insert(symbol.length(), "0");
        }

        if (s.charAt(s.length() - 1) == '.') { // 最后一位是小数点
            boolean hasDotBefore = false; // 判断是否已经输入过小数点
            for (int i = 0; i < s.length() - 1; i++) {
                if (s.charAt(i) == '.') {
                    hasDotBefore = true;
                    break;
                }
            }
            if (hasDotBefore) { // 输入过小数点，删除最后一位输入
                s.delete(s.length() - 1, s.length());
            }
            return;
        }

        if (s.charAt(symbol.length()) == '0' && s.length() > symbol.length() + 1) {
            if (s.charAt(symbol.length() + 1) == '0') { // 删除多余的 0
                s.delete(symbol.length() + 1, symbol.length() + 2);
            } else if (s.charAt(symbol.length() + 1) != '.') {
                s.delete(symbol.length(), symbol.length() + 1);
            }
        }

        if (s.toString().contains(".")) { // 删除错误的格式
            String[] sr = s.toString().split("[.]");
            if (sr.length >= 2 && sr[1].length() > maxDecimalLength) {
                s.delete(s.length() - 1, s.length());
            }
        }
    }
}
