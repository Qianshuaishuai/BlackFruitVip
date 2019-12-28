package com.heiguo.blackfruitvip.ui.info;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.AddressBean;
import com.heiguo.blackfruitvip.bean.CityBean;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.response.AddressListResponse;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_address_edit)
public class AddressEditActivity extends BaseActivity {

    private AddressBean addressBean;
    private int mode = Constant.ADDRESS_MODE_GO_ADD;

    private int selectSex = -1;
    private int selectTag = -1;

    @ViewInject(R.id.title)
    private TextView titleTextView;
    @ViewInject(R.id.name)
    private EditText nameTextView;
    @ViewInject(R.id.phone)
    private EditText phoneTextView;
    @ViewInject(R.id.address)
    private TextView addressTextView;
    @ViewInject(R.id.detail)
    private EditText detailTextView;
    @ViewInject(R.id.group_tag)
    private RadioGroup groupTag;
    @ViewInject(R.id.bt1_tag)
    private RadioButton bt1Tag;
    @ViewInject(R.id.bt2_tag)
    private RadioButton bt2Tag;
    @ViewInject(R.id.bt3_tag)
    private RadioButton bt3Tag;
    @ViewInject(R.id.group_sex)
    private RadioGroup groupSex;
    @ViewInject(R.id.bt1_sex)
    private RadioButton bt1Sex;
    @ViewInject(R.id.bt2_sex)
    private RadioButton bt2Sex;

    @Event(R.id.layout_address)
    private void goToAddress(View view) {
        startCityActivity();
    }

    private void startCityActivity() {
        Intent intent = new Intent(this, CityActivity.class);
        intent.putExtra("mode", Constant.CITY_MODE_FROM_ADDRESS);
        startActivityForResult(intent, Constant.CITY_MODE_FROM_ADDRESS);
    }

    @Event(R.id.confirm)
    private void confirm(View view) {
        if (phoneTextView.getText().toString().length() != 11) {
            T.s("联系人号码格式不对");
            return;
        }

        if (nameTextView.getText().toString().length() == 0) {
            T.s("姓名不能为空");
            return;
        }

        if (addressTextView.getText().toString().length() == 0) {
            T.s("请先选择地址");
            return;
        }

        if (detailTextView.getText().toString().length() == 0) {
            T.s("门牌号不能为空");
            return;
        }
        if (addressTextView.getText().toString().length() == 0) {
            T.s("请先选择地址");
            return;
        }

        if (selectSex == -1) {
            T.s("请先选择性别");
            return;
        }

        if (selectTag == -1) {
            T.s("请先选择标签");
            return;
        }

        AddressBean newBean = new AddressBean();
        newBean.setSex(addressBean.getSex());
        newBean.setTag(addressBean.getTag());
        newBean.setAddress(addressTextView.getText().toString());
        newBean.setDetail(detailTextView.getText().toString());
        newBean.setPhone(phoneTextView.getText().toString());
        newBean.setUserPhone(((BlackFruitVipApplication) getApplication()).getLoginPhone());
        newBean.setName(nameTextView.getText().toString());
        newBean.setLatitude(addressBean.getLatitude());
        newBean.setLongitude(addressBean.getLongitude());

        if (mode == Constant.ADDRESS_MODE_GO_ADD) {
            newBean.setId(-1);
        } else {
            newBean.setId(addressBean.getId());
        }
        postAddressData(newBean);
    }

    private void postAddressData(AddressBean bean) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ADDRESS_EDIT);
        params.addQueryStringParameter("userPhone", bean.getUserPhone());
        params.addQueryStringParameter("id", bean.getId());
        params.addQueryStringParameter("name", bean.getName());
        params.addQueryStringParameter("sex", bean.getSex());
        params.addQueryStringParameter("address", bean.getAddress());
        params.addQueryStringParameter("phone", bean.getPhone());
        params.addQueryStringParameter("detail", bean.getDetail());
        params.addQueryStringParameter("lat", bean.getLatitude());
        params.addQueryStringParameter("long", bean.getLongitude());
        params.addQueryStringParameter("tag", bean.getTag());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("编辑成功");
                    finish();
                } else {
                    T.s("新增或修改地址信息出错");
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

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        addressBean = new AddressBean();
        mode = intent.getIntExtra("mode", -1);

        if (mode == -1) {
            T.s("进入模式错误");
            finish();
            return;
        } else if (mode == Constant.ADDRESS_MODE_GO_ADD) {
            titleTextView.setText("新增收货地址");
        } else if (mode == Constant.ADDRESS_MODE_GO_EDIT) {
            Gson gson = new Gson();
            addressBean = gson.fromJson(intent.getStringExtra("address"), AddressBean.class);
            titleTextView.setText("修改收货地址");
            nameTextView.setText(addressBean.getName());
            phoneTextView.setText(addressBean.getPhone());
            addressTextView.setText(addressBean.getAddress());
            detailTextView.setText(addressBean.getDetail());

            switch (addressBean.getSex()) {
                case 0:
                    bt1Sex.setChecked(true);
                    bt2Sex.setChecked(false);
                    break;
                case 1:
                    bt1Sex.setChecked(false);
                    bt2Sex.setChecked(true);
                    break;
            }

            switch (addressBean.getTag()) {
                case 0:
                    bt1Tag.setChecked(true);
                    bt2Tag.setChecked(false);
                    bt3Tag.setChecked(false);
                    break;
                case 1:
                    bt1Tag.setChecked(false);
                    bt2Tag.setChecked(true);
                    bt3Tag.setChecked(false);
                    break;
                case 2:
                    bt1Tag.setChecked(false);
                    bt2Tag.setChecked(false);
                    bt3Tag.setChecked(true);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CITY_MODE_FROM_ADDRESS && resultCode == Constant.CITY_MODE_FROM_ADDRESS) {
            Gson gson = new Gson();
            CityBean bean = gson.fromJson(data.getStringExtra("city"), CityBean.class);
            addressTextView.setText(bean.getAddress());
            addressBean.setLatitude(bean.getLatitude());
            addressBean.setLongitude(bean.getLongitude());
        }
    }

    private void initView() {
        groupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (bt1Sex.isChecked()) {
                    selectSex = 0;
                }

                if (bt2Sex.isChecked()) {
                    selectSex = 1;
                }
                addressBean.setSex(selectSex);
            }
        });

        groupTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (bt1Tag.isChecked()) {
                    selectTag = 0;
                }

                if (bt2Tag.isChecked()) {
                    selectTag = 1;
                }

                if (bt3Tag.isChecked()) {
                    selectTag = 2;
                }
                addressBean.setTag(selectTag);
            }
        });
    }
}
