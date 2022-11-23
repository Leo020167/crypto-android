package com.bitcnew.module.legal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.module.chat.ChatRoomActivity;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.trade.entity.ChatStaff;
import com.bitcnew.module.legal.dialog.OtcConfirmReceivedPayDialog;
import com.bitcnew.module.legal.dialog.OtcOrderCancelDialog;
import com.bitcnew.module.legal.dialog.ResetReceiptTermDialogFragment;
import com.bitcnew.module.legal.entity.OtcOrderInfo;
import com.bitcnew.module.legal.entity.OtcOrderStateEnum;
import com.bitcnew.module.myhome.entity.Receipt;
import com.bitcnew.subpush.ReceiveModel;
import com.bitcnew.subpush.ReceiveModelTypeEnum;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.InflaterUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.VeDate;
import com.bitcnew.widgets.BadgeView;
import com.bitcnew.widgets.CircleImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 订单详情（包括各种状态）
 * <p>
 * <p>
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class LegalOrderInfoActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {


    @BindView(R.id.llChat)
    LinearLayout llChat;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTimeRight)
    TextView tvTimeRight;
    @BindView(R.id.icState)
    AppCompatImageView icState;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvTolCny)
    TextView tvTolCny;
    @BindView(R.id.llCopyTolBalance)
    LinearLayout llCopyTolBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.llCopyOrderNo)
    LinearLayout llCopyOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvUserRealName)
    TextView tvUserRealName;
    @BindView(R.id.llCopyName)
    LinearLayout llCopyName;
    @BindView(R.id.ivPayLogo)
    ImageView ivPayLogo;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvGoPay)
    TextView tvGoPay;
    @BindView(R.id.llBtn1)
    LinearLayout llBtn1;
    //    @BindView(R.id.tvSubmit)
//    TextView tvSubmit;
    @BindView(R.id.tvAppeal)
    TextView tvAppeal;
    @BindView(R.id.tvReceivedConfirm)
    TextView tvReceivedConfirm;
    @BindView(R.id.tvAppeal2)
    TextView tvAppeal2;
    @BindView(R.id.tvReceivedConfirm2)
    TextView tvReceivedConfirm2;
    @BindView(R.id.llBtn2)
    LinearLayout llBtn2;
    @BindView(R.id.tvBuyOrSellName)
    TextView tvBuyOrSellName;
    @BindView(R.id.tvBuyOrSellRealName)
    TextView tvBuyOrSellRealName;

    @BindView(R.id.llPayWay)
    LinearLayout llPayWay;
    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;
    @BindView(R.id.llSeller)
    LinearLayout llSeller;

    private Call<ResponseBody> orderCancelCall;
    private Call<ResponseBody> getOrderCashDetailCall;
    private Call<ResponseBody> otcToConfirmReceivedPayCall;

    private OtcConfirmReceivedPayDialog otcConfirmReceivedPayDialog;
    private OtcOrderCancelDialog otcOrderCancelDialog;


    private OtcOrderInfo orderCash;
    private long orderCashId;

    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private CountDownTimer timer;

    private ChatStaff chatStaff;//私聊用到


    private BadgeView badgeChat;


    private Group<Receipt> receipts;//收款方式
    private Receipt receipt;//重新修改所选中的
    private ResetReceiptTermDialogFragment selectReceiptTermDialogFragment;


    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //收到一条新信息
                setChatNewsCount();
                break;

            default:
                break;
        }
    }

    public static void pageJump(Context context, long orderCashId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        PageJumpUtil.pageJump(context, LegalOrderInfoActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.legal_order_info;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    @Override
    protected void showReConnection() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unpaid_info);

        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
            finish();
            return;
        }


        llCopyName.setOnClickListener(this);
        llCopyOrderNo.setOnClickListener(this);
        llCopyTolBalance.setOnClickListener(this);
        llChat.setOnClickListener(this);
        llSeller.setOnClickListener(this);

        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        tjrImageLoaderUtil = new TjrImageLoaderUtil();

    }

    private void hiddenBtn() {//回到初始状态
        llBtn1.setVisibility(View.GONE);
        tvAppeal.setVisibility(View.GONE);
        tvReceivedConfirm.setVisibility(View.GONE);
        llBtn2.setVisibility(View.GONE);
        ivPayWay.setVisibility(View.GONE);
        cancelTimer();
        tvTime.setText("");
    }


    @Override
    protected void onResume() {
        super.onResume();
        startOtcGetOrderDetail();
        Log.d("setChatNewsCount", "onResume==");
        setChatNewsCount();

    }

    public void showPrivateChatNewsCount(String chatTopic) {
        int chatCount = 0;
        if (getApplicationContext().getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(getApplicationContext(), chatTopic, getApplicationContext().getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }
    }
//    wait(0), //买家：取消、去付款、我已付款成功，卖家：我确认已收到付款（此态暂不可用）
//    mark(1), //买家：从“我已付款成功”点击后->“申诉”，卖家：申诉、我确认已收到付款
//    done(2), //买家：完成，卖家：从“我确认已收到付款”点击后->完成
//    appeal(3), //申诉，mark->appeal
//    expire(-1), //已过期，订单过期不处理
//    cancel(-2), //已撤销，
//    admin_cancel(-3);// 系统撤销

    private void setData() {
        setChatNewsCount();
        if (orderCash != null) {
            tvState.setText(orderCash.stateValue);
            tvTips.setText(orderCash.stateTip);
            tvDesc.setText(orderCash.buySellValue);
            tvTolCny.setText(orderCash.currencySign + orderCash.tolPrice);
            tvPrice.setText(orderCash.currencySign + orderCash.price);
            tvNum.setText(orderCash.amount + " USDT");
            tvOrderNo.setText(String.valueOf(orderCash.orderId));
            tvOrderTime.setText(DateUtils.getStringDateOfString2(orderCash.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            tvUserName.setText(orderCash.showUserName);
            tvUserRealName.setText(orderCash.showRealName);
            tjrImageLoaderUtil.displayImage(orderCash.showUserLogo, ivHead);

            receipts = new Gson().fromJson(orderCash.showPayWay, new TypeToken<Group<Receipt>>() {
            }.getType());

            if (receipts != null && receipts.size() > 0) {
                if (isBuyer()) {//我是买家
                    if (receipt == null) {//避免买家每次回来刷新都改变,
                        receipt = receipts.get(0);
                    }
                }else{
                    receipt = receipts.get(0);//可能买家更改了付款方式
                }
                tjrImageLoaderUtil.displayImage(receipt.receiptLogo, ivPayLogo);
                tvPayWay.setText(receipt.receiptTypeValue);
            }

            if (isBuyer()) {//我是买家
                tvBuyOrSellName.setText(getResources().getString(R.string.maijianicheng2));
                tvBuyOrSellRealName.setText(getResources().getString(R.string.maijiaxingming2));
            } else {
                tvBuyOrSellName.setText(getResources().getString(R.string.maijianicheng));
                tvBuyOrSellRealName.setText(getResources().getString(R.string.canshucuowu));
            }
            llPayWay.setOnClickListener(null);
            if (orderCash.state == OtcOrderStateEnum.wait.state) {
                if (isBuyer()) {//我是买家 请付款
                    llBtn1.setVisibility(View.VISIBLE);
                    icState.setImageResource(R.drawable.ic_svg_time);
                    tvTimeRight.setVisibility(View.GONE);
                    tvCancel.setOnClickListener(this);
                    tvGoPay.setOnClickListener(this);
                    startCountDownTimeLeft();
                    if (receipts != null && receipts.size() > 1) {
                        ivPayWay.setVisibility(View.VISIBLE);
                        llPayWay.setOnClickListener(this);
                    }

                } else {//我是卖家 买家正在付款
                    tvReceivedConfirm.setVisibility(View.VISIBLE);
                    tvReceivedConfirm.setOnClickListener(this);
                    startCountDownTimeRight();
                }
            } else if (orderCash.state == OtcOrderStateEnum.mark.state) {
                if (isBuyer()) {//我是买家 等待卖家放行
                    tvAppeal.setVisibility(View.VISIBLE);
                    tvAppeal.setOnClickListener(this);
                    startCountDownTimeRight();
                } else {//我是卖家 请确认收款
                    icState.setImageResource(R.drawable.ic_svg_time);
                    tvTimeRight.setVisibility(View.GONE);
                    llBtn2.setVisibility(View.VISIBLE);
                    tvAppeal2.setOnClickListener(this);
                    tvReceivedConfirm2.setOnClickListener(this);
                }

            } else if (orderCash.state == OtcOrderStateEnum.done.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_success);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.appeal.state) {
                icState.setImageResource(R.drawable.ic_svg_time);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.expire.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_false);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.cancel.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_false);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.admin_cancel.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_false);
                tvTimeRight.setVisibility(View.GONE);
            }
        }

    }


    private void showLegalQuickSellDialogFragment(Group<Receipt> receipts) {
        selectReceiptTermDialogFragment = ResetReceiptTermDialogFragment.newInstance(receipts, new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                if (selectReceiptTermDialogFragment != null) {
                    selectReceiptTermDialogFragment.dismiss();
                }
                receipt = (Receipt) t;
                tjrImageLoaderUtil.displayImage(receipt.receiptLogo, ivPayLogo);
                tvPayWay.setText(receipt.receiptTypeValue);

            }
        });
        selectReceiptTermDialogFragment.showDialog(getSupportFragmentManager(), "");
    }

    private boolean isBuyer() {
        return orderCash != null && getUserId().equals(orderCash.buyUserId);
    }

    private boolean isSeller() {
        return orderCash != null && getUserId().equals(orderCash.sellUserId);
    }


    private void setChatNewsCount() {
        Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff==" + chatStaff);

        if (chatStaff != null) {
            Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff.chatTopic==" + chatStaff.chatTopic);
            showPrivateChatNewsCount(chatStaff.chatTopic);
        }
    }


    private void showOtcConfirmReceivedPayDialog() {
        otcConfirmReceivedPayDialog = new OtcConfirmReceivedPayDialog(this) {
            @Override
            public void onclickOk() {
                if (orderCash != null) {
                    startOtcToConfirmReceivedPay();
                }
            }
        };
        otcConfirmReceivedPayDialog.show();
    }

    private void startOtcToConfirmReceivedPay() {
        CommonUtil.cancelCall(otcToConfirmReceivedPayCall);
        otcToConfirmReceivedPayCall = VHttpServiceManager.getInstance().getVService().otcToConfirmReceivedPay(orderCashId);
        otcToConfirmReceivedPayCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            }
        });
    }


    private void startCountDownTimeLeft() {
        if (orderCash == null) return;
//        Long ss = VeDate.strLongToDate(String.valueOf(orderCash.expireTime)).getTime() - VeDate.strLongToDate(String.valueOf(orderCash.createTime)).getTime();
        if (orderCash.paySecondTime > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(orderCash.paySecondTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[2] + ":" + time[3]);
                    }
                }

                public void onFinish() {
                    tvTime.setText("00:00");
//                    llBtn1.setVisibility(View.GONE);
                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.dingdanyichaoshi), LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            };
            timer.start();
        } else {
            tvTime.setText("00:00");
//            llBtn1.setVisibility(View.GONE);
        }
    }


    private void startCountDownTimeRight() {
        if (orderCash == null) return;
        icState.setImageResource(R.drawable.ic_svg_otc_time_bg);
        tvTimeRight.setVisibility(View.VISIBLE);
        if (orderCash.paySecondTime > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(orderCash.paySecondTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTimeRight.setText(time[2] + ":" + time[3]);
                    }
                }

                public void onFinish() {
                    tvTimeRight.setText("00:00");
//                    tvReceivedConfirm.setVisibility(View.GONE);
//                    tvAppeal.setVisibility(View.GONE);
                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.dingdanyichaoshi), LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            };
            timer.start();
        } else {
            tvTimeRight.setText("00:00");
//            tvReceivedConfirm.setVisibility(View.GONE);
//            tvAppeal.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            orderCash = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCopyTolBalance:
//                if (orderCash != null)
//                    com.bitcnew.util.CommonUtil.copyText(this, orderCash.balanceCny);
                break;
            case R.id.llCopyOrderNo:
                com.bitcnew.util.CommonUtil.copyText(this, tvOrderNo.getText().toString());
                break;
            case R.id.tvCancel:
                showCancleOrderDialog();
                break;
            case R.id.llChat:
            case R.id.llSeller:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;
            case R.id.llQr:
//                if (orderCash != null && orderCash.receipt != null && !TextUtils.isEmpty(orderCash.receipt.qrCode)) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(CommonConst.PAGETYPE, 6);
//                    bundle.putString(CommonConst.SINGLEPICSTRING, orderCash.receipt.qrCode);
//                    PageJumpUtil.pageJumpToData(LegalOrderInfoActivity.this, ViewPagerPhotoViewActivity.class, bundle);
//                }
                break;
            case R.id.tvGoPay:
                if (orderCash != null && receipt != null) {
                    LegalPayActivity.pageJump(this, orderCash.orderId, orderCash.showUserId, receipt.paymentId);
                }
                break;
            case R.id.tvReceivedConfirm:
                break;
            case R.id.tvReceivedConfirm2:
                showOtcConfirmReceivedPayDialog();
                break;
            case R.id.tvAppeal:
            case R.id.tvAppeal2:
                OtcAppealActivity.pageJump(LegalOrderInfoActivity.this, orderCashId);
                break;
            case R.id.llPayWay:
                if (receipts != null && receipts.size() > 0) {
                    showLegalQuickSellDialogFragment(receipts);
                }
                break;


        }
    }

    private void showCancleOrderDialog() {
        otcOrderCancelDialog = new OtcOrderCancelDialog(this) {
            @Override
            public void onclickOk() {
                if (orderCash != null) {
                    startOrderCancel();
                }
            }
        };
        otcOrderCancelDialog.show();
    }


    private void startOrderCancel() {
        CommonUtil.cancelCall(orderCancelCall);
        orderCancelCall = VHttpServiceManager.getInstance().getVService().otcCancelOrder(orderCashId);
        orderCancelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            }
        });
    }


    private void startOtcGetOrderDetail() {
        hiddenBtn();//先影藏下面按钮
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().otcGetOrderDetail(orderCashId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    chatStaff = resultData.getObject("chatStaff", ChatStaff.class);
                    orderCash = resultData.getObject("order", OtcOrderInfo.class);
                    setData();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean saveToSdcard(String uri) {
        if (TextUtils.isEmpty(uri)) return false;
        // TODO remove 东西
        File file = null;
        try {
//            String uri = getUrl(imgUrls.get(veiePhotoView.getCurrentItem()));
            Log.d("saveToSdcard", "uri==" + uri);
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri);
            if (bitmap == null) {
                com.bitcnew.social.util.CommonUtil.showToast(this, getResources().getString(R.string.meiyouhuoqudaotupian), Gravity.BOTTOM);
                return false;
            }

            String fileName = com.bitcnew.social.util.VeDate.getyyyyMMddHHmmss(com.bitcnew.social.util.VeDate.getNow())
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
            e.printStackTrace();
            Log.d("saveToSdcard", "e==" + e);
            com.bitcnew.social.util.CommonUtil.showToast(this, getResources().getString(R.string.baocuntupianchucuo), Gravity.BOTTOM);
            return false;
        }
        CommonUtil.showmessage(getResources().getString(R.string.baocunerweima), this);
        return true;
    }


    PopupWindow pop;

    private void showPopupMenu(View parent, int width, int height) {
        if (pop == null) {
            pop = new PopupWindow(InflaterUtils.inflateView(this, R.layout.money_mark), width, height);//
//            pop.setOutsideTouchable(false);
//            pop.setFocusable(false);// 如果不加这个，Grid不会响应ItemClick
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));// 特别留意这个东东

//            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                }
//            });
        }
        if (pop != null && !pop.isShowing()) {
//            pop.showAsDropDown(parent);
            pop.showAsDropDown(parent, 0, 20);
//            pop.showAtLocation(parent,Gravity.CENTER,50,50);
        }
    }

    private void dimissPop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }


}
