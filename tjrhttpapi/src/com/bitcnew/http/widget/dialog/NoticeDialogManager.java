package com.bitcnew.http.widget.dialog;

import android.app.Activity;
import android.text.Html;

import com.bitcnew.http.util.ShareData;
import com.bitcnew.http.model.Components;
import com.bitcnew.http.model.Placard;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;

public class NoticeDialogManager {
    private TjrBaseDialog noticeDialog;
    private Placard placard;
    //	private SharedPreferences sharedata;// 获取用户信息
    protected Activity mActivity; // 页面的内容

    protected Components components;// 组件信息

    public NoticeDialogManager(Activity activity, Components components, Placard placard) {
        this.mActivity = activity;
        this.components = components;
        this.placard = placard;
    }

    public void setUpdateManager(Activity activity, Components components, Placard placard) {
        this.mActivity = activity;
        this.components = components;
        this.placard = placard;
    }

    /**
     * 是否显示对话框
     */
    public void checkNoticeDialog() {
        if (canshowNoticeDialog()) {
            if (placard == null) return;
            noticeDialog = new TjrBaseDialog(mActivity) {

                @Override
                public void setDownProgress(int progress) {
                    // 不需要
                }

                @Override
                // 确定 不用操作
                public void onclickOk() {

                }

                @Override
                public void onclickClose() {
                    savePlardToxml();
                }
            };
            noticeDialog.setTvTitle(placard.getTitle());
            noticeDialog.setMessage(Html.fromHtml(placard.getContent() == null ? "" : placard.getContent()));
            noticeDialog.setBtnColseText("不再提醒");
            noticeDialog.setBtnOkText(" 确  定 ");
            if (!mActivity.isFinishing() && !noticeDialog.isShowing()) noticeDialog.show();

        }
    }

    public void savePlardToxml() {
//		if (sharedata == null) sharedata = ShareData.getUserSharedPreferences(mActivity);
        if (placard == null) return;
        ShareData.savePlacardTime(mActivity, placard.getPlacardTime());
    }

    public boolean canshowNoticeDialog() {
//		if (mActivity == null) sharedata = ShareData.getUserSharedPreferences(mActivity);
//        if (mActivity == null) return false;
        String time = ShareData.getPlacardTime(mActivity);
        if (placard != null && !time.equals(placard.getPlacardTime())) {
            return true;
        }
        return false;
    }

}
