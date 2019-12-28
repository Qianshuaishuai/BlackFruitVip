package com.heiguo.blackfruitvip.ui.info;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.AddressAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.AddressBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.response.AddressListResponse;
import com.heiguo.blackfruitvip.response.GoodListResponse;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_address)
public class AddressActivity extends BaseActivity {

    private AddressAdapter adapter;
    private List<AddressBean> addressList;

    private int mode = Constant.ADDRESS_LIST_MODE_NORMAL;

    @ViewInject(R.id.list)
    private RecyclerView listRecycleView;

    @Event(R.id.layout_add)
    private void add(View view) {
        startAddAddressActivity();
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
        mode = intent.getIntExtra("mode", Constant.ADDRESS_LIST_MODE_NORMAL);
    }

    private void getAddressList() {
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_ADDRESS_LIST);
        params.addQueryStringParameter("phone", phone);
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
                    adapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    private void initView() {
        addressList = new ArrayList<>();

        adapter = new AddressAdapter(this, addressList);
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (mode == Constant.ADDRESS_LIST_MODE_PICKER) {
                    Gson gson = new Gson();
                    Intent intent = new Intent();
                    String beanStr = gson.toJson(addressList.get(position));
                    intent.putExtra("select-address", beanStr);
                    setResult(Constant.ADDRESS_LIST_MODE_PICKER, intent);
                    finish();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRecycleView.setLayoutManager(layoutManager);
        listRecycleView.setAdapter(adapter);
    }

    private void startAddAddressActivity() {
        Intent intent = new Intent(this, AddressEditActivity.class);
        intent.putExtra("mode", Constant.ADDRESS_MODE_GO_ADD);
        startActivity(intent);
    }

    public void startEditActivity(int position, int mode) {
        Gson gson = new Gson();
        AddressBean bean = addressList.get(position);
        String beanStr = gson.toJson(bean);

        Intent intent = new Intent(this, AddressEditActivity.class);
        intent.putExtra("address", beanStr);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
}
