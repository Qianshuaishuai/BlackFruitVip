package com.heiguo.blackfruitvip.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.OrderDetailAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.OrderBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.GoodListResponse;
import com.heiguo.blackfruitvip.response.OrderDetailResponse;
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
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    private OrderDetailAdapter adapter;
    private List<GoodBean> goodList;
    private OrderBean orderBean;
    private long orderId;

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

    }

    @Event(R.id.layout_status)
    private void layoutStatus(View view) {
        //状态栏
    }

    @Event(R.id.cancel)
    private void cancel(View view) {

    }

    @Event(R.id.go_pay)
    private void goPay(View view) {
        DoPayOrder(1, "qiangeshuai", totalPrice);
    }

    @Event(R.id.layout_mode)
    private void layoutMode(View view) {

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
//        Intent intent = getIntent();
//        orderId = intent.getLongExtra("order-id",0);
//
//        if (orderId == 0){
//            T.s("订单获取失败");
//            finish();
//            return;
//        }

        orderId = 2001051355213597L;

        getOrderDetail(orderId);
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


    private void DoPayOrder(int payType, String payOrder, double payCount) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_PAY);
        params.addQueryStringParameter("phone", orderBean.getPhone());
        params.addQueryStringParameter("orderId", orderBean.getId());
        params.addQueryStringParameter("payType", payType);
        params.addQueryStringParameter("payOrder", payOrder);
        params.addQueryStringParameter("payCount", payCount);
        params.addQueryStringParameter("type", Constant.PAY_FOR_ORDER); //支付订单固定为1
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("支付成功");
                    getOrderDetail(orderId);
                } else {
                    T.s("支付失败");
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

        rpTextView.setText("实付￥" + df.format(totalPrice));
        dpTextView.setText(priceTipStr);

        rpaTextView.setText("实付￥" + df.format(totalPrice));
        dpaTextView.setText(priceTipStr);

        saTextView.setText(orderBean.getStore().getAddress());
        ctTextView.setText(orderBean.getCreateTime());
        oiTextView.setText("" + orderBean.getId());
        upTextView.setText("" + orderBean.getPhone());
        sertTextView.setText(Constant.SERVICETIPS[orderBean.getService()]);

        if (orderBean.getPayOrder() == "") {
            layoutPayId.setVisibility(View.GONE);
        } else {
            layoutPayId.setVisibility(View.VISIBLE);
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
        params.addQueryStringParameter("orderId", orderBean.getId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("取消成功");
                    getOrderDetail(orderId);
                } else {
                    T.s("取消失败");
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

    private void initView() {
        goodList = new ArrayList<>();

        adapter = new OrderDetailAdapter(goodList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        detailList.setLayoutManager(layoutManager);
        detailList.setAdapter(adapter);
    }
}
