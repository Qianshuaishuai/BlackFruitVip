package com.heiguo.blackfruitvip.ui.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.OrderDetailAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    private OrderDetailAdapter adapter;

    @ViewInject(R.id.detail_list)
    private RecyclerView detailList;

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

    @ViewInject(R.id.show_img)
    private ImageView showImg;

    @Event(R.id.back)
    private void back(View view){

    }

    @Event(R.id.layout_status)
    private void layoutStatus(View view){

    }

    @Event(R.id.cancel)
    private void cancel(View view){

    }

    @Event(R.id.go_pay)
    private void goPay(View view){

    }

    @Event(R.id.layout_mode)
    private void layoutMode(View view){

    }

    @Event(R.id.show_layout)
    private void showLayout(View view){

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
        testList.add(new ShopBean());
        testList.add(new ShopBean());

        adapter = new OrderDetailAdapter(testList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        detailList.setLayoutManager(layoutManager);
        detailList.setAdapter(adapter);
    }
}
