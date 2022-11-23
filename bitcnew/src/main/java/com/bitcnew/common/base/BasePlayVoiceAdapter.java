package com.bitcnew.common.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.Gravity;

import com.bitcnew.R;
import com.bitcnew.module.circle.ui.PlayVoiceUtilView;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.base.baseadapter.BaseImageLoaderAdapter;
import com.bitcnew.social.util.CommonUtil;

import java.io.File;

public abstract class BasePlayVoiceAdapter<T extends TaojinluType> extends BaseImageLoaderAdapter<T> implements PlayVoiceUtilView.VoiceLoadAndPlay {

    /**
     * 如果 playingPos =1 playingFollowPos=-1 播放的是跟帖的声音 playingPos =1
     * playingFollowPos=1 播放的是楼中楼的声音 playingPos =-1 playingFollowPos=-1 没有播放
     */
    protected int playingPos = -1;// 正在播放的item 索引
    protected int playingFollowPos = -1;// 正在播放楼中楼的item 索引


    private MediaPlayer mp;
    private Context mContext;
    private MediaPlaycastReceiver mediaPlaycastReceiver;
    private static final String TRYLISTENACTION = "com.bitcnew.mediaPlay.tryListen";//
    private static final String WHENRECORDSTARTACTION = "com.bitcnew.recordStart.stop";
    private static final String WHENFLOORPLAY = "com.bitcnew.mediaPlay.floorPlay";//微访谈 播放楼层

    private static final String WHENMEDIASTOPACTION = "com.bitcnew.mediaPlay.stop";

    private String willPlayUrl = "";

    public BasePlayVoiceAdapter(Context context) {
        this(context, com.bitcnew.http.R.drawable.ic_default_head);
    }

    public BasePlayVoiceAdapter(Context context, int defaultRes) {
        super(defaultRes);
        this.mContext = context;
        mediaPlaycastReceiver = new MediaPlaycastReceiver();
        mediaPlaycastReceiver.register();
        if (mp == null) {
            mp = new MediaPlayer();
            // mp.setScreenOnWhilePlaying(true);
            mp.setOnErrorListener(new OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    CommonUtil.showToast(mContext, mContext.getResources().getString(R.string.bofangyichang), Gravity.CENTER);
                    mp.reset();
                    return false;
                }
            });
            mp.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    Log.d("setOnComplete", "playingPos==" + playingPos);
                    int tryPlayPos = playingPos + 1;
                    playingPos = -1;
                    playingFollowPos = -1;
                    notifyDataSetChanged();

                    playNext(tryPlayPos);

                    // playVoiceUtilView.setState(0);
                }
            });
            mp.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    notifyDataSetChanged();
                }
            });
            mp.setVolume(1.0f, 1.0f);
            ((Activity) mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);

        }

    }

    public void playNext(int tryPlayPos) {
    }

    @Override
    public void stop() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.reset();
            this.playingPos = -1;
            this.playingFollowPos = -1;
            notifyDataSetChanged();
        }
    }

    public void releaseMp() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        try {
            if (mContext != null && mediaPlaycastReceiver != null)
                mContext.unregisterReceiver(mediaPlaycastReceiver);
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
        }

    }

    @Override
    public void play(File file, int pos, int followPos) {
        Intent intent = new Intent();
        intent.setAction(WHENMEDIASTOPACTION);
        mContext.sendBroadcast(intent);// 如果录音后正在试听，先停止
        if (file.exists()) {
            try {
                // Log.d("filePath", "filePath==" + file.getAbsolutePath());
                // Log.d("playing", "pos=="+pos+"   followPos=="+followPos);
                mp.setDataSource(file.getAbsolutePath());
                mp.prepareAsync();
                willPlayUrl = "";
                this.playingPos = pos;
                this.playingFollowPos = followPos;
            } catch (Exception e) {
                this.playingPos = -1;
                this.playingFollowPos = -1;
                notifyDataSetChanged();
                // Log.d("Exception", "e==" + e);
                CommonUtil.showToast(mContext, mContext.getResources().getString(R.string.bofangyichang), Gravity.CENTER);
            }
        }
    }

    @Override
    public void playAfterLoad(File file, int pos, int followPos) {
        if (!file.getName().equals(willPlayUrl)) return;
        play(file, pos, followPos);
    }

    @Override
    public void setWillPlay(String url) {
        this.willPlayUrl = url;
    }

    private class MediaPlaycastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TRYLISTENACTION.equals(intent.getAction())) {
                // String filePath=intent.getStringExtra("filePath");
                // play(filePath, -1, -1);
                stop();
            } else if (WHENRECORDSTARTACTION.equals(intent.getAction())) {
                stop();
            } else if (WHENFLOORPLAY.equals(intent.getAction())) {
                stop();
            }
        }

        public void register() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(TRYLISTENACTION);
            intentFilter.addAction(WHENRECORDSTARTACTION);
            intentFilter.addAction(WHENFLOORPLAY);
            mContext.registerReceiver(this, intentFilter);
        }
    }

}
