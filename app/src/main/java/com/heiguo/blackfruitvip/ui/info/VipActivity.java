package com.heiguo.blackfruitvip.ui.info;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ali.PayDetailResult;
import com.heiguo.blackfruitvip.bean.ali.PayResult;
import com.heiguo.blackfruitvip.bean.wechat.WechatConfigBean;
import com.heiguo.blackfruitvip.response.AliPayConfigResponse;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.WechatConfigResponse;
import com.heiguo.blackfruitvip.ui.order.OrderDetailActivity;
import com.heiguo.blackfruitvip.util.T;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Map;

@ContentView(R.layout.activity_vip)
public class VipActivity extends BaseActivity {

    private String phone = "";

    private int payType = Constant.PAY_TYPE_ALI;
    private AlertDialog payTypeDialog;

//    @ViewInject(R.id.header)
//    private ImageView headerImageView;
//
//    @ViewInject(R.id.phone)
//    private TextView phoneTextView;

    @ViewInject(R.id.vip_price)
    private TextView vpTextView;

    @ViewInject(R.id.old_price)
    private TextView opTextView;

    @ViewInject(R.id.iv_bg)
    private ImageView ivBg;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.detail)
    private void detail(View view) {

    }

    @Event(R.id.go_pay)
    private void goToPay(View view) {
        payTypeDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();

        if (phone == "") {
            T.s("获取登录信息失败");
            finish();
            return;
        }

//        phoneTextView.setText(phone);
    }

    private void initView() {
        opTextView.getPaint().setAntiAlias(true);
        opTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        x.image().bind(ivBg,Constant.VIP_IMAGE_URL);
        initPayTypeDialog();
    }

    private void initPayTypeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_select_pay, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        payTypeDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        Button sureButton = (Button) view.findViewById(R.id.sure);
        sureButton.setText("确认购买");
        final CheckBox aliBox = (CheckBox) view.findViewById(R.id.ali_bt);
        final CheckBox wcBox = (CheckBox) view.findViewById(R.id.wechat_bt);
        LinearLayout aliLayout = (LinearLayout) view.findViewById(R.id.layout_ali);
        LinearLayout wcLayout = (LinearLayout) view.findViewById(R.id.layout_wechat);

        if (payType == Constant.PAY_TYPE_ALI) {
            aliBox.setChecked(true);
            wcBox.setChecked(false);
        } else if (payType == Constant.PAY_TYPE_WECHAT) {
            aliBox.setChecked(false);
            wcBox.setChecked(true);
        }

        aliLayout.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (payType != Constant.PAY_TYPE_ALI) {
                    aliBox.setChecked(true);
                    wcBox.setChecked(false);
                    payType = Constant.PAY_TYPE_ALI;
                }
            }
        });

        wcLayout.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (payType != Constant.PAY_TYPE_WECHAT) {
                    aliBox.setChecked(false);
                    wcBox.setChecked(true);
                    payType = Constant.PAY_TYPE_WECHAT;
                }
            }
        });

        payTypeDialog.setCancelable(false);

        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payTypeDialog.cancel();
                goToPayVip();
            }
        });
    }

    private void goToPayVip() {
        switch (payType) {
            case Constant.PAY_TYPE_ALI:
                payAli();
                break;
            case Constant.PAY_TYPE_WECHAT:
                payWeChat();
                break;
        }
    }

    private void payWeChat() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_PAY_WECHAT_CONFIG);
        params.addQueryStringParameter("payCount", Constant.VIP_BUY_COUNT);
        params.addQueryStringParameter("storeName", Constant.VIP_BUY_NAME);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WechatConfigResponse response = gson.fromJson(result, WechatConfigResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {

                } else {
                    T.s("获取支付配置失败");
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void payAli() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_PAY_ALI_CONFIG);
        params.addQueryStringParameter("payCount", Constant.VIP_BUY_COUNT);
        params.addQueryStringParameter("storeName", Constant.VIP_BUY_NAME);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AliPayConfigResponse response = gson.fromJson(result, AliPayConfigResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    final String orderInfo = response.getF_data();
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(VipActivity.this);
                            Map<String, String> result = alipay.payV2(orderInfo, true);

                            Message msg = new Message();
                            msg.what = Constant.PAY_FOR_ORDER;
                            msg.obj = result;
                            payHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    T.s("获取支付配置失败");
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private Handler payHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                Gson gson = new Gson();
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                PayDetailResult result = gson.fromJson(resultInfo, PayDetailResult.class);

                String orderNo = result.getAlipay_trade_app_pay_response().getOut_trade_no();
                String payTotal = result.getAlipay_trade_app_pay_response().getTotal_amount();
                DoPayOrder(Constant.PAY_TYPE_ALI, orderNo, Double.parseDouble(payTotal), 0.0);
                T.s("支付成功");
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                T.s("支付失败");
            }
        }

        ;
    };

    private void DoPayOrder(int payType, String payOrder, double payCount, double balance) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_PAY);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("orderId", "");
        params.addQueryStringParameter("payType", payType);
        params.addQueryStringParameter("payOrder", payOrder);
        params.addQueryStringParameter("payCount", payCount);
        params.addQueryStringParameter("balance", balance);
        params.addQueryStringParameter("type", Constant.PAY_FOR_VIP); //支付订单固定为1
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("充值成功");
                    ((BlackFruitVipApplication)getApplication()).updateUserInfo();
                    finish();
                } else {
//                    T.s("支付失败");
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
