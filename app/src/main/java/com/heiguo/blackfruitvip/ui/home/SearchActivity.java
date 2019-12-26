package com.heiguo.blackfruitvip.ui.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.SearchResultAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.CityBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.bean.StoreBean;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.StoreListResponse;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    private SearchResultAdapter adapter;
    private List<StoreBean> storeList = new ArrayList<>();

    private StoreBean selectBean;

    @ViewInject(R.id.search)
    private EditText searchEditText;

    @ViewInject(R.id.list)
    private RecyclerView listRecycleView;

    @ViewInject(R.id.layout_detail)
    private LinearLayout layoutDetail;

    @ViewInject(R.id.detail_img)
    private ImageView imgDetail;

    @ViewInject(R.id.detail_name)
    private TextView nameDetail;

    @ViewInject(R.id.detail_address)
    private TextView addressDetail;

    @ViewInject(R.id.detail_tip)
    private TextView tipDetail;

    @ViewInject(R.id.group_service)
    private RadioGroup groupService;

    @ViewInject(R.id.rb_left)
    private RadioButton rbLeft;

    @ViewInject(R.id.rb_middle)
    private RadioButton rbMiddle;

    @ViewInject(R.id.rb_right)
    private RadioButton rbRight;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.detail_close)
    private void closeDetail(View view) {
        layoutDetail.setVisibility(View.GONE);
    }

    @Event(R.id.detail_map)
    private void mapDetail(View view) {
        Gson gson = new Gson();
        String selectBeanStr = gson.toJson(selectBean);

        Intent newIntent = new Intent(this, MapActivity.class);
        newIntent.putExtra("mapdata", selectBeanStr);
        startActivity(newIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //暂时测试
        getStoreList("");

        initView();
    }

    private void initView() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getStoreList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        groupService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
    }

    private void getCurrentCityBean() {
        CityBean bean = ((BlackFruitVipApplication) getApplication()).getCityPick();
        adapter.setLatLonPoint(new LatLonPoint(bean.getLatitude(), bean.getLongitude()));
    }

    private void getStoreList(String search) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_STORE_LIST);
        params.addQueryStringParameter("search", search);
        params.addQueryStringParameter("lat", 0.0);
        params.addQueryStringParameter("long", 0.0);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                StoreListResponse response = gson.fromJson(result, StoreListResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    storeList = response.getF_data();
                } else {

                }

                initList();
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

    private void initList() {
        adapter = new SearchResultAdapter(storeList);
        CityBean bean = ((BlackFruitVipApplication) getApplication()).getCityPick();
        adapter.setLatLonPoint(new LatLonPoint(bean.getLatitude(), bean.getLongitude()));
        adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                showLayoutDetail(storeList.get(position));
            }

        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRecycleView.setAdapter(adapter);
        listRecycleView.setLayoutManager(layoutManager);
    }

    private void showLayoutDetail(StoreBean bean) {
        if (bean == null) {
            T.s("未加载到店铺消息!");
            return;
        }
        x.image().bind(imgDetail, bean.getImage());
        nameDetail.setText(bean.getName());
        addressDetail.setText(bean.getAddress());
        tipDetail.setText(bean.getAnnoun());

        rbLeft.setClickable(bean.getService1() == 1);
        rbMiddle.setClickable(bean.getService2() == 1);
        rbRight.setClickable(bean.getService3() == 1);

        selectBean = bean;

        if (bean.getService1() == 0) {
            rbLeft.setTextColor(getResources().getColor(R.color.colorTv));
            rbLeft.setClickable(false);
        } else {
            rbLeft.setTextColor(getResources().getColor(R.color.colorBlack));
            rbLeft.setClickable(true);
        }

        if (bean.getService2() == 0) {
            rbMiddle.setTextColor(getResources().getColor(R.color.colorTv));
            rbMiddle.setClickable(false);
        } else {
            rbMiddle.setTextColor(getResources().getColor(R.color.colorBlack));
            rbMiddle.setClickable(true);
        }

        if (bean.getService3() == 0) {
            rbRight.setTextColor(getResources().getColor(R.color.colorTv));
            rbRight.setClickable(false);
        } else {
            rbRight.setTextColor(getResources().getColor(R.color.colorBlack));
            rbRight.setClickable(true);
        }

        layoutDetail.setVisibility(View.VISIBLE);
    }
}
