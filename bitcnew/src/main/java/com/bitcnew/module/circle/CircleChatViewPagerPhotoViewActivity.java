
package com.bitcnew.module.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.photo.PicFragment;
import com.bitcnew.module.circle.entity.CircleChatEntity;
import com.bitcnew.MainApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.bitcnew.http.model.User;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.social.util.CommonUtil;
import com.bitcnew.social.util.VeDate;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.photo.ImageEntity;
import com.bitcnew.module.circle.entity.CircleChatTypeEnum;
import com.bitcnew.widgets.PhotoViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里包含私聊和圈子的聊天,因为
 */
public class CircleChatViewPagerPhotoViewActivity extends TJRBaseToolBarSwipeBackActivity {
    private PhotoViewPager veiePhotoView;
    private int defaultPos;
    private final List<String> imgUrls = new ArrayList<String>();//大图
    private TjrImageLoaderUtil mTjrImageLoaderUtil;
    private PicPagerAdapter mAdapter;
    private User user;

    private String circleNum;//如果是私聊就是 chatTopic
    private boolean isPrivateChat;// 如果是
    private int mark = -1;

    @Override
    protected int setLayoutId() {
        return R.layout.hot_news_image;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getApplicationContext().getUser();
        if (getIntent().getExtras() != null) {
            isPrivateChat = getIntent().getExtras().getBoolean(CommonConst.ISPRIVATECHAT);
            if (!isPrivateChat) {
                circleNum = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            } else {
                circleNum = getIntent().getExtras().getString(CommonConst.CHAT_TOPIC);
            }
            mark = getIntent().getExtras().getInt(CommonConst.CIRCLECHATMARK);

        }
        if (TextUtils.isEmpty(circleNum) || mark == -1) {
            CommonUtil.showToast(this, getResources().getString(R.string.canshucuowu), Gravity.BOTTOM);
            finish();
            return;
        }
        List<CircleChatEntity> listEntity = null;
        if (!isPrivateChat) {
            listEntity = getApplicationContext().getmDb().getCirclePicChat(user.getUserId(), circleNum);
        } else {
            listEntity = getApplicationContext().getmDb().getPrivateChatPicChat(user.getUserId(), circleNum);
        }
        if (listEntity != null && listEntity.size() > 0) {
            CircleChatEntity entity = null;
            for (int i = 0, m = listEntity.size(); i < m; i++) {
                entity = listEntity.get(i);
                imgUrls.add(entity.say);
                if (entity.chatMark == mark) {
                    defaultPos = i;
                }
            }
        }
        if (imgUrls.size() == 0) {
            CommonUtil.showToast(this, getResources().getString(R.string.canshucuowu), Gravity.BOTTOM);
            finish();
            return;
        }
        mActionBar.setTitle((defaultPos + 1) + "/" + imgUrls.size());
//        setContentView(R.layout.hot_news_image);
        veiePhotoView = (PhotoViewPager) findViewById(R.id.photoViewpager);
//        veiePhotoView.setOffscreenPageLimit(imgUrls.size() - 1);
        mTjrImageLoaderUtil = new TjrImageLoaderUtil();
        mAdapter = new PicPagerAdapter(
                getSupportFragmentManager());
        veiePhotoView.setAdapter(mAdapter);
        veiePhotoView.setCurrentItem(defaultPos);
        if (this.getIntent() == null) {
            finish();
            CommonUtil.showToast(this, getResources().getString(R.string.canshucuowu), Gravity.BOTTOM);
            return;
        }
        veiePhotoView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mActionBar.setTitle((arg0 + 1) + "/" + imgUrls.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pic_more, menu);
//        menu.findItem(R.id.action_edit).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveToSdcard();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean saveToSdcard() {
        // TODO remove 东西
        File file = null;
        try {
            String uri = getUrl(imgUrls.get(veiePhotoView.getCurrentItem()));
            Log.d("saveToSdcard", "uri==" + uri);
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri);
            if (bitmap == null) {
                CommonUtil.showToast(this, getResources().getString(R.string.meiyouhuoqudaotupian), Gravity.BOTTOM);
                return false;
            }

            String fileName = VeDate.getyyyyMMddHHmmss(VeDate.getNow())
                    + ".png";
            file = ((MainApplication) getApplicationContext())
                    .getmDCIMRemoteResourceManager().getFile(fileName);
            ((MainApplication) getApplicationContext())
                    .getmDCIMRemoteResourceManager().writeFile(file, bitmap,
                    false);
            if (((MainApplication) getApplicationContext()).isSDCard()) {
                // 其次把文件插入到系统图库
//                try {
//                    MediaStore.Images.Media.insertImage(this.getContentResolver(),
//                            file.getAbsolutePath(), fileName, null);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                // 最后通知图库更新

            }
        } catch (Exception e) {
            CommonUtil.showToast(this, getResources().getString(R.string.baocuntupianchucuo), Gravity.BOTTOM);
            return false;
        }
        CommonUtil.showToast(this, getResources().getString(R.string.baocuntupiandao) + file.getParent(), Gravity.BOTTOM);
        return true;
    }

    class PicPagerAdapter extends FragmentPagerAdapter {

        public PicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Log.d("PicPagerAdapter", "...........");
            return PicFragment
                    .newInstance(new ImageEntity("", getUrl(imgUrls.get(arg0))),
                            mTjrImageLoaderUtil, 5);
        }

        @Override
        public int getCount() {
            return imgUrls.size();
        }

    }

    private String getUrl(String say) {
        say = say.replace(CircleChatTypeEnum.SAY_IMG.type, "");
        return say.split(",")[0];
    }
}
