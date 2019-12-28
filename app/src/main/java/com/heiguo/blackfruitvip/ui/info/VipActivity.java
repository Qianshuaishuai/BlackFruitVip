package com.heiguo.blackfruitvip.ui.info;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.util.T;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_vip)
public class VipActivity extends BaseActivity {

    private String phone = "";

    @ViewInject(R.id.header)
    private ImageView headerImageView;

    @ViewInject(R.id.phone)
    private TextView phoneTextView;

    @ViewInject(R.id.vip_price)
    private TextView vpTextView;

    @ViewInject(R.id.old_price)
    private TextView opTextView;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.detail)
    private void detail(View view) {

    }

    @Event(R.id.go_pay)
    private void goToPay(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();

        if (phone==""){
            T.s("获取登录信息失败");
            finish();
            return;
        }
    }

    private void initView() {
        opTextView.getPaint().setAntiAlias(true);
        opTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
    }
}
