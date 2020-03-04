package com.heiguo.blackfruitvip.ui.order;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.PayGoodAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.AddressBean;
import com.heiguo.blackfruitvip.bean.BuildGoodBean;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.bean.StoreBean;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.response.AddressListResponse;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.OrderBuildResponse;
import com.heiguo.blackfruitvip.ui.info.AddressActivity;
import com.heiguo.blackfruitvip.ui.user.ForgetActivity;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;
import com.heiguo.blackfruitvip.util.T;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_shop_detail)
public class ShopDetailActivity extends BaseActivity {

    private PayGoodAdapter adapter;
    private List<GoodBean> buyCarList;
    private StoreBean storeBean;
    private int serviceIndex = 0;
    private UserBean userBean;
    private AddressBean addressBean;
    private List<AddressBean> addressList;

    private int payType = Constant.PAY_TYPE_ALI;

    private AlertDialog loadingDialog;
    private AlertDialog payTypeDialog;

    private double totalPrice = 0.00;
    private double totalOldPrice = 0.00;
    private int totalCount = 0;
    private double savePrice = 0.00;
    private double needPayPrice = 0.00;
    private double balancePayPrice = 0.00;

    @ViewInject(R.id.goods_list)
    private RecyclerView listRecycleView;

    @ViewInject(R.id.name)
    private TextView nameTextView;

    @ViewInject(R.id.old_price)
    private TextView oldPriceTextView;

    @ViewInject(R.id.vip_price)
    private TextView vipPriceTextView;

    @ViewInject(R.id.price_tip)
    private TextView priceTipTextView;

    @ViewInject(R.id.price_detail)
    private TextView priceDetailTextView;

    @ViewInject(R.id.balance)
    private TextView balanceTextView;

    @ViewInject(R.id.mode_pay)
    private TextView modePayTextView;

    @ViewInject(R.id.layout_service)
    private LinearLayout layoutService;

    @ViewInject(R.id.mode_name)
    private TextView modeNameTextView;

    @ViewInject(R.id.con_phone)
    private TextView conPhoneTextView;

    @ViewInject(R.id.con_name)
    private TextView conNameTextView;

    @ViewInject(R.id.con_address)
    private TextView conAddressTextView;

    @ViewInject(R.id.group_service)
    private RadioGroup groupService;

    @ViewInject(R.id.rb_left)
    private RadioButton rbLeft;

    @ViewInject(R.id.rb_middle)
    private RadioButton rbMiddle;

    @ViewInject(R.id.rb_right)
    private RadioButton rbRight;

    @ViewInject(R.id.layout_mode)
    private LinearLayout modeLayout;

    @Event(R.id.layout_mode)
    private void changePayMode(View view) {
        payTypeDialog.show();
    }

    @Event(R.id.change_address)
    private void changeAddress(View view) {
        startAddressActivity();
    }

    @Event(R.id.go_pay)
    private void goPay(View view) {
        goToPay();
    }

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initLoadingDialog();
        initPayTypeDialog();
        getAddressList();
    }

    private void goToPay() {
        //如果余额足够支付
        if (needPayPrice <= 0) {
            RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_BUY_BALANCE);
            params.addQueryStringParameter("phone", userBean.getPhone());
            params.addQueryStringParameter("payCount", totalPrice);

            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    CommonResponse response = gson.fromJson(result, CommonResponse.class);
                    if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                        T.s("余额支付成功");
                        buildNewOrder(Constant.IS_BALANCE);
                    } else {
                        T.s("余额支付失败");
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    T.s("请求出错，请检查网络");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    loadingDialog.cancel();
                }
            });

            return;
        }

        buildNewOrder(Constant.IS_NOT_ALL_BALANCE);
    }

    private void buildNewOrder(final int isBalance) {

        //把商品信息转为string
        List<BuildGoodBean> buildGoodList = new ArrayList<>();
        for (int b = 0; b < buyCarList.size(); b++) {
            BuildGoodBean bean = new BuildGoodBean();
            bean.setId(buyCarList.get(b).getId());
            bean.setOldPrice(buyCarList.get(b).getoPrice());
            bean.setRealPrice(buyCarList.get(b).getvPrice());
            bean.setCount(buyCarList.get(b).getCount());
            buildGoodList.add(bean);
        }

        Gson gson = new Gson();
        String goodBeanListStr = gson.toJson(buildGoodList);

        loadingDialog.show();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ORDER_BUILD);
        params.setBodyContentType("application/json;charset=utf-8");
        params.addQueryStringParameter("phone", userBean.getPhone());
        params.addQueryStringParameter("isBalance", isBalance);
        params.addQueryStringParameter("storeId", storeBean.getId());
        params.addQueryStringParameter("oldPrice", totalOldPrice);
        params.addQueryStringParameter("realPrice", totalPrice);
        params.addQueryStringParameter("service", serviceIndex);
        params.addQueryStringParameter("address", addressBean.getAddress());
        params.addQueryStringParameter("addressDetail", addressBean.getDetail());
        params.addQueryStringParameter("lat", addressBean.getLatitude());
        params.addQueryStringParameter("long", addressBean.getLongitude());
        params.addQueryStringParameter("totalCount", totalCount);
        params.addQueryStringParameter("contractName", addressBean.getName());
        params.setBodyContent(goodBeanListStr);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OrderBuildResponse response = gson.fromJson(result, OrderBuildResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("创建成功");
                    startOrderDetailActivity(response.getF_data(), isBalance);
                } else {
                    System.out.println(response.getF_responseMsg());
                    T.s("创建订单失败");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                loadingDialog.cancel();
            }
        });
    }

    private void initLoadingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_loading, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        loadingDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("正在生成订单");

        loadingDialog.setCancelable(false);
    }

    private void startOrderDetailActivity(long orderId, int isBalance) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order-id", orderId);
        if (isBalance != Constant.IS_BALANCE) {
            intent.putExtra("start-mode", Constant.ORDER_DETAIL_TYPE_BUILD);
            intent.putExtra("balance", balancePayPrice);
            intent.putExtra("pay-type", payType);
        }
        startActivity(intent);
    }

    private void startAddressActivity() {
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra("mode", Constant.ADDRESS_LIST_MODE_PICKER);
        intent.putExtra("phone", userBean.getPhone());
        startActivityForResult(intent, Constant.ADDRESS_LIST_MODE_PICKER);
    }

    private void getAddressList() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ADDRESS_LIST);
        params.addQueryStringParameter("phone", userBean.getPhone());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AddressListResponse response = gson.fromJson(result, AddressListResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    addressList.clear();
                    for (int a = 0; a < response.getF_data().size(); a++) {
                        addressList.add(response.getF_data().get(a));
                    }
                    //默认取第一个地址信息
                    if (addressList.size() != 0) {
                        addressBean = addressList.get(0);
                        conPhoneTextView.setText(addressList.get(0).getPhone());
                        conNameTextView.setText(addressList.get(0).getName());
                        conAddressTextView.setText(addressList.get(0).getAddress() + " " + addressList.get(0).getDetail());
                    }
                } else {
                    T.s("获取地址列表失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        List<GoodBean> list = gson.fromJson(intent.getStringExtra("buycar"), new TypeToken<List<GoodBean>>() {
        }.getType());

        if (list.size() == 0) {
            T.s("未选购商品");
            finish();
            return;
        }
        buyCarList.clear();
        DecimalFormat df = new DecimalFormat("#.00");
        for (int l = 0; l < list.size(); l++) {
            buyCarList.add(list.get(l));
            totalPrice = totalPrice + (list.get(l).getPrice() * list.get(l).getCount());
            totalOldPrice = totalOldPrice + (list.get(l).getoPrice() * list.get(l).getCount());
            totalCount = totalCount + list.get(l).getCount();
        }

        savePrice = totalOldPrice - totalPrice;
        String priceTipStr = "共" + totalCount + "件，原价：" + df.format(totalOldPrice) + "，优惠：" + df.format(savePrice);

        vipPriceTextView.setText("" + df.format(totalPrice));
        oldPriceTextView.setText("" + df.format(totalOldPrice));
        priceTipTextView.setText(priceTipStr);
        priceDetailTextView.setText("小计：￥" + df.format(totalPrice));
        adapter.notifyDataSetChanged();

        //获取个人信息
        userBean = ((BlackFruitVipApplication) getApplication()).getUserInfo();
        needPayPrice = totalPrice - userBean.getBalance();
        if (needPayPrice <= 0) {
            needPayPrice = 0;
        }

        if (userBean.getBalance() == 0.0){
            balanceTextView.setText("￥0.00");
        }else{
            balanceTextView.setText("￥" + df.format(userBean.getBalance()));
        }

        if (needPayPrice == 0) {
            modePayTextView.setText("无需另外支付");
            modeLayout.setVisibility(View.GONE);
            balancePayPrice = totalPrice;
        } else {
            modePayTextView.setText("￥" + df.format(needPayPrice));
            balancePayPrice = userBean.getBalance();
        }

        //更新店铺信息
        storeBean = gson.fromJson(intent.getStringExtra("store"), StoreBean.class);
        serviceIndex = intent.getIntExtra("serviceIndex", 0);

        nameTextView.setText(storeBean.getName());

        rbLeft.setClickable(storeBean.getService1() == 1);
        rbMiddle.setClickable(storeBean.getService2() == 1);
        rbRight.setClickable(storeBean.getService3() == 1);

        if (storeBean.getService1() == 0) {
            rbLeft.setTextColor(getResources().getColor(R.color.colorTv));
            rbLeft.setClickable(false);

            if (serviceIndex == 0) {
                serviceIndex = 1;
            }
        } else {
            rbLeft.setTextColor(getResources().getColor(R.color.colorBlack));
            rbLeft.setClickable(true);
        }

        if (storeBean.getService2() == 0) {
            rbMiddle.setTextColor(getResources().getColor(R.color.colorTv));
            rbMiddle.setClickable(false);

            if (serviceIndex == 1) {
                if (storeBean.getService1() == 0) {
                    serviceIndex = 2;
                } else {
                    serviceIndex = 0;
                }
            }
        } else {
            rbMiddle.setTextColor(getResources().getColor(R.color.colorBlack));
            rbMiddle.setClickable(true);
        }

        if (storeBean.getService3() == 0) {
            rbRight.setTextColor(getResources().getColor(R.color.colorTv));
            rbRight.setClickable(false);
        } else {
            rbRight.setTextColor(getResources().getColor(R.color.colorBlack));
            rbRight.setClickable(true);
        }

        switch (serviceIndex) {
            case 0:
                rbLeft.setChecked(true);
                break;
            case 1:
                rbMiddle.setChecked(true);
                break;
            case 2:
                rbRight.setChecked(true);
                break;
        }

        if (storeBean.getExchange() != -1) {
            layoutService.setVisibility(View.GONE);
        }
    }

    private void initView() {
        buyCarList = new ArrayList<>();
        addressList = new ArrayList<>();
        adapter = new PayGoodAdapter(buyCarList);
        adapter.setOnItemClickListener(new PayGoodAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRecycleView.setAdapter(adapter);
        listRecycleView.setLayoutManager(layoutManager);

        oldPriceTextView.getPaint().setAntiAlias(true);
        oldPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        groupService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbLeft.isChecked()) {
                    serviceIndex = 0;
                }

                if (rbMiddle.isChecked()) {
                    serviceIndex = 1;
                }

                if (rbRight.isChecked()) {
                    serviceIndex = 2;
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ADDRESS_LIST_MODE_PICKER && resultCode == Constant.ADDRESS_LIST_MODE_PICKER) {
            Gson gson = new Gson();
            addressBean = gson.fromJson(data.getStringExtra("select-address"), AddressBean.class);
            conPhoneTextView.setText(addressBean.getPhone());
            conNameTextView.setText(addressBean.getName());
            conAddressTextView.setText(addressBean.getAddress() + " " + addressBean.getDetail());
        }
    }
}
