package com.heiguo.blackfruitvip.ui.order;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayResultActivity;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.OrderDetailAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.OrderBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.bean.ali.PayDetailResult;
import com.heiguo.blackfruitvip.bean.ali.PayResult;
import com.heiguo.blackfruitvip.response.AliPayConfigResponse;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.GoodListResponse;
import com.heiguo.blackfruitvip.response.OrderDetailResponse;
import com.heiguo.blackfruitvip.ui.home.HomeActivity;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    private OrderDetailAdapter adapter;
    private List<GoodBean> goodList;
    private OrderBean orderBean;
    private UserBean userBean;
    private long orderId;
    private double balancePayPrice;
    private int payType;
    private AlertDialog payTypeDialog;

    private int startMode = Constant.ORDER_DETAIL_TYPE_COMMON;
    private boolean firstTipPay = true;

    private double totalPrice = 0.00;
    private double totalOldPrice = 0.00;
    private int totalCount = 0;
    private double savePrice = 0.00;
    private double needPayPrice = 0.00;

    private int payTimeOut = 0;
    private int receiptTimeOut = 0;
    private Timer payTimer;
    private TimerTask payTimerTask;
    private Timer receiptTimer;
    private TimerTask receiptTimerTask;

    @ViewInject(R.id.detail_list)
    private RecyclerView detailList;

    @ViewInject(R.id.store_address)
    private TextView saTextView;

    @ViewInject(R.id.create_time)
    private TextView ctTextView;

    @ViewInject(R.id.order_id)
    private TextView oiTextView;

    @ViewInject(R.id.pay_id)
    private TextView piTextView;

    @ViewInject(R.id.service_tip)
    private TextView sertTextView;

    @ViewInject(R.id.mode_name)
    private TextView modeNameTextView;

    @ViewInject(R.id.user_phone)
    private TextView upTextView;

    @ViewInject(R.id.angel_tip)
    private TextView atTextView;

    @ViewInject(R.id.status_tip)
    private TextView stTextView;

    @ViewInject(R.id.mode_name)
    private TextView mnTextView;

    @ViewInject(R.id.detail_price)
    private TextView dpTextView;

    @ViewInject(R.id.real_price)
    private TextView rpTextView;

    @ViewInject(R.id.tip)
    private TextView tipTextView;

    @ViewInject(R.id.layout_pay_id)
    private LinearLayout layoutPayId;

    @ViewInject(R.id.detail_price_another)
    private TextView dpaTextView;

    @ViewInject(R.id.real_price_another)
    private TextView rpaTextView;

    @ViewInject(R.id.pay_layout)
    private LinearLayout payLayout;

    @ViewInject(R.id.pay_layout_another)
    private LinearLayout payLayoutAnother;

    @ViewInject(R.id.show_img)
    private ImageView showImg;

    @Event(R.id.back)
    private void back(View view) {
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Event(R.id.layout_status)
    private void layoutStatus(View view) {
        //状态栏
    }

    @Event(R.id.cancel)
    private void cancel(View view) {
        cancelOrder();
    }

    @Event(R.id.go_pay)
    private void goPay(View view) {
        if (payTimeOut < 0) {
            T.s("订单已支付超时!");
        }
        DoPayOrderBefore();
    }

    @Event(R.id.layout_mode)
    private void layoutMode(View view) {
        payTypeDialog.show();
    }

    @Event(R.id.show_layout)
    private void showLayout(View view) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        orderId = intent.getLongExtra("order-id", 0);
        startMode = intent.getIntExtra("start-mode", Constant.ORDER_DETAIL_TYPE_COMMON);
        payType = intent.getIntExtra("pay-type", Constant.PAY_TYPE_ALI);
        balancePayPrice = intent.getDoubleExtra("balance", 0.0);
        userBean = ((BlackFruitVipApplication) getApplication()).getUserInfo();

        if (orderId == 0) {
            T.s("订单获取失败");
            finish();
            return;
        }

        switch (payType) {
            case Constant.PAY_TYPE_ALI:
                modeNameTextView.setText("支付宝");
                break;
            case Constant.PAY_TYPE_WECHAT:
                modeNameTextView.setText("微信支付");
                break;
        }

        getOrderDetail(orderId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getOrderDetail(long orderId) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_DETAIL);
        params.addQueryStringParameter("id", orderId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OrderDetailResponse response = gson.fromJson(result, OrderDetailResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    orderBean = response.getF_data();
                    //创建订单开始唤醒一次支付
                    if (startMode == Constant.ORDER_DETAIL_TYPE_BUILD && firstTipPay) {
                        DoPayOrderBefore();
                        firstTipPay = false;
                    }
                    updateView();
                } else {
                    T.s("获取订单详情出错");
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
                System.out.println(ex);
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


    private void DoPayOrderBefore() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_PAY_ALI_CONFIG);
        params.addQueryStringParameter("payCount", orderBean.getRealPrice());
        params.addQueryStringParameter("storeName", orderBean.getStore().getName());
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
                            PayTask alipay = new PayTask(OrderDetailActivity.this);
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
        sureButton.setText("确认");
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
                switch (payType) {
                    case Constant.PAY_TYPE_ALI:
                        modeNameTextView.setText("支付宝");
                        break;
                    case Constant.PAY_TYPE_WECHAT:
                        modeNameTextView.setText("微信支付");
                        break;
                }
            }
        });
    }


    private void DoPayOrder(int payType, String payOrder, double payCount, double balance) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_PAY);
        params.addQueryStringParameter("phone", orderBean.getPhone());
        params.addQueryStringParameter("orderId", orderBean.getId());
        params.addQueryStringParameter("payType", payType);
        params.addQueryStringParameter("payOrder", payOrder);
        params.addQueryStringParameter("payCount", payCount);
        params.addQueryStringParameter("balance", balance);
        params.addQueryStringParameter("type", Constant.PAY_FOR_ORDER); //支付订单固定为1
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
//                    T.s("支付成功");
                    getOrderDetail(orderId);
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

    private void updateView() {
        DecimalFormat df = new DecimalFormat("#.00");

        goodList.clear();
        for (int g = 0; g < orderBean.getGoods().size(); g++) {
            goodList.add(orderBean.getGoods().get(g));
            totalCount = totalCount + orderBean.getGoods().get(g).getCount();
        }

        adapter.notifyDataSetChanged();

        totalOldPrice = orderBean.getOldPrice();
        totalPrice = orderBean.getRealPrice();
        savePrice = totalOldPrice - totalPrice;
        String priceTipStr = "共" + totalCount + "件，原价：" + df.format(totalOldPrice) + "，优惠：" + df.format(savePrice);

        rpTextView.setText("￥" + df.format(totalPrice));
        dpTextView.setText(priceTipStr);

        rpaTextView.setText("￥" + df.format(totalPrice));
        dpaTextView.setText(priceTipStr);

        saTextView.setText(orderBean.getStore().getAddress());
        ctTextView.setText(orderBean.getCreateTime());
        oiTextView.setText("" + orderBean.getId());
        upTextView.setText("" + orderBean.getContractName());
        sertTextView.setText(Constant.SERVICETIPS[orderBean.getService()]);

        if (orderBean.getPayOrder() == "") {
            layoutPayId.setVisibility(View.GONE);
        } else {
            layoutPayId.setVisibility(View.GONE);
            piTextView.setText(orderBean.getPayOrder());
        }

        switch (orderBean.getStatus()) {
            case -1:
                atTextView.setText("订单取消");
                stTextView.setText("已取消");
                tipTextView.setText("订单已被取消！");
                payLayout.setVisibility(View.GONE);
                payLayoutAnother.setVisibility(View.VISIBLE);
                break;
            case 2:
                updatePayTimeOutView();
                stTextView.setText("待支付");
                tipTextView.setText("若超时未付款，订单将自动取消");
                payLayout.setVisibility(View.VISIBLE);
                payLayoutAnother.setVisibility(View.GONE);
                break;
            case 3:
                updateReceiptTimeOutView();
                stTextView.setText("等待餐厅接单");
                tipTextView.setText("若餐厅未接单，将自动退款");
                payLayout.setVisibility(View.GONE);
                payLayoutAnother.setVisibility(View.VISIBLE);
                break;
            case 4:
                updateReceiptTimeOutView();
                stTextView.setText("配送中");
                tipTextView.setText("商家正在配送中，请耐心等待");
                payLayout.setVisibility(View.GONE);
                payLayoutAnother.setVisibility(View.VISIBLE);
                break;
            case 5:
                updateReceiptTimeOutView();
                stTextView.setText("已完成");
                tipTextView.setText("订单已完成，祝用餐愉快");
                payLayout.setVisibility(View.GONE);
                payLayoutAnother.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateReceiptTimeOutView() {
        receiptTimeOut = orderBean.getReceiptTimeOut();
        if (receiptTimeOut <= 0) {
            atTextView.setText("接单超时");
        } else {

            receiptTimer = new Timer();
            receiptTimerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int minute = receiptTimeOut / 60;
                            int second = receiptTimeOut % 60;

                            String minuteStr = "" + minute;
                            String secondStr = "" + second;

                            if (minute < 10) {
                                minuteStr = "0" + minute;
                            }

                            if (second < 10) {
                                secondStr = "0" + second;
                            }

                            String timeStr = minuteStr + ":" + secondStr;
                            atTextView.setText(timeStr);

                            receiptTimeOut--;

                            if (receiptTimeOut < 0) {
                                atTextView.setText("接单超时");
                                receiptTimer.cancel();
                            }
                        }
                    });
                }
            };
            if (payTimer != null) {
                payTimer.cancel();
            }
            receiptTimer.schedule(receiptTimerTask, 0, 1000);
        }
    }

    private void updatePayTimeOutView() {
        payTimeOut = orderBean.getPayTimeOut();
        if (payTimeOut <= 0) {
            atTextView.setText("支付超时");
        } else {

            payTimer = new Timer();
            payTimerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int minute = payTimeOut / 60;
                            int second = payTimeOut % 60;

                            String minuteStr = "" + minute;
                            String secondStr = "" + second;

                            if (minute < 10) {
                                minuteStr = "0" + minute;
                            }

                            if (second < 10) {
                                secondStr = "0" + second;
                            }

                            String timeStr = minuteStr + ":" + secondStr;
                            atTextView.setText(timeStr);

                            payTimeOut--;

                            if (payTimeOut < 0) {
                                atTextView.setText("支付超时");
                                payTimer.cancel();
                            }
                        }
                    });
                }
            };
            payTimer.schedule(payTimerTask, 0, 1000);
        }
    }

    private void cancelOrder() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_CANCEL);
        params.addQueryStringParameter("id", orderBean.getId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("取消成功");
                    if (payTimer != null) {
                        payTimer.cancel();
                    }
                    if (receiptTimer != null) {
                        receiptTimer.cancel();
                    }
                    getOrderDetail(orderId);
                } else {
                    T.s("取消失败");
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
                DoPayOrder(Constant.PAY_TYPE_ALI, orderNo, Double.parseDouble(payTotal), balancePayPrice);
                T.s("支付成功");
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                T.s("支付失败");
            }
        }

        ;
    };

    private void initView() {
        goodList = new ArrayList<>();

        adapter = new OrderDetailAdapter(goodList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        detailList.setLayoutManager(layoutManager);
        detailList.setAdapter(adapter);

        initPayTypeDialog();
    }
}
