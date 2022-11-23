package com.bitcnew.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.util.MD5;
import com.bitcnew.common.base.BaseBarActivity;
import com.bitcnew.common.entity.jsonparser.ResultDataParser;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.trade.TransferCoinActivity;
import com.bitcnew.module.home.trade.dialog.RechargeOrTransferDialog;
import com.bitcnew.module.legal.LegalMoneyActivity;
import com.bitcnew.module.myhome.IdentityAuthenActivity;
import com.bitcnew.module.myhome.PaymentTermActivity;
import com.bitcnew.module.myhome.SettingPayPasswordActivity;
import com.bitcnew.util.dialog.DragFragment;
import com.bitcnew.widgets.transactionpassword.FixAuthPasswordDialog2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 这个结果解析本来可以用Retrofit自带的gson解析，但是因为这里json嵌套了一个data，所以我们手动解析，然后返回ResultData
 * Created by zhengmj on 18-9-6.
 */

public abstract class MyCallBack implements Callback<ResponseBody> {
    private int type;//默认0,登录1,验证码2
    Context ctx;
    String rawResult;//这个是原始result
    ResultDataParser resultDataParser;


    public MyCallBack(Context ctx) {
        this(ctx, 0);
    }

    public MyCallBack(Context ctx, int type) {
        this.ctx = ctx;
        this.type = type;
        resultDataParser = new ResultDataParser();
    }

    public String getRawResult() {//获取原始result
        return rawResult;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.d("MyCallBack", "onResponse  response==" + response);
        Log.d("MyCallBack", "response.body==" + (response == null ? "null" : response.body()));
        if (ctx == null || ((Activity) ctx).isFinishing()) return;
        if (response != null && response.body() != null) {
            try {
                Log.d("MyCallBack", "response.code==" + response.code());
                rawResult = response.body().string();//这句话只能调用一次,第二次调用会出错
                Log.d("MyCallBack", "response.rawResult==" + rawResult);
                ResultData resultData = resultDataParser.parse(new JSONObject(rawResult));
                if (resultData != null) {
                    if (!resultData.isSuccess()) {
                        handleSuccessFalse(resultData);
                    }
                    callBack(resultData);
                } else {
                    Log.d("MyCallBack", "333");
                    handleError(call, response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleError(call, response);
                Log.d("MyCallBack", "4444 e==" + e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                handleError(call, response);
                Log.d("MyCallBack", "555 e==" + e.toString());
            }
        } else {
            handleError(call, response);
        }
    }


    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("MyCallBack", "Throwable==" + t);
        Log.d("MyCallBack", "call.isCancel==" + call.isCanceled());
        if (ctx == null || ((Activity) ctx).isFinishing()) return;
        if (!call.isCanceled()) {//cancle也会回调这个，但是不需要处理
//            CommonUtil.showmessage(t.getMessage(), ctx);
            handleError(call);
        }
    }

    private void handleError(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (ctx != null) {
            if (response != null && !TextUtils.isEmpty(response.message())) {
//                CommonUtil.showmessage(response.message(), ctx);
            } else {
                if (ctx != null) CommonUtil.showmessage("數據請求异常", ctx);
            }
        }
        handleError(call);
    }

    //看情况是否需要重写,一般重写这个方法是为了当网络错误的时候取消进度框或者下拉刷新状态
    protected void handleError(Call<ResponseBody> call) {
    }

    //这个是弹出交易密码框之后，输入交易密码之后的回调，并且把密码返回（已经MD5加密）
    protected void onPassWordFinsh(String pwString) {
    }

    //需要滑动时回调
    protected void onDragSuccessCallback(String dragImgKey, int locationx) {

    }

    //看情况是否需要重写，如何后台返回success为false，默认处理是弹出Toast，如何需要其他处理就需要重写这个方法
    protected void handleSuccessFalse(ResultData resultData) {
        if (!TextUtils.isEmpty(resultData.msg)) {
            if (ctx != null){
                if (!resultData.msg.equals("请重新获取拖动图片！")){
                    CommonUtil.showmessage(resultData.msg, ctx);
                }
            }
        }
        CommonUtil.logoutToLoginActity(resultData.code, resultData.msg);//40009 session 过期
        if (resultData.code == 40030) {//弹出交易密码
            FixAuthPasswordDialog2 passwordDialog = FixAuthPasswordDialog2.newInstance(ctx);
            passwordDialog.setCallBack(new FixAuthPasswordDialog2.PassWordFinish() {
                @Override
                public void checkPassWordFinsh(String pwString) {
                    //验证密码后进行修改密码
//                    if (passwordDialog != null && passwordDialog.isShowing()) {
//                    passwordDialog.dismiss();
//                    }
                    onPassWordFinsh(MD5.getMessageDigest(pwString));
                }
            });
            if (ctx != null && !((Activity) ctx).isFinishing())
                passwordDialog.show(((BaseBarActivity) ctx).getSupportFragmentManager(), "");
        } else if (resultData.code == 40031) {//还未设置交易密码
            showGoSetTradePassDialog(resultData.msg);
        } else if (resultData.code == 40016) {//弹出拖动验证
            DragFragment dragFragment = DragFragment.newInstance(new DragFragment.OnSuccessCallback() {
                @Override
                public void onSuccess(String dragImgKey, int locationx) {
                    onDragSuccessCallback(dragImgKey, locationx);
                }
            });
            Bundle bundle = new Bundle();
            bundle.putInt(DragFragment.TEXT, type);
            dragFragment.setArguments(bundle);
            dragFragment.show(((AppCompatActivity) ctx).getSupportFragmentManager(), "");
        } else if (resultData.code == 40080) {//40080代表跳转充值页面。当返回40080的时候entrustAmount要用后台返回的，因为小数点的问题
            int recharge = resultData.getItem("recharge", Integer.class);
            showGoRechargeDialog(resultData.msg, recharge);
//            PageJumpUtil.pageJump(ctx, USDTTradeActivity.class);
        } else if (resultData.code == 40090) {//提币需要实名认证
//            PageJumpUtil.pageJump(ctx, IdentityAuthenActivity.class);
            showGoIdentityAuthDialog(resultData.msg);
        } else if (resultData.code == 44010) {//卖币时如果没有收款方式就跳转至收款管理
            showGoPaymentTermDialog(resultData.msg);
        } else if (resultData.code == 1005){
            showHuazhuanDialog(resultData.msg);
        }
    }

    TjrBaseDialog goHuazhuanDialog;
    private void showHuazhuanDialog(final String msg) {//去划转
        goHuazhuanDialog = new TjrBaseDialog(ctx) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.pageJump(ctx, TransferCoinActivity.class);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        goHuazhuanDialog.setMessage(msg);
        goHuazhuanDialog.setTvTitle(ctx.getResources().getString(R.string.tishi));
        goHuazhuanDialog.setBtnOkText(ctx.getResources().getString(R.string.quhuazhuan));
        goHuazhuanDialog.show();
    }


    TjrBaseDialog goPaymentTermDialog;
    private void showGoPaymentTermDialog(final String msg) {
        goPaymentTermDialog = new TjrBaseDialog(ctx) {
            @Override
            public void onclickOk() {
                dismiss();
                PaymentTermActivity.pageJump(ctx, 0);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        goPaymentTermDialog.setMessage(msg);
        goPaymentTermDialog.setTvTitle(ctx.getResources().getString(R.string.tishi));
        goPaymentTermDialog.setBtnOkText(ctx.getResources().getString(R.string.qutianjiashoukuanfangshi));
        goPaymentTermDialog.show();
    }

    RechargeOrTransferDialog goRechargeDialog;
    private void showGoRechargeDialog(final String msg, final int recharge) {
        goRechargeDialog = new RechargeOrTransferDialog(ctx) {
            @Override
            public void onclickOk() {//去划转
                dismiss();
//                if (ctx instanceof TJRBaseToolBarActivity) {
//                    TJRBaseToolBarActivity tjrBaseToolBarActivity = (TJRBaseToolBarActivity) ctx;
//                }
//                PageJumpUtil.pageJump(ctx, USDTTradeActivity.class);
                PageJumpUtil.pageJump(ctx, TransferCoinActivity.class);
            }

            @Override
            public void onclickRecharge() {//去充值
//                RechargeCoinActivity.pageJump(ctx,"USDT");
                PageJumpUtil.pageJump(ctx, LegalMoneyActivity.class);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        goRechargeDialog.setMessage(msg);
        goRechargeDialog.setTvTitle(ctx.getResources().getString(R.string.wenxintishi));
        goRechargeDialog.show();
    }

    TjrBaseDialog goSetTradePassDialog;

    private void showGoSetTradePassDialog(final String msg) {
        goSetTradePassDialog = new TjrBaseDialog(ctx) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.pageJump(ctx, SettingPayPasswordActivity.class);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        goSetTradePassDialog.setMessage(msg);
        goSetTradePassDialog.setBtnOkText(ctx.getResources().getString(R.string.qushezhi));
        goSetTradePassDialog.setTitleVisibility(View.VISIBLE);
        goSetTradePassDialog.setTvTitle(ctx.getResources().getString(R.string.wenxintishi));
        goSetTradePassDialog.show();
    }

    TjrBaseDialog goIdentityAuth;
    private void showGoIdentityAuthDialog(final String msg) {
        goIdentityAuth = new TjrBaseDialog(ctx) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.pageJump(ctx, IdentityAuthenActivity.class);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        goIdentityAuth.setMessage(msg);
        goIdentityAuth.setBtnOkText(ctx.getResources().getString(R.string.qushimingrenzheng));
        goIdentityAuth.setTitleVisibility(View.VISIBLE);
        goIdentityAuth.setTvTitle(ctx.getResources().getString(R.string.wenxintishi));
        goIdentityAuth.show();
    }

    protected abstract void callBack(ResultData resultData);

//    public interface ParserCallBack {
//        void callBack(ResultData resultData);
//    }
}
