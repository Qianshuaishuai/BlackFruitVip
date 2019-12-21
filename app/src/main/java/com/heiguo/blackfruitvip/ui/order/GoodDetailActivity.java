package com.heiguo.blackfruitvip.ui.order;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_good_detail)
public class GoodDetailActivity extends BaseActivity {

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

    @Event(R.id.go_pay)
    private void goPay(View view) {

    }

    @Event(R.id.back)
    private void back(View view) {

    }

    @Event(R.id.reduce)
    private void reduce(View view) {

    }

    @Event(R.id.add)
    private void add(View view) {

    }

    @Event(R.id.buy_car)
    private void showBuyCar(View view) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        opTextView.getPaint().setAntiAlias(true);
        opTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
    }
}
