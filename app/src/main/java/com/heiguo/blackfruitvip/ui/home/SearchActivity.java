package com.heiguo.blackfruitvip.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
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
import com.heiguo.blackfruitvip.ui.order.MenuActivity;
import com.heiguo.blackfruitvip.util.MapUtil;
import com.heiguo.blackfruitvip.util.T;
import com.heiguo.blackfruitvip.util.TimeUtil;

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
    private CityBean cityBean;
    private List<StoreBean> storeList = new ArrayList<>();

    private StoreBean selectBean;
    private int serviceIndex = 0;

    private AlertDialog mapSelectMap;
    private int mapSelect = 0;

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
//        Gson gson = new Gson();
//        String selectBeanStr = gson.toJson(selectBean);
//
//        Intent newIntent = new Intent(this, MapActivity.class);
//        newIntent.putExtra("mapdata", selectBeanStr);
//        startActivity(newIntent);
        mapSelectMap.show();
    }

    @Event(R.id.go_in)
    private void goIn(View view) {
        String time1Start = selectBean.getTime1Start();
        String time1End = selectBean.getTime1End();

        String[] time1Starts = time1Start.split(":");
        String[] time1Ends = time1End.split(":");
        int startHour = 0;
        int startMin = 0;
        int endHour = 0;
        int endMin = 0;
        if (time1Starts.length != 0) {
            startHour = Integer.parseInt(time1Starts[0]);
            startMin = Integer.parseInt(time1Starts[1]);
        }

        if (time1Ends.length != 0) {
            endHour = Integer.parseInt(time1Ends[0]);
            endMin = Integer.parseInt(time1Ends[1]);
        }

        LatLonPoint currentPoint = new LatLonPoint(selectBean.getLatitude(), selectBean.getLongitude());
        double distance = AMapUtils.calculateLineDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()), new LatLng(cityBean.getLatitude(), cityBean.getLongitude()));
        if (selectBean.getMaxDistance() < distance) {
            T.s("超出配送范围");
            return;
        }

        if (!TimeUtil.isCurrentInTimeScope(startHour, startMin, endHour, endMin)) {
            T.s("不在配送时间内");
            return;
        }
        startMenuActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //暂时测试
        getCurrentCityBean();

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int storeType = intent.getIntExtra("storeType", 0);

        if (storeType == 0) {
            getStoreList("");
        } else {
            getStoreListForType(storeType);
        }
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
                if (rbLeft.isChecked()) {
                    serviceIndex = 0;
                }

                if (rbMiddle.isChecked()) {
                    serviceIndex = 1;
                }

                if (rbRight.isChecked()) {
                    serviceIndex = 2;
                }
            }
        });

        initMapDialog();
    }

    private void startMenuActivity() {
        Gson gson = new Gson();
        String selectBeanStr = gson.toJson(selectBean);

        Intent newIntent = new Intent(this, MenuActivity.class);
        newIntent.putExtra("store", selectBeanStr);
        newIntent.putExtra("serviceIndex", serviceIndex);
        startActivity(newIntent);
    }

    private void getCurrentCityBean() {
        cityBean = ((BlackFruitVipApplication) getApplication()).getCityPick();
//        adapter.setLatLonPoint(new LatLonPoint(cityBean.getLatitude(), cityBean.getLongitude()));
    }

    private void getStoreListForType(int storeType) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_STORE_TYPE);
        params.addQueryStringParameter("storeType", storeType);
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

    private void getStoreList(String search) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_STORE_LIST);
        params.addQueryStringParameter("search", search);
        params.addQueryStringParameter("lat", cityBean.getLatitude());
        params.addQueryStringParameter("long", cityBean.getLongitude());
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
        adapter.setLatLonPoint(new LatLonPoint(cityBean.getLatitude(), cityBean.getLongitude()));
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

        if (bean.getExchange() == -1){
            groupService.setVisibility(View.GONE);
        }

        if (bean.getService1() == 0) {
            rbLeft.setTextColor(getResources().getColor(R.color.colorTv));
            rbLeft.setClickable(false);

            if (serviceIndex == 0) {
                serviceIndex = 1;
            }
        } else {
            rbLeft.setTextColor(getResources().getColor(R.color.colorBlack));
            rbLeft.setClickable(true);
        }

        if (bean.getService2() == 0) {
            rbMiddle.setTextColor(getResources().getColor(R.color.colorTv));
            rbMiddle.setClickable(false);

            if (serviceIndex == 1) {
                if (bean.getService1() == 0) {
                    serviceIndex = 2;
                } else {
                    serviceIndex = 0;
                }
            }
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

    // 百度地图
    public void toBaidu(StoreBean bean) {

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + bean.getLatitude() + "," + bean.getLongitude()));
        startActivity(intent);
    }

    // 腾讯地图
    public void toTencent(StoreBean bean) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + bean.getLatitude() + "," + bean.getLongitude() + "&policy=0&referer=appName"));
        startActivity(intent);

    }

    // 高德地图
    public void toGaodeNavi(StoreBean bean) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + bean.getLatitude() + "&dlon=" + bean.getLongitude() + "&dname=目的地&dev=0&t=2"));
        startActivity(intent);
    }

    private void initMapDialog() {
        final List mList = MapUtil.hasMap(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_map_select, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        mapSelectMap = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        Button sureButton = (Button) view.findViewById(R.id.sure);
        sureButton.setText("确认");
        final TextView noTip = (TextView) view.findViewById(R.id.no_tip);
        final CheckBox baiduBox = (CheckBox) view.findViewById(R.id.baidu_bt);
        final CheckBox gaodeBox = (CheckBox) view.findViewById(R.id.gaode_bt);
        final CheckBox tengxunBox = (CheckBox) view.findViewById(R.id.tengxun_bt);
        LinearLayout baiduLayout = (LinearLayout) view.findViewById(R.id.layout_baidu);
        LinearLayout gaodeLayout = (LinearLayout) view.findViewById(R.id.layout_gaode);
        LinearLayout tengxunLayout = (LinearLayout) view.findViewById(R.id.layout_tengxun);

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).equals("com.autonavi.minimap")) {
                gaodeLayout.setVisibility(View.VISIBLE);
            } else if (mList.get(i).equals("com.baidu.BaiduMap")) {
                baiduLayout.setVisibility(View.VISIBLE);
            } else if (mList.get(i).equals("com.tencent.map")) {
                tengxunLayout.setVisibility(View.VISIBLE);
            }
        }

        if (mList.size() == 0) {
            noTip.setVisibility(View.VISIBLE);
            sureButton.setText("取消");
        }

        if (mapSelect == Constant.MAP_TYPE_BAIDU) {
            baiduBox.setChecked(true);
            gaodeBox.setChecked(false);
            tengxunBox.setChecked(false);
        } else if (mapSelect == Constant.MAP_TYPE_GAODE) {
            baiduBox.setChecked(false);
            gaodeBox.setChecked(true);
            tengxunBox.setChecked(false);
        } else if (mapSelect == Constant.MAP_TYPE_TENGXUN) {
            baiduBox.setChecked(false);
            gaodeBox.setChecked(false);
            tengxunBox.setChecked(true);
        }

        baiduLayout.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mapSelect != Constant.MAP_TYPE_BAIDU) {
                    baiduBox.setChecked(true);
                    gaodeBox.setChecked(false);
                    tengxunBox.setChecked(false);
                    mapSelect = Constant.MAP_TYPE_BAIDU;
                }
            }
        });

        gaodeLayout.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mapSelect != Constant.MAP_TYPE_GAODE) {
                    baiduBox.setChecked(false);
                    gaodeBox.setChecked(true);
                    tengxunBox.setChecked(false);
                    mapSelect = Constant.MAP_TYPE_GAODE;
                }
            }
        });

        tengxunLayout.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mapSelect != Constant.MAP_TYPE_TENGXUN) {
                    baiduBox.setChecked(false);
                    gaodeBox.setChecked(false);
                    tengxunBox.setChecked(true);
                    mapSelect = Constant.MAP_TYPE_TENGXUN;
                }
            }
        });

        mapSelectMap.setCancelable(false);

        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapSelectMap.cancel();
                if (mList.size() == 0) {
                    return;
                }
                switch (mapSelect) {
                    case Constant.MAP_TYPE_BAIDU:
                        toBaidu(selectBean);
                        break;
                    case Constant.MAP_TYPE_GAODE:
                        toGaodeNavi(selectBean);
                        break;
                    case Constant.MAP_TYPE_TENGXUN:
                        toTencent(selectBean);
                        break;
                }
            }
        });
    }

}
