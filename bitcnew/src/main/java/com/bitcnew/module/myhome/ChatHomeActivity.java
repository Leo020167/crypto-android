package com.bitcnew.module.myhome;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.bitcnew.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.bitcnew.data.db.TaoJinLuDatabase;
import com.bitcnew.module.chat.entity.ChatHomeEntity;
import com.bitcnew.module.circle.entity.CircleChatEntity;
import com.bitcnew.module.myhome.adapter.ChatHomeAdapter;
import com.bitcnew.subpush.ReceiveModel;
import com.bitcnew.subpush.ReceiveModelTypeEnum;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.task.BaseAsyncTask;
import com.bitcnew.R;

public class ChatHomeActivity extends TJRBaseActionBarSwipeBackObserverActivity {
//    private int backType;// 1代表是从通知点击进来的

    private TaoJinLuDatabase mDb;
    protected RecyclerView listView;
    private ChatHomeAdapter chatHomeAdapter;
    private ClearChatInfoTask clearChatInfoTask;

    public long myUserId;

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.wodesixin);
    }

    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //收到一条新信息
                if (model.obj instanceof Group) {
                    Group<CircleChatEntity> group = (Group<CircleChatEntity>) model.obj;
                    System.out.println("group size : " + group.size());
                    if (group.size() == 1) {
                        CircleChatEntity newEntity = group.get(0);
                        boolean newChat = true;   //是否为对方创建的新对话
                        for (int i = 0; i < chatHomeAdapter.getItemCount(); i++) {
                            ChatHomeEntity oldEntity = chatHomeAdapter.getItem(i);
                            if (oldEntity != null) {
                                if (oldEntity.chatTopic.equals(newEntity.chatTopic)) {
                                    if (newEntity.userId != myUserId) {
                                        oldEntity.name = newEntity.name;
                                        oldEntity.headurl = newEntity.headurl;
                                    }
                                    oldEntity.latestMsg = newEntity.say;
                                    oldEntity.chatCreateTime = Long.parseLong(newEntity.createTime);
                                    oldEntity.notify = newEntity.isPush;
                                    newChat = false;
                                }
                            }
                        }
                        if (newChat) {
                            chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
//                            if (!TextUtils.isEmpty(newEntity.circleNum)) {
//                                long taUserId = 0;
//                                String[] strings = newEntity.circleNum.split("-");
//                                if (strings[0].equals(String.valueOf(myUserId))) {
//                                    taUserId = Long.parseLong(strings[1]);
//                                } else if (strings[1].equals(String.valueOf(myUserId))) {
//                                    taUserId = Long.parseLong(strings[0]);
//                                }
//                                if (taUserId == 0) {
//                                    CommonUtil.showToast(ChatHomeActivity.this, "参数错误", Gravity.CENTER);
//                                    break;
//                                } else {
////                                    new GetChatTopicTask(myUserId, taUserId).executeParams();   //获取对话的完整信息
//                                }
//                            }
                        } else {
                            chatHomeAdapter.notifyDataSetChangedWithComparator();

                        }
                    } else if (group.size() > 1) {
                        chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = TaoJinLuDatabase.getInstance(this.getApplicationContext());
        myUserId = Long.parseLong(getUserId());
        listView = (RecyclerView) findViewById(R.id.rv_list);
        chatHomeAdapter = new ChatHomeAdapter(this, Long.parseLong(getUserId()));
        listView.setAdapter(chatHomeAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(new SimpleRecycleDivider(this));
        listView.setBackgroundColor(ContextCompat.getColor(this,R.color.pageBackground));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
    }


    public void startClearChatInfoTask(long myUserId, String chatTopic) {
        CommonUtil.cancelAsyncTask(clearChatInfoTask);
        clearChatInfoTask = (ClearChatInfoTask) new ClearChatInfoTask(myUserId, chatTopic).executeParams();
    }

    class ClearChatInfoTask extends BaseAsyncTask<Void, Void, Boolean> {

        long myUserId;
        String chatTopic;


        public ClearChatInfoTask(long myUserId, String chatTopic) {
            this.myUserId = myUserId;
            this.chatTopic = chatTopic;
        }


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override

        protected Boolean doInBackground(Void... params) {
            int clearInfoNums = mDb.deleteChatHomeRecord(String.valueOf(myUserId), chatTopic);//清空与莫个人的私聊房间
            int clearChatNums = mDb.clearPrivateChat(myUserId, chatTopic);//清空与莫个人的私聊所有聊天记录
            PrivateChatSharedPreferences.clearPriChatRecordNum(ChatHomeActivity.this, chatTopic, myUserId);//清除消息数量
            if (clearInfoNums >= 0 && clearChatNums >= 0) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dismissProgressDialog();
            if (aBoolean) {
                CommonUtil.showToast(ChatHomeActivity.this, getResources().getString(R.string.shanchuchenggong), Gravity.CENTER);
            } else {
                CommonUtil.showToast(ChatHomeActivity.this, getResources().getString(R.string.shanchushibai), Gravity.CENTER);
            }
            chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
        }
    }

}
