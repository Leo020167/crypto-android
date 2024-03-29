package com.bitcnew.http.tjrcpt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bitcnew.SpUtils;
import com.bitcnew.http.BuildConfig;
import com.bitcnew.http.TjrBaseApi;
import com.bitcnew.http.retrofitservice.FileUploadService;
import com.bitcnew.http.retrofitservice.PublicParameterInterceptor;
import com.bitcnew.http.retrofitservice.VService;
import com.bitcnew.http.util.MD5;
import com.google.gson.Gson;
//import com.cropyme.http.retrofitservice.ImredzService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by zhengmj on 19-2-16.
 */

public class VHttpServiceManager {
    private static VHttpServiceManager instance;
    private VService vService,predictGameService,marketService,mHomeMarkService;
    private FileUploadService fileUploadService;

//    private final String mApiCropymeBaseUri;
//    private final String mApiCropymeFileBaseUri;

    public static final int TIMEOUT = 30;//超时30秒
    public static final int UPLOADTIMEOUT = 60;//超时60秒
    public static final String API_KEY = "2CE2BA19C7CA4937AD18BC1AFEE034E8";
    public static final String API_SECRET = "C2AE585AB6814937960DF0E0A22DF3FD";

    public static final String UPLOADFILE_URL = "upload/file.do";//上传文件

    public static final String URL_API_HOME = "config/all.do";
    public static final String URL_API_BOOTPAGE = "config/bootPage.do";
    public static final String CONFIG_COUNTRYCODEINFOLIST = "area/list.do";
    public static final String V_HOME_ACCOUNT = "home/account.do";
    public static final String V_MINING_INFO = "mining/info.do";


    public static final String V_AGENT_INFO = "agent/info.do";
    public static final String V_AGENT_CONFIG = "agent/config.do";
    public static final String V_AGENT_APPLY = "agent/apply.do";
    public static final String V_AGENT_LIJISHENGJI = "agent/upgradeLevel.do";
    public static final String V_CONTACT_INFO = "user/contactInfo.do";
    public static final String V_EMAIL_GET = "email/get.do";
    public static final String V_USER_INFO = "user/info.do";
    public static final String V_UPDATE_EMAIL = "user/security/updateEmail.do";
    public static final String V_CHECK_EMAIL_CODE = "user/security/checkEmailCode.do";
    public static final String V_QUOTE_HOMEPAGE = "quote/homePage.do";
    public static final String V_INVITE_HOME = "invite/home.do";
    public static final String V_INVITE_BUY = "invite/buy.do";
    public static final String PRO_ORDER_QUERYSUM = "pro/order/querySum.do";


    public static final String V_SHOW_LIST = "show/list.do";
    public static final String V_SHOW_CREATE = "show/add.do";
    public static final String V_SHOW_DETAIL = "show/detail.do";
    public static final String V_SHOW_COMMENT = "show/commentList.do";
    public static final String V_SHOW_COMMENT_SEND = "show/comment.do";
    public static final String V_SHOW_LIKE_OR_NOT = "show/goodOrUnGood.do";

    public static final String V_CIRCLE_OPP_GET = "circle/opp/get.do";

    public static final String V_CIRCLE_OPP_SEARC = "circle/opp/search.do";
    public static final String V_CIRCLE_OPP_APPLYJOINCIRLCE = "circle/opp/applyJoinCirlce.do";
    public static final String V_CIRCLE_OPP_GETMEMBERLIST = "circle/opp/getMemberList.do";
    public static final String V_CIRCLE_OPP_FINDAPPLIES = "circle/opp/findApplies.do";
    public static final String V_CIRCLE_OPP_HANDLEAPPLY = "circle/opp/handleApply.do";
    public static final String V_CIRCLE_OPP_UPDATEROLE = "circle/opp/updateRole.do";
    public static final String V_CIRCLE_OPP_REMOVEMEMBER = "circle/opp/removeMember.do";
    public static final String V_CIRCLE_OPP_BLACKLIST = "circle/opp/blackList.do";
    public static final String V_CIRCLE_OPP_HANDLEBLACK = "circle/opp/handleBlack.do";
    public static final String V_CIRCLE_OPP_SETUPJOINMODE = "circle/opp/setupJoinMode.do";
    public static final String V_CIRCLE_OPP_EXIT = "circle/opp/exit.do";
    public static final String V_CIRCLE_OPP_SETSPEAKSTATUS = "circle/opp/setSpeakStatus.do";

    public static final String V_CIRCLE_OPP_SETMSGALERT = "circle/opp/setMsgAlert.do";


    public static final String V_LIVE_DISCOVER = "home/discover.do";
    public static final String V_LIVE_ROOMLIST = "live/msg/list.do";
    public static final String V_LIVE_ROOMINFO = "live/room/info.do";
    public static final String V_LIVE_LOD = "live/msg/goodOrUnGood.do";
    public static final String V_LIVE_SEND = "live/msg/send.do";
    public static final String V_LIVE_EDIT = "live/room/update.do";
    public static final String V_LIVE_DELETE = "live/msg/delete.do";

    public static final String V_CIRCLE_CREATE = "circle/opp/create.do";
    public static final String V_CIRCLE_UPDATE = "circle/opp/update.do";


    public static final String V_COIN_LIST = "coin/list.do";
    public static final String V_COIN_TOIN = "coin/toIn.do";
    public static final String V_COIN_TOOUT = "coin/toOut.do";
    public static final String V_COIN_TOFASTIN = "coin/toFastIn.do";

    public static final String V_GET_SMS = "sms/get.do";//获取短信验证码
    public static final String V_GET_EMAIL = "email/get.do";//获取邮箱验证码
    public static final String V_SETPAYPASS = "user/security/setPayPass.do";//

    public static final String MESSAGE_FIND = "message/find.do";
    public static final String USER_UPDATE_INFO = "user/updateUserInfo.do";
    public static final String USER_MYCOIN_INFO = "user/myCoin.do";

    public static final String V_HOME_ACTIVITY = "home/activity.do";
    public static final String V_ACTIVITY_SIGN = "home/activitySign.do";

    public static final String USER_UPDATE_USERPASS = "user/security/updateUserPass.do";
    public static final String V_CHECKIDENTITY = "user/security/checkIdentity.do";//
    public static final String V_LOGOUT = "security/loginOut.do";//退出登录
    public static final String V_CHANGE_PHONE = "user/security/changePhoneTwo.do";//更改绑定手机号
    public static final String V_UPDATE_PHONE = "user/security/updatePhone.do";//第一次绑定手机号



    public static final String V_CHAT_GETSERVICECHATTOPIC = "config/svcChatTopic.do";//

    public static final String V_USER_HOMEPAGE = "user/homePage.do";//

    public static final String CHECK_DRAG = "security/checkDragImg.do";//验证图片验证码

    public static final String IDENTITY_GET = "identity/get.do";//
    public static final String IDENTITY_SUBMIT = "identity/submit.do";//
    public static final String LOGIN = "security/login.do";//登录
    public static final String REGISTE = "security/register.do";//注册
    public static final String RECEIVE = "security/forgetPass.do";//找回密码

    public static final String SUB_GETLIST = "subscribe/getList.do";//新币申购列表
    public static final String SUB_GETDETAIL = "subscribe/getDetail.do";//新币申购详情
    public static final String SUB_APPLY = "subscribe/apply.do";//新币申购
    public static final String SUB_ALLIN = "subscribe/allIn.do";//全额申购接口

    public static final String PLEDGE_LIST = "/procoin/pledge/list.do";
    public static final String PLEDGE_RECORD_LIST = "/procoin/pledge/recordList.do";
    public static final String PLEDGE_COMMIT = "/procoin/pledge/commit.do";

//    public static final String SHARE_GETSHAREINFO = "share/getShareInfo.do";//


    public static final String FOLLOW = "home/attentionList.do";//关注
    public static final String MARKET = "home/market.do";//行情
    public static final String CROPYME = "home/cropyme.do";//CROPYME页

    public static final String HOMEMY = "home/my.do";//我的  获取信息

    public static final String HOME_ABILITYVALUETOAWARD = "home/abilityValueToAward.do";//能力值兑换



    public static final String CONFIG = "trade/config.do";//获取USDT汇率、随机尾数

//    public static final String INOUTCREATE = "cash/inOut/create.do";//充值/提现

    public static final String INOUTMARKPAY = "trade/orderCash/markPay.do";//用户标记付款
    public static final String ORDERCANCEL = "trade/orderCash/cancel.do";//用户标记付款

    public static final String INOUTCANCEL = "trade/cash/order/cancel.do";//用户撤销订单(订单state=0可撤销)


    public static final String INOUTADMINDONE = "cash/inOut/admin/done.do";//服务人员放币(充值)/服务人员确认打款(提现)

    public static final String INOUTADMINUNPAYCANCEL = "cash/inOut/admin/unpayCancel.do";//服务人员放币(充值)/服务人员确认打款(提现)

    public static final String RECEIPTRECEIPTSFORADD = "receipt/receiptListForAdd.do";//用户添加收款方式，获取支持的收款方式
    public static final String RECEIPTMYRECEIPTS = "receipt/userReceiptList.do";//获取已添加的收款方式
    public static final String RECEIPTRECEIPTSFORPAY = "receipt/userPayList.do";//用户买卖币，获取平台的收款方式
    public static final String RECEIPTSAVERECEIPT = "receipt/saveReceipt.do";//添加/修改收款方式
    public static final String RECEIPTDELETE = "receipt/delete.do";//删除收款方式收款方式
    public static final String RECEIPTGETDEFAULT = "receipt/getDefault.do";//获取默认收款方式
    public static final String RECEIPTSETDEFAULT = "receipt/setDefault.do";//设置用户默认的收款方式


    public static final String TRADE_ORDER = "trade/order.do";//下单（余额充足）

    public static final String TRADE_CASH_ORDER_BUY = "trade/orderCash/buy.do";//充值
    public static final String TRADE_CASH_ORDER_SELL = "trade/orderCash/sell.do";//提现


    public static final String TRADE_CHECKOUT = "trade/checkOut.do";//输入时实时获取跟单数量、金额、预计数量、预计金额等等

    public static final String TRADE_HISTORY = "trade/history/orderCashList.do";//现金订单记录（包括充值提现）


    public static final String TRADE_ORDERLIST = "trade/history/orderList.do";//币币订单记录


    public static final String TRADE_CANCEL = "trade/cancel.do";//【币币】交易记录中撤销订单

    public static final String TRADE_CASH_ORDER_CANCEL = "trade/cash/order/cancel.do";//【现金】撤销订单

    public static final String TRADE_CASH_ORDER_DETAIL = "trade/history/orderCash/detail.do";//【现金】订单详情


    public static final String TRADE_ORDER_DETAIL = "trade/history/order/detail.do";//【币币】订单详情
    public static final String TRADE_INOUTHOME = "trade/inOutHome.do";



    public static final String PERSONAL_HOME = "personal/home.do";//获取主页数据

//    public static final String PERSONAL_TRENDNUM = "personal/trendNum.do";//获取个人业绩走势
//    public static final String PERSONAL_COPYNUM = "personal/copyNum.do";//获取跟单人气
//    public static final String PERSONAL_TRADENUM = "personal/tradeNum.do";//获取交易次数

    public static final String PERSONAL_TRADENUM = "personal/trendChart.do";//获取交易次数


    public static final String FOLLOW_UNBIND = "follow/unBind.do";//解绑
    public static final String FOLLOW_GETTYPES = "follow/getTypes.do";//获取跟单类型
    public static final String FOLLOW_APPLYFORFOLLOW = "follow/applyForFollow.do";//申请带单

    public static final String FOLLOW_UPDATEFOROPEN = "follow/updateForOpen.do";//跟单账户－更新开通跟单权限
    public static final String FOLLOW_UPDATEMULTIPLE = "follow/updateMultiple.do";//跟单账户－更改跟单倍数


    public static final String ATTENTION_ADD= "attention/add.do";//关注
    public static final String ATTENTION_CANCEL= "attention/cancel.do";//取消关注



    public static final String COPY_SLAVE_ORDER = "copy/slave/order.do";//提交跟单接口
    public static final String COPY_SLAVE_ORDERDETAIL = "copy/slave/orderDetail.do";//跟单详情
    public static final String COPY_SLAVE_APPENDBALANCE = "copy/slave/appendBalance.do";//跟单追加金额
    public static final String COPY_SLAVE_UPDATEOPTION = "copy/slave/updateOption.do";//跟单修改配置选项
    public static final String COPY_SLAVE_UPDATESTOPTIPS = "copy/slave/updateStopTips.do";//跟单修改配置选项前调用
    public static final String COPY_SLAVE_CLOSEORDER = "copy/slave/closeOrder.do";//停止跟单

    public static final String COPY_SLAVE_TRADELIST = "copy/slave/tradeList.do";//跟单交易明细
    public static final String COPY_HISTORY = "copy/history.do";//跟单记录

    public static final String COPY_SLAVE_CLOSEORDERTIPS = "copy/slave/closeOrderTips.do";//是否有委托单


    public static final String COPY_DATA_HOME = "copy/data/home.do";//
    public static final String COPY_DATA_HOLDCOST = "copy/data/holdCost.do";//
    public static final String COPY_DATA_COPYBALANCE = "copy/data/copyBalance.do";//
    public static final String COPY_DATA_HOLDMARKETVALUE = "copy/data/holdMarketValue.do";//


    public static final String SEARCH_GET = "search/get.do";//

    public static final String SEARCH_COIN = "search/coin.do";//


    public static final String WITHDRAWCOIN_COINLIST = "depositeWithdraw/coinList.do";//
    public static final String WITHDRAWCOIN_SUBMIT = "depositeWithdraw/submit.do";//
    public static final String WITHDRAWCOIN_CANCEL = "depositeWithdraw/cancel.do";//
    public static final String WITHDRAWCOIN_LIST = "depositeWithdraw/list.do";//
    public static final String WITHDRAWCOIN_GETCOININFO = "depositeWithdraw/getCoinInfo.do";//
    public static final String V_ACCOUNT_RECORDLIST = "account/recordList.do";
    public static final String WITHDRAWCOIN_GETINFO = "depositeWithdraw/getInfo.do";//
    public static final String WITHDRAWCOIN_LOCALSUBMIT = "depositeWithdraw/localSubmit.do";//


    public static final String PERSONAL_CONSOLE = "personal/console.do";//

    public static final String CONFIG_OPENCOPYRULES = "config/openCopyRules.do";//

    public static final String USER_SECURITY_OPENCOPY = "user/security/openCopy.do";//

    public static final String COIN_INFO = "coin/info.do";//

    public static final String SHARE_GETSHAREINFO = "share/getShareInfo.do";//


    public static final String DRAG_IMG = "security/outDragImg.do";//获取图片验证码

    public static final String GETDNS = "security/getDynamicDns.do";//获取DNS

    public static final String COIN_ASSET = "account/asset.do";
    public static final String COIN_INTOSUBHOME = "coin/sub/intoSubHome.do";
    public static final String COIN_NOTICE = "coin/sub/notice.do";
    public static final String COIN_SUBBUY = "coin/sub/subBuy.do";

    public static final String YYB_NOTICE = "yyb/notice.do";
    public static final String YYB_REPOHOME = "yyb/repoHome.do";
    public static final String YYB_REPO = "yyb/repo.do";

    public static final String ACCOUNT_HOLD = "account/hold.do";
    public static final String ACCOUNT_LISTACCOUNTTYPE = "account/listAccountType.do";
    public static final String ACCOUNT_OUTHOLDAMOUNT = "account/outHoldAmount.do";
    public static final String ACCOUNT_GETSYMBOLMAXAMOUNT = "account/getSymbolMaxAmount.do";
    public static final String ACCOUNT_TRANSFER = "account/transfer.do";
    public static final String ACCOUNT_QUERYTRANSFERLIST = "account/queryTransferList.do";


    public static final String OTC_SUPPORT_OTCLIST = "otc/support/otcList.do";

    public static final String PREDICT_RECORD = "predict/record.do";


    public static final String OPTIONAL_COIN_FIND = "optional/coin/find.do";//我的自选
    public static final String OPTIONAL_COIN_FINDALL = "optional/coin/findAll.do";//编辑获取我的自选


    public static final String OPTIONAL_COIN_ADD = "optional/coin/add.do";//添加自选
    public static final String OPTIONAL_COIN_DEL = "optional/coin/del.do";//删除自选
    public static final String OPTIONAL_COIN_SYNSORT = "optional/coin/synSort.do";//自选排序
    public static final String OPTIONAL_COIN_ISOPTIONAL = "optional/coin/isOptional.do";//是否是自选
    public static final String OTC_MAINAD_CONFIG = "otc/mainad/config.do";//获取币种的符号接口

    public static final String PRYBAR_STORE_ASSET = "prybar/store/asset.do";//存币宝->进入某个存币宝数据
    public static final String PRYBAR_STORE_CONFIG = "prybar/store/config.do";//存币宝->转入转出配置信息提前获取
    public static final String PRYBAR_STORE_CREATEIN = "prybar/store/createIn.do";//存币宝->转入
    public static final String PRYBAR_STORE_CREATEOUT = "prybar/store/createOut.do";//存币宝->转出
    public static final String PRYBAR_CONFIG = "pro/order/config.do";//获取交易配置信息
    public static final String PRYBAR_CHECKOUT = "pro/order/checkOut.do";//选择保证金发生变化获取相关可视化数据
    public static final String PRYBAR_ORDER_CREATEORDER = "pro/order/open.do";//下单
    public static final String PRYBAR_ORDER_DETAIL = "pro/order/detail.do";//订单详情
    public static final String PRYBAR_ORDER_CLOSEORDER = "pro/order/close.do";//平仓
    public static final String PRYBAR_ORDER_CANCEL = "pro/order/cancel.do";//撤单

    public static final String PRYBAR_ORDER_UPDATEWINPRICE = "pro/order/updateWinPrice.do";//止盈
    public static final String PRYBAR_ORDER_UPDATELOSSPRICE = "pro/order/updateLossPrice.do";//止损

    public static final String HOME_CHECKSERVER = "home/checkServer.do";//检查服务是否连通
    public static final String PRYBAR_ORDER_QUERYLIST = "pro/order/queryList.do";//杠杆交易记录
    public static final String PRYBAR_ORDER_QUERYFOLLOWLIST = "pro/order/queryFollowList.do";//跟单记录

    public static final String PRYBAR_ORDER_APPENDBAILBALANCE = "prybar/order/appendBailBalance.do";//增加保证金
    public static final String PRYBAR_ORDER_CHECKWINLOSS = "prybar/order/checkWinLoss.do";//止盈止损检查：第1步
    public static final String PRYBAR_ORDER_UPDATEWINLOSS = "prybar/order/updateWinLoss.do";//止盈止损提交：第2步

    public static final String ARTICLE_NOTICELIST = "article/noticeList.do";
    public static final String ARTICLE_HELPLIST = "article/helpList.do";
    public static final String ARTICLE_NOTICETOP = "article/noticeTop.do";


    public static final String OTC_MAINAD_FINDADLIST = "otc/mainad/findAdList.do";
    public static final String OTC_MAINAD_CREATEORDER = "otc/mainad/createOrder.do";
    public static final String OTC_MAINAD_GETORDERDETAIL = "otc/mainad/getOrderDetail.do";
    public static final String OTC_MAINAD_TOPAYORDER = "otc/mainad/toPayOrder.do";
    public static final String OTC_MAINAD_TOMARKPAYORDERSUCCESS = "otc/mainad/toMarkPayOrderSuccess.do";
    public static final String OTC_MAINAD_TOCONFIRMRECEIVEDPAY = "otc/mainad/toConfirmReceivedPay.do";
    public static final String OTC_MAINAD_FINDORDERLIST= "otc/mainad/findOrderList.do";
    public static final String OTC_MAINAD_CANCELORDER= "otc/mainad/cancelOrder.do";

    public static final String OTC_MAINAD_GETINITAPPEALLIST= "otc/mainad/getInitAppealList.do";
    public static final String OTC_MAINAD_SUBMITAPPEAL= "otc/mainad/submitAppeal.do";



    public static final String OTC_PAYMENT_FINDMYPAYMENTLIST= "otc/payment/findMyPaymentList.do";
    public static final String OTC_PAYMENT_FINDPAYMENTOPTIONLIST= "otc/payment/findPaymentOptionList.do";
    public static final String OTC_PAYMENT_SAVEPAYMENT= "otc/payment/savePayment.do";
    public static final String OTC_PAYMENT_DELETE= "otc/payment/delete.do";
    public static final String OTC_CERTIFICATION_GETCERTIFICATIONINFO= "otc/certification/getCertificationInfo.do";
    public static final String OTC_CERTIFICATION_AUTHENTICATE= "otc/certification/authenticate.do";
    public static final String OTC_CERTIFICATION_APPLYFORCANCELLATION= "otc/certification/applyForCancellation.do";

    public static final String OTC_MYAD_FINDMYADLIST= "otc/myad/findMyAdList.do";
    public static final String OTC_MYAD_GETMYADINFO= "otc/myad/getMyAdInfo.do";
    public static final String OTC_MYAD_SETONLINE= "otc/myad/setOnline.do";
    public static final String OTC_MYAD_DELMYAD= "otc/myad/delMyAd.do";
    public static final String OTC_MYAD_ADDMYAD= "otc/myad/addMyAd.do";
    public static final String OTC_MYAD_UPDATEMYAD= "otc/myad/updateMyAd.do";

    public static final String OTC_MYAD_GETADPRICE= "otc/myad/getAdPrice.do";


    public static final String MARKETDATA = "quote/marketData.do";//行情

    public static final String QUOTE_MINUTELINE = "quote/getMinuteLine.do";
    public static final String QUOTE_MINUTE5DAYLINE = "quote/getMinute5DayLine.do";
    public static final String QUOTE_KLINE = "quote/kline.do";
    public static final String QUOTE_REAL = "quote/real.do";

    //项目名
    public static final String PROJECT_NAME_URI = "/procoin/";
    public static final String PREDICT_PROJECT_NAME_URI = "/procoin-predict/";
    public static final String UPLOAD_PROJECT_NAME_URI = "/procoin-file/";
    public static final String MARKET_PROJECT_NAME_URI = "/procoin-market/";


    private Context context;

    public static VHttpServiceManager getInstance() {
        if (instance == null) {
            synchronized (VHttpServiceManager.class) {
                if (instance == null) instance = new VHttpServiceManager();
            }
        }
        return instance;
    }

    private VHttpServiceManager() {
//        mApiCropymeBaseUri = TjrBaseApi.mApiCropymeBaseUri.uri();
//        mApiCropymeFileBaseUri = TjrBaseApi.mApiCropymeBaseUploadFile.uri();
    }

    public synchronized void resetService(Context context) {
        this.context = context;
        vService = null;
        getVService();
    }

    public void test() {
        Map<String, String> data = new HashMap<>();
        data.put("appCode", "WorldCoin");
        data.put("appName", "WorldCoin交易所");
        data.put("appGateway", TjrBaseApi.mApiCropymeBaseUri);
        data.put("channel", "ANDROID");
        data.put("clientVersion", Build.VERSION.RELEASE);
        data.put("clientDeviceId", getIMEI(context));
        data.put("clientModel", android.os.Build.MODEL);
        data.put("reportType", "startup");

        Map<String, Object> entity = new HashMap<>();
        entity.put("service", "licenceReport");
        entity.put("data", data);

        post(entity);
    }

    private static final String GATEWAY_URL = "http://190.92.245.81/gateway.do";
    private static final String PARTNER_ID = "23011918131600340030";
    private static final String ACCESS_KEY = "23011918131600340030";
    private static final String SECRET_KEY = "4c8f89de2a90e68545b1a67167d64dd2";

    private void post(Map<String, Object> entity) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient httpClient = builder.build();

        entity.put("requestNo", genRequestNo());
        entity.put("partnerId", PARTNER_ID);
        entity.put("version", "1.0");

        String bodyString = new Gson().toJson(entity);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyString);

        String sign = sign(bodyString, SECRET_KEY);

        Request request = new Request.Builder()
                .url(GATEWAY_URL)
                .post(body)
                .addHeader("x-api-signType", "MD5")
                .addHeader("x-api-accessKey", ACCESS_KEY)
                .addHeader("x-api-sign", sign)
                .build();
        httpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("Report", "failure:" + e.getMessage(), e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("Report", "response:" + response.body().string());
                    }
                });
    }

    public static String getIMEI(Context ctx) {
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getDeviceId();
            }
        } catch (Exception e) {

        }
        return null;
    }

    private static String genRequestNo() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    private static String sign(String str, String key) {
        try {
            okio.Buffer buffer = new okio.Buffer();
            buffer.write(str.getBytes("utf-8"));
            buffer.write(key.getBytes("utf-8"));
            return buffer.md5().hex();
        } catch (Exception e) {
            return null;
        }
    }

    public VService getVService() {
        TjrBaseApi.mApiCropymeBaseUri = SpUtils.getInstance(context).getString("mApiCropymeBaseUri",TjrBaseApi.mApiCropymeBaseUri);
        if (vService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.addInterceptor(createLangInterceptor());
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(TjrBaseApi.mApiCropymeBaseUri + PROJECT_NAME_URI)
                    .build();
            vService = retrofit.create(VService.class);
        }
        else {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.addInterceptor(createLangInterceptor());
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(TjrBaseApi.mApiCropymeBaseUri + PROJECT_NAME_URI)
                    .build();
            vService = retrofit.create(VService.class);
        }
        return vService;

    }

    private Interceptor createLangInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String lang = sp.getString("myLanguage1", "");
                if ("zh-cn".equals(lang)) {//简体中文
                    lang = "cn";
                } else if ("zh-tw".equals(lang)) {//繁体中文
                    lang = "ts";
                } else if ("ko".equals(lang)) {//韩语
                    lang = "ko";
                } else if ("jp".equals(lang)) {//日语
                    lang = "ja";
                } else if ("ru".equals(lang)) {//俄语
                    lang = "ru";
                } else if ("fr".equals(lang)) {//法语
                    lang = "fr";
                } else if ("es".equals(lang)) {//西班牙语
                    lang = "es";
                } else if ("pt".equals(lang)) {//葡萄牙语
                    lang = "pt";
                } else {
                    if ("tradingview".equalsIgnoreCase(BuildConfig.FLAVOR)) {
                        lang = "ts";
                    } else {
                        lang = "en";
                    }
                }
                Request request = chain.request().newBuilder().addHeader("lang", lang).build();
                return chain.proceed(request);
            }
        };
    }

    public VService getMarketService() {
        if (marketService == null) {
            TjrBaseApi.stockHomeUriHttp = SpUtils.getInstance(TjrBaseApi.getCtx()).getString("stockHomeUriHttp",TjrBaseApi.stockHomeUriHttp);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.addInterceptor(createLangInterceptor());
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(TjrBaseApi.stockHomeUriHttp + MARKET_PROJECT_NAME_URI)
                    .build();
            marketService = retrofit.create(VService.class);
        }
        return marketService;

    }

    public synchronized void resetPredictGameService() {
        predictGameService = null;
        getPredictGameService();
    }

    public VService getPredictGameService() {
        TjrBaseApi.gamePredictHttp = SpUtils.getInstance(TjrBaseApi.getCtx()).getString("gamePredictHttp",TjrBaseApi.gamePredictHttp);
        if (predictGameService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.addInterceptor(createLangInterceptor());
            builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(TjrBaseApi.gamePredictHttp + PREDICT_PROJECT_NAME_URI)
                    .build();
            predictGameService = retrofit.create(VService.class);
        }
        return predictGameService;

    }


    public FileUploadService getFileUploadService() {
        TjrBaseApi.mApiCropymeBaseUploadFile = SpUtils.getInstance(TjrBaseApi.getCtx()).getString("mApiCropymeBaseUploadFile",TjrBaseApi.mApiCropymeBaseUploadFile);
        if (fileUploadService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.addInterceptor(createLangInterceptor());
            builder.connectTimeout(UPLOADTIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(UPLOADTIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(UPLOADTIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(TjrBaseApi.mApiCropymeBaseUploadFile + UPLOAD_PROJECT_NAME_URI)
                    .build();
            fileUploadService = retrofit.create(FileUploadService.class);

        }
        return fileUploadService;
    }


}
