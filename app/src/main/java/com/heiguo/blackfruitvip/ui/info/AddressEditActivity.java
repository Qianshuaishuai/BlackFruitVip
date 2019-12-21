package com.heiguo.blackfruitvip.ui.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_address_edit)
public class AddressEditActivity extends BaseActivity {

    @ViewInject(R.id.name)
    private TextView nameTextView;
    @ViewInject(R.id.phone)
    private TextView phoneTextView;
    @ViewInject(R.id.address)
    private TextView addressTextView;
    @ViewInject(R.id.tag)
    private TextView tagTextView;
    @ViewInject(R.id.group_sex)
    private RadioGroup groupSex;
    @ViewInject(R.id.bt1_sex)
    private RadioButton bt1Sex;
    @ViewInject(R.id.bt2_sex)
    private RadioButton bt2Sex;

    @Event(R.id.layout_address)
    private void goToAddress(View view) {

    }

    @Event(R.id.back)
    private void back(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        groupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
    }
}
