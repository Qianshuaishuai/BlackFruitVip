package com.heiguo.blackfruitvip.ui.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.BuyCarAdapter;
import com.heiguo.blackfruitvip.adapter.ShopDetailAdapter;
import com.heiguo.blackfruitvip.adapter.ShopMenuAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.ui.user.ForgetActivity;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_menu)
public class MenuActivity extends BaseActivity {

    private ShopMenuAdapter adapter;
    private ShopDetailAdapter dAdapter;
    private BuyCarAdapter bAdapter;

    private Dialog carDialog;

    @ViewInject(R.id.menu_list)
    private RecyclerView menuList;

    @ViewInject(R.id.detail_list)
    private RecyclerView detailList;

    @ViewInject(R.id.car_list)
    private RecyclerView buylList;

    @ViewInject(R.id.price)
    private TextView priceTextView;

    @ViewInject(R.id.old_price)
    private TextView oldPriceTextView;

    @ViewInject(R.id.name)
    private TextView nameTextView;

    @Event(R.id.back)
    private void back(View view) {

    }

    @Event(R.id.go_pay)
    private void goPay(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDialog();
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
        List<ShopBean> testList = new ArrayList<>();
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        adapter = new ShopMenuAdapter(testList);
        dAdapter = new ShopDetailAdapter(testList);
        bAdapter = new BuyCarAdapter(testList);

        adapter.setOnItemClickListener(new ShopMenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                adapter.notifyDataSetChanged();
            }
        });

        dAdapter.setOnItemClickListener(new ShopDetailAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager dLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager bLayoutManager = new LinearLayoutManager(this);

        menuList.setAdapter(adapter);
        menuList.setLayoutManager(layoutManager);

        detailList.setLayoutManager(dLayoutManager);
        detailList.setAdapter(dAdapter);

        buylList.setLayoutManager(bLayoutManager);
        buylList.setAdapter(bAdapter);

    }
}
