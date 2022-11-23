package com.bitcnew.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.adapter.HomeTokenAccountAdapter;
import com.bitcnew.module.home.bean.RecordListBean;
import com.bitcnew.module.home.entity.AccountInfo;
import com.bitcnew.module.home.trade.adapter.TakeCoinHistoryAdapter;
import com.bitcnew.module.home.trade.entity.TakeCoinHistory;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryDetailsActivity;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeTokenAccountFragment extends UserBaseFragment {
    @BindView(R.id.txt_chakanruhehuoqutoken)
    TextView txt_chakanruhehuoqutoken;
    @BindView(R.id.tvTolAssetsText)
    TextView tvTolAssetsText;
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.ll_shizhi)
    LinearLayout ll_shizhi;
    @BindView(R.id.tvShizhi)
    TextView tvShizhi;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvEableBalance)
    TextView tvEableBalance;
    @BindView(R.id.tvFrozenBalance)
    TextView tvFrozenBalance;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private HomeTokenAccountAdapter adapter;
    private AccountInfo balanceAccount;
    private int pageSize = 15;
    private int pageNo = 1;
    private Call<ResponseBody> withdrawCoinListCall;
    private Group<TakeCoinHistory> group;
    private int type = 0;

    public static HomeTokenAccountFragment newInstance(int type) {
        HomeTokenAccountFragment fragment = new HomeTokenAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetwithdrawCoinList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetwithdrawCoinList();
        }
    }

    public void setData(AccountInfo balanceAccount) {
        if (rvList == null) return;
        this.balanceAccount = balanceAccount;
        if (balanceAccount != null) {
            tvTolAssets.setText(balanceAccount.assets);
            tvTolAssetsCny.setText(balanceAccount.assetsCny);
            tvEableBalance.setText(balanceAccount.holdAmount);
            tvFrozenBalance.setText(balanceAccount.frozenAmount);
            tvShizhi.setText(balanceAccount.assetsCny);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_token_account, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle.containsKey("type")) {
            type = bundle.getInt("type", 1);
        }
        if (type==1){
            tvTolAssetsText.setText(getActivity().getResources().getString(R.string.zongzichantoken));
            tvTolAssetsCny.setVisibility(View.INVISIBLE);
            txt_chakanruhehuoqutoken.setVisibility(View.VISIBLE);
            ll_shizhi.setVisibility(View.VISIBLE);
        }else{
            tvTolAssetsText.setText(getActivity().getResources().getString(R.string.yuezhanghuzongzichanusdt));
            tvTolAssetsCny.setVisibility(View.VISIBLE);
            txt_chakanruhehuoqutoken.setVisibility(View.GONE);
            ll_shizhi.setVisibility(View.INVISIBLE);
        }

        adapter = new HomeTokenAccountAdapter(getActivity(),1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.dividerColor)));
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
        adapter.setOnPlayClickListener(new HomeTokenAccountAdapter.OnPlayClickListener() {
            @Override
            public void onSelClick(String orderId) {
                long L = Long.parseLong(orderId);
                LeverInfoActivity.pageJump(getActivity(), L,"");
            }
        });

        txt_chakanruhehuoqutoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), CommonConst.PASSGEDETAIL_222);
            }
        });
        return view;
    }

    TjrBaseDialog questionMarkDialog;
    private void showSubmitTipsDialog(String tips) {
        questionMarkDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage(getResources().getString(R.string.shizhixiaoyu) + tips + getResources().getString(R.string.usdtdebizhong));
        questionMarkDialog.setBtnOkText(getResources().getString(R.string.zhidaole));
        questionMarkDialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void startGetwithdrawCoinList() {
        CommonUtil.cancelCall(withdrawCoinListCall);
        withdrawCoinListCall = VHttpServiceManager.getInstance().getVService().getRecordList("token",pageNo);
        withdrawCoinListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<RecordListBean.DataBean>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        adapter.setGroup(group);
                        rvList.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    } else {
                        rvList.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
//                    if (takeCoinHistoryAdapter.getRealItemCount() > 0) {
//                        takeCoinHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
//                    }
                }
            }
        });
    }
}
