package com.heiguo.blackfruitvip.ui.order;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.BuyCarAdapter;
import com.heiguo.blackfruitvip.adapter.GoodBuyCarAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.CityBean;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_good_detail)
public class GoodDetailActivity extends BaseActivity {

    private List<GoodBean> allList = new ArrayList<>();
    private List<GoodBean> buycayList;

    private GoodBuyCarAdapter bAdapter;
    private int selectPosition = 0;

    @ViewInject(R.id.name)
    private TextView nameTextView;

    @ViewInject(R.id.img)
    private ImageView mainImageView;

    @ViewInject(R.id.tip)
    private TextView tipTextView;

    @ViewInject(R.id.vip_good_price)
    private TextView vgpTextView;

    @ViewInject(R.id.old_good_price)
    private TextView ogpTextView;

    @ViewInject(R.id.vip_price)
    private TextView vpTextView;

    @ViewInject(R.id.old_price)
    private TextView opTextView;

    @ViewInject(R.id.count)
    private TextView countTextView;

    @ViewInject(R.id.car_list)
    private RecyclerView buylList;

    @Event(R.id.go_pay)
    private void goPay(View view) {

    }

    @Event(R.id.back)
    private void back(View view) {
        backMenuActivity();
    }

    @Event(R.id.reduce)
    private void reduce(View view) {
        if (allList.get(selectPosition).getCount() > 0) {
            allList.get(selectPosition).setCount(allList.get(selectPosition).getCount() - 1);
            updateBuyCarAndTotal();
        }

    }

    @Event(R.id.add)
    private void add(View view) {
        allList.get(selectPosition).setCount(allList.get(selectPosition).getCount() + 1);
        updateBuyCarAndTotal();
    }

    @Event(R.id.buy_car)
    private void showBuyCar(View view) {
        if (buylList.getVisibility() == View.VISIBLE) {
            buylList.setVisibility(View.GONE);
        } else {
            buylList.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        Gson gson = new Gson();
        Intent newIntent = getIntent();
        int position = newIntent.getIntExtra("position", -1);
        allList = gson.fromJson(newIntent.getStringExtra("good-list"), new TypeToken<List<GoodBean>>() {
        }.getType());

        if (position == -1) {
            T.s("获取商品信息失败");
            finish();
            return;
        }

        selectPosition = position;

        DecimalFormat df = new DecimalFormat("#.00");
        nameTextView.setText(allList.get(position).getName());
        tipTextView.setText(allList.get(position).getSubtitle());
        vgpTextView.setText("" + df.format(allList.get(position).getPrice()));
        ogpTextView.setText("" + df.format(allList.get(position).getoPrice()));
        countTextView.setText("" + allList.get(position).getCount());
        x.image().bind(mainImageView, allList.get(position).getImage());

        updateBuyCarAndTotal();
    }

    private void initView() {
        opTextView.getPaint().setAntiAlias(true);
        opTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        buycayList = new ArrayList<>();
        bAdapter = new GoodBuyCarAdapter(this, buycayList);
        LinearLayoutManager bLayoutManager = new LinearLayoutManager(this);

        buylList.setLayoutManager(bLayoutManager);
        buylList.setAdapter(bAdapter);

        ogpTextView.getPaint().setAntiAlias(true);
        ogpTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
    }

    private void backMenuActivity() {
        Gson gson = new Gson();
        String beanString = gson.toJson(allList);

        Intent intent = new Intent();
        intent.putExtra("back-list", beanString);
        setResult(Constant.GOOD_DETAIL_BACK_REQUEST_CODE, intent);
        finish();
    }

    public void updateBuyCarAndTotal() {
        buycayList.clear();
        DecimalFormat df = new DecimalFormat("#.00");
        double allPrice = 0;
        double allOldPrice = 0;
        for (int a = 0; a < allList.size(); a++) {
            allPrice = allPrice + (allList.get(a).getPrice() * allList.get(a).getCount());
            allOldPrice = allOldPrice + (allList.get(a).getoPrice() * allList.get(a).getCount());
            if (allList.get(a).getCount() > 0) {
                buycayList.add(allList.get(a));
            }
        }

        opTextView.getPaint().setAntiAlias(true);
        opTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        bAdapter.notifyDataSetChanged();
        countTextView.setText("" + allList.get(selectPosition).getCount());

        if (buycayList.size() == 0) {
            buylList.setVisibility(View.GONE);
            vpTextView.setText("未选购商品");
            opTextView.setVisibility(View.GONE);
        } else {
            buylList.setVisibility(View.VISIBLE);
            vpTextView.setText("" + df.format(allPrice));
            opTextView.setText("" + df.format(allOldPrice));
            opTextView.setVisibility(View.VISIBLE);
        }
    }
}
