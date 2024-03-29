package com.bitcnew.module.chat.util;

import com.bitcnew.http.TaojinluHttp;
import com.bitcnew.http.resource.BaseRemoteResourceManager;
import com.bitcnew.task.BaseAsyncTask;

/**
 * 获取私聊记录
 */
public class DownLoadVoiceTask extends BaseAsyncTask<Long, Void, String> {

    private String url;
    //	private MainApplication mainApplication;
    private BaseRemoteResourceManager mRTalk;

//	public DownLoadVoiceTask(MainApplication mainApplication, String url,RemoteResourceManager mRTalk) {
//		this.mRTalk = mRTalk;
//		this.url = url;
//		this.mainApplication = mainApplication;
//	}

    public DownLoadVoiceTask(String url, BaseRemoteResourceManager mRTalk) {
        this.mRTalk = mRTalk;
        this.url = url;
    }

    @Override
    protected String doInBackground(Long... params) {
        try {
            String result = TaojinluHttp.getInstance().downloadVoice(mRTalk.getmDiskCache(), url);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}