package com.bitcnew.module.wallet.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.trade.adapter.LeverTypeAdapter;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.SimpleSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 增加保证金
 */

public class AddMarginFragmentDialog extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvEnableBalance)
    TextView tvEnableBalance;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvProtocol)
     TextView tvProtocol;
    @BindView(R.id.rvKeyType)
    RecyclerView rvKeyType;

    private String symbol;

    private LeverTypeAdapter leverTypeAdapter;
    private String leverType = "";//保证金

    private OnAppendBailBalanceListen onAppendBailBalanceListen;

    private Call<ResponseBody> getPrybarConfigCall;


    public void setOnAppendBailBalanceListen(OnAppendBailBalanceListen onAppendBailBalanceListen) {
        this.onAppendBailBalanceListen = onAppendBailBalanceListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static AddMarginFragmentDialog newInstance(String symbol) {
        AddMarginFragmentDialog dialog = new AddMarginFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        dialog.setArguments(bundle);

        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreate ");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        //入参处理
        Bundle b = getArguments();
        if (null == b) return;
        symbol = b.getString(CommonConst.SYMBOL);

    }

    @Override
    public void onStart() {
        CommonUtil.LogLa(2, "OLStarHomeDialogFragment                      ---> onStart ");
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.dialog_add_margin, container, false);
        ButterKnife.bind(this, v);
        tvSubmit.setOnClickListener(this);
        leverTypeAdapter = new LeverTypeAdapter(getActivity());
        rvKeyType.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvKeyType.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 0, 10, 5, 5));
        rvKeyType.setAdapter(leverTypeAdapter);
//        leverTypeAdapter.setData(new String[]{"18", "20", "30", "50", "80", "100",});
        leverTypeAdapter.setOnItemclickListen(new LeverTypeAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String leverType) {
                AddMarginFragmentDialog.this.leverType = leverType;
            }
        });
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString normalText = new SpannableString(getResources().getString(R.string.quedingjidaibiaoniyitongyibingjieshou));
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString clickText = new SpannableString(getResources().getString(R.string.shuzizichanjiedaifuwuhetong));

        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), CommonConst.PASSGEDETAIL_25);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(getActivity(), R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString clickText2 = new SpannableString(getResources().getString(R.string.gangganjiaoyixieyi));
        clickText2.setSpan(new ForegroundColorSpan(Color.parseColor("#376eb8")), 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), CommonConst.PASSGEDETAIL_26);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(getActivity(), R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(clickText);
        spannableStringBuilder.append(getResources().getString(R.string.he));
        spannableStringBuilder.append(clickText2);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);
        startPrybarConfig();
        return v;
    }
    private String holdUsdt = "0.0";
    private String[] bailBalanceList;

    private void startPrybarConfig() {
        CommonUtil.cancelCall(getPrybarConfigCall);
        getPrybarConfigCall = VHttpServiceManager.getInstance().getVService().prybarConfig(symbol);
        getPrybarConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    bailBalanceList = resultData.getStringArray("bailBalanceList");

                    tvEnableBalance.setText(getResources().getString(R.string.keyong) + holdUsdt + "USDT");
                    leverTypeAdapter.setData(bailBalanceList);

                    if (bailBalanceList != null && bailBalanceList.length > 0) {
                        leverType = bailBalanceList[0];
                        leverTypeAdapter.setSelected(leverType);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->            onResume   isVisible = " + getUserVisibleHint());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->           setUserVisibleHint  isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnAppendBailBalanceListen {
        void OnAppendBailBalance(String bailBalance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                if (onAppendBailBalanceListen != null) {
                    onAppendBailBalanceListen.OnAppendBailBalance(leverType);
                }
                break;

            default:
                break;
        }
    }

}
