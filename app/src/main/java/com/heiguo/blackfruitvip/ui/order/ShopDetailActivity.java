package com.heiguo.blackfruitvip.ui.order;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.PayGoodAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_shop_detail)
public class ShopDetailActivity extends BaseActivity {

    private PayGoodAdapter adapter;

    @ViewInject(R.id.goods_list)
    private RecyclerView listRecycleView;

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


    @ViewInject(R.id.con_phone)
    private TextView conPhoneTextView;


    @ViewInject(R.id.con_name)
    private TextView conNameTextView;


    @ViewInject(R.id.con_address)
    private TextView conAddressTextView;

    @Event(R.id.layout_mode)
    private void changePayMode(View view){

    }

    @Event(R.id.go_pay)
    private void goPay(View view){

    }

    @Event(R.id.back)
    private void back(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        List<ShopBean> testList = new ArrayList<>();
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());

        adapter = new PayGoodAdapter(testList);
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
    }
}
