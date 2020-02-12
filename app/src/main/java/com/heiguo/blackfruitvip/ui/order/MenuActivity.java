package com.heiguo.blackfruitvip.ui.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.BuyCarAdapter;
import com.heiguo.blackfruitvip.adapter.ShopDetailAdapter;
import com.heiguo.blackfruitvip.adapter.ShopMenuAdapter;
import com.heiguo.blackfruitvip.adapter.ShopMenuAnoAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.StoreBean;
import com.heiguo.blackfruitvip.bean.TypeBean;
import com.heiguo.blackfruitvip.linklayout.base.BaseScrollableContainer;
import com.heiguo.blackfruitvip.linklayout.content.RecyclerViewContentContainer;
import com.heiguo.blackfruitvip.linklayout.tab.ListViewTabContainer;
import com.heiguo.blackfruitvip.linklayout.ui.LinkedLayout;
import com.heiguo.blackfruitvip.linklayout.widget.RealSectionIndexer;
import com.heiguo.blackfruitvip.linklayout.widget.SimpleArrayAdapter;
import com.heiguo.blackfruitvip.response.GoodListResponse;
import com.heiguo.blackfruitvip.ui.info.VipActivity;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ContentView(R.layout.activity_menu)
public class MenuActivity extends BaseActivity {

//    private ShopMenuAdapter adapter;
    private ShopMenuAnoAdapter adapter;
    private ShopDetailAdapter dAdapter;
    private BuyCarAdapter bAdapter;

    private StoreBean storeBean;
    private int serviceIndex = 0;

    private AlertDialog noVipDialog;
    private AlertDialog noLoginDialog;

    private List<GoodBean> allList;
    private List<GoodBean> currentList;
    private List<GoodBean> buycayList;
    private List<TypeBean> typeList;

    private int selectPoistion = 0;

    private Dialog carDialog;

    private BaseScrollableContainer mTabContainer;      // 左边的 Tab 页
    private BaseScrollableContainer mContentContainer;  // 右边的 content 页

    List<Integer> mData = Stream.iterate(0, item -> item+1)
            .limit(10)
            .collect(Collectors.toList());

    private SectionIndexer mSectionIndexer = new RealSectionIndexer(mData);

//    @ViewInject(R.id.menu_list)
//    private RecyclerView menuList;
//
//    @ViewInject(R.id.detail_list)
//    private RecyclerView detailList;
//
    @ViewInject(R.id.layout_main)
    private LinkedLayout mainLayout;

    @ViewInject(R.id.car_list)
    private RecyclerView buylList;

    @ViewInject(R.id.vip_price)
    private TextView vpriceTextView;

    @ViewInject(R.id.old_price)
    private TextView oldPriceTextView;

    @ViewInject(R.id.name)
    private TextView nameTextView;

    @ViewInject(R.id.total_count)
    private TextView totalCountTextView;

    @Event(R.id.bottom)
    private void bottomClick(View view) {
        if (buylList.getVisibility() == View.VISIBLE) {
            buylList.setVisibility(View.GONE);
        } else if (buylList.getVisibility() == View.GONE) {
            buylList.setVisibility(View.VISIBLE);
        }
    }

    @Event(R.id.back)
    private void back(View view) {
        if (buycayList.size() != 0) {
            carDialog.show();
        } else {
            finish();
        }
    }

    @Event(R.id.go_pay)
    private void goPay(View view) {

        if (buycayList.size() <= 0) {
            T.s("请先选择商品！");
            return;
        }

        Gson gson = new Gson();
        Intent intent = new Intent(this, ShopDetailActivity.class);
        String beanStr = gson.toJson(buycayList);
        String storeBeanStr = gson.toJson(storeBean);
        intent.putExtra("buycar", beanStr);
        intent.putExtra("store", storeBeanStr);
        intent.putExtra("serviceIndex", serviceIndex);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDialog();
        initData();
        initNoVipDialog();
        initNoLoginDialog();
    }

    private void initNoVipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_user_common, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noVipDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("你当前还不是黑果会员，不能选择购买商品，是否前去购买？");
        Button leftButton = (Button) view.findViewById(R.id.left);
        Button rightButton = (Button) view.findViewById(R.id.right);

        leftButton.setText("返回退出");
        rightButton.setText("前往购买");

        leftButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noVipDialog.cancel();
            }
        });

        rightButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noVipDialog.cancel();
                startVipActivity();
            }
        });

        noVipDialog.setCancelable(false);
    }


    private void startVipActivity() {
        Intent intent = new Intent(this, VipActivity.class);
        startActivity(intent);
    }

    private void initNoLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_user_common, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noLoginDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("你还未登录，是否前去登录？");
        Button leftButton = (Button) view.findViewById(R.id.left);
        Button rightButton = (Button) view.findViewById(R.id.right);

        leftButton.setText("继续浏览");
        rightButton.setText("前往登录");

        leftButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noLoginDialog.cancel();
            }
        });

        rightButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noLoginDialog.cancel();
                startLoginActivity();
            }
        });

        noLoginDialog.setCancelable(false);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean checkIsLogin() {
        String phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();
        if (phone == "") {
            noLoginDialog.show();
            return false;
        }

        return true;
    }

    public boolean checkIsVip() {
        if (!((BlackFruitVipApplication) getApplication()).isCurrentVip()) {
            noVipDialog.show();
            return false;
        }

        return true;
    }

    private void initData() {
        Intent newIntent = getIntent();
        String beanStr = newIntent.getStringExtra("store");
        serviceIndex = newIntent.getIntExtra("serviceIndex", 0);
        if (beanStr != null) {
            Gson gson = new Gson();
            storeBean = gson.fromJson(beanStr, StoreBean.class);
            nameTextView.setText(storeBean.getName());
            RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_GOOD_LIST);
            params.addQueryStringParameter("store_id", storeBean.getId());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    GoodListResponse response = gson.fromJson(result, GoodListResponse.class);
                    if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                        allList = response.getF_data().getGoods();

                        typeList.clear();
                        for (int t = 0; t < response.getF_data().getStoreTypes().size(); t++) {
                            typeList.add(response.getF_data().getStoreTypes().get(t));
                        }
                        isGoToGoodDetail();
                        updateList();
                    } else {
                        T.s("获取商品列表失败");
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
        } else {
            T.s("获取商品列表失败");
            finish();
        }
    }

    private void isGoToGoodDetail() {
        Intent intent = getIntent();
        int mode = intent.getIntExtra("store-mode", Constant.GOOD_DETAIL_NORMAL);
        int goodId = intent.getIntExtra("good", 0);

        if (mode == Constant.GOOD_DETAIL_JUMP_TYPE) {
            int position = getGoodPositionFromAllList(goodId);
            if (position != -1) {
                startGoodDetailActivityForAno(position);
            }
        }
    }

    private int getGoodPositionFromAllList(int goodId) {
        for (int a = 0; a < allList.size(); a++) {
            if (allList.get(a).getId() == goodId) {
                return a;
            }
        }

        return -1;
    }

    private void updateList() {
        adapter.setSelectPosition(selectPoistion);
        adapter.notifyDataSetChanged();
        selectGoodList(selectPoistion);

        initTabContainer();
        initContentContainer();
        initLinkedLayout();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_user_common, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        carDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("离开当前页面，清空购物车，是否继续？");
        Button leftButton = (Button) view.findViewById(R.id.left);
        Button rightButton = (Button) view.findViewById(R.id.right);

        leftButton.setText("取消");
        rightButton.setText("确定");

        leftButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                carDialog.cancel();
            }
        });

        rightButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                carDialog.cancel();
                finish();
            }
        });

        carDialog.setCancelable(false);
    }

    private void initView() {
        typeList = new ArrayList<>();
        currentList = new ArrayList<>();
        buycayList = new ArrayList<>();

        storeBean = new StoreBean();

        adapter = new ShopMenuAnoAdapter(this, typeList, new ShopMenuAnoAdapter.ShopMenuClickListener() {
            @Override
            public void clickListener(View v) {

            }
        });
        dAdapter = new ShopDetailAdapter(this, currentList);
        bAdapter = new BuyCarAdapter(this, buycayList);

//        adapter.setOnItemClickListener(new ShopMenuAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                selectPoistion = position;
//                selectGoodList(position);
//            }
//        });

        dAdapter.setOnItemClickListener(new ShopDetailAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startGoodDetailActivity(position);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager dLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager bLayoutManager = new LinearLayoutManager(this);
//
//        menuList.setAdapter(adapter);
//        menuList.setLayoutManager(layoutManager);
//
//        detailList.setLayoutManager(dLayoutManager);
//        detailList.setAdapter(dAdapter);

        buylList.setLayoutManager(bLayoutManager);
        buylList.setAdapter(bAdapter);

        vpriceTextView.setText("未选购商品");
        oldPriceTextView.setVisibility(View.GONE);
    }

    private void startGoodDetailActivity(int position) {
        Gson gson = new Gson();
        String beanString = gson.toJson(allList);

        Intent newIntent = new Intent(this, GoodDetailActivity.class);
        newIntent.putExtra("good-list", beanString);
        newIntent.putExtra("position", allListPosition(position));
        newIntent.putExtra("store", gson.toJson(storeBean));
        newIntent.putExtra("serviceIndex", serviceIndex);
        startActivityForResult(newIntent, Constant.GOOD_DETAIL_BACK_REQUEST_CODE);
    }

    private void startGoodDetailActivityForAno(int position) {
        Gson gson = new Gson();
        String beanString = gson.toJson(allList);

        Intent newIntent = new Intent(this, GoodDetailActivity.class);
        newIntent.putExtra("good-list", beanString);
        newIntent.putExtra("position", position);
        newIntent.putExtra("store", gson.toJson(storeBean));
        newIntent.putExtra("serviceIndex", serviceIndex);
        startActivityForResult(newIntent, Constant.GOOD_DETAIL_BACK_REQUEST_CODE);
    }


    private int allListPosition(int currnetPosition) {
        int id = currentList.get(currnetPosition).getId();
        int newPosition = -1;
        for (int a = 0; a < allList.size(); a++) {
            if (allList.get(a).getId() == id) {
                newPosition = a;
            }
        }

        return newPosition;
    }

    private void selectGoodList(int position) {
        int type = typeList.get(position).getId();

        currentList.clear();
        for (int a = 0; a < allList.size(); a++) {
            if (allList.get(a).getType() == type) {
                currentList.add(allList.get(a));
            }
        }

        adapter.notifyDataSetChanged();
        dAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GOOD_DETAIL_BACK_REQUEST_CODE && resultCode == Constant.GOOD_DETAIL_BACK_REQUEST_CODE) {
            Gson gson = new Gson();
            allList = gson.fromJson(data.getStringExtra("back-list"), new TypeToken<List<GoodBean>>() {
            }.getType());

            updateList();
            updateBuyCarAndTotal();
        }
    }

    public void updateBuyCarAndTotal() {
        buycayList.clear();
        DecimalFormat df = new DecimalFormat("#.00");
        double allPrice = 0;
        double allOldPrice = 0;
        int totalCount = 0;
        for (int a = 0; a < allList.size(); a++) {
            allPrice = allPrice + (allList.get(a).getPrice() * allList.get(a).getCount());
            allOldPrice = allOldPrice + (allList.get(a).getoPrice() * allList.get(a).getCount());
            if (allList.get(a).getCount() > 0) {
                buycayList.add(allList.get(a));
            }
        }

        for (int b = 0; b < buycayList.size(); b++) {
            totalCount = totalCount + buycayList.get(b).getCount();
        }

        oldPriceTextView.getPaint().setAntiAlias(true);
        oldPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        totalCountTextView.setText("共" + totalCount + "件商品");

        bAdapter.notifyDataSetChanged();
        dAdapter.notifyDataSetChanged();

        if (buycayList.size() == 0) {
//            buylList.setVisibility(View.GONE);
            vpriceTextView.setText("未选购商品");
            oldPriceTextView.setVisibility(View.GONE);
        } else {
//            buylList.setVisibility(View.VISIBLE);
            vpriceTextView.setText("" + df.format(allPrice));
            oldPriceTextView.setText("" + df.format(allOldPrice));
            oldPriceTextView.setVisibility(View.VISIBLE);
        }
    }


    //特殊布局
    private void initTabContainer() {
        ListView mListView = new ListView(this);
        mListView.setAdapter(adapter);

        mTabContainer = new ListViewTabContainer(this, mListView);
    }

    private void initContentContainer() {
        RecyclerView mRecyclerView = new RecyclerView(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SimpleArrayAdapter<>(this, mData, mSectionIndexer));

        mContentContainer = new RecyclerViewContentContainer(this, mRecyclerView);
    }

    private void initLinkedLayout() {
        mainLayout.setContainers(mTabContainer, mContentContainer);
        mainLayout.setSectionIndexer(mSectionIndexer);
    }
}
