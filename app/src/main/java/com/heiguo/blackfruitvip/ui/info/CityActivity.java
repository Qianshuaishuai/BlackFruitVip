package com.heiguo.blackfruitvip.ui.info;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.HistoryCityAdapter;
import com.heiguo.blackfruitvip.adapter.SearchMapAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.CityBean;
import com.heiguo.blackfruitvip.bean.DistanceBean;
import com.heiguo.blackfruitvip.util.T;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@ContentView(R.layout.activity_city)
public class CityActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private HistoryCityAdapter adapter;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;

    private SearchMapAdapter searchMapAdapter;
    private List<PoiItem> searchMapList = new ArrayList<>();

    private List<CityBean> hList;
    private AMapLocation location;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;

    private String locationProvince = "北京";
    private String locationCity = "北京";
    private String locationCode = "101010100";
    private LatLonPoint locationPoint = new LatLonPoint(0.0, 0.0);

    private String selectProvince = "北京";
    private String selectCity = "北京";
    private String selectCode = "101010100";

    private int mode = Constant.CITY_MODE_FROM_MAIN;

    private CityPicker picker;

    @ViewInject(R.id.history)
    private GridView historyList;

    @ViewInject(R.id.search_list)
    private RecyclerView searchList;

    @ViewInject(R.id.search)
    private EditText search;

    @ViewInject(R.id.search_layout)
    private LinearLayout layoutSearch;

    @ViewInject(R.id.sv)
    private ScrollView sv;

    @ViewInject(R.id.current_city)
    private TextView currentCity;

    @ViewInject(R.id.current_location)
    private TextView currentLocation;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.current_city)
    private void showCityPicker(View view) {
        picker.show();
    }

    @Event(R.id.re_location)
    private void reLocation(View view) {
        startLocation();
    }

    @Event(R.id.current_location)
    private void location(View view) {
        if (currentLocation.getText().toString().equals("定位中")) {
            T.s("当前定位有误，稍候重试!");
            return;
        }
        CityBean bean = new CityBean();
        bean.setAddress(location.getAddress());
        bean.setCity(location.getCity());
        bean.setProvince(location.getProvince());
        bean.setCode(location.getCityCode());
        bean.setLatitude(location.getLatitude());
        bean.setLongitude(location.getLongitude());
        if (mode == Constant.CITY_MODE_FROM_MAIN) {
            ((BlackFruitVipApplication) getApplication()).saveCityPick(bean);
        } else {
            Gson gson = new Gson();
            Intent intent = new Intent();
            intent.putExtra("city", gson.toJson(bean));
            setResult(Constant.CITY_MODE_FROM_ADDRESS, intent);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initCityPicker();
        initLocationOption();
        startLocation();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", Constant.CITY_MODE_FROM_MAIN);
    }

    private void initCityPicker() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100")); //code为城市代码
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));

        picker = CityPicker.from(this);

        picker//activity或者fragment
                .enableAnimation(true)    //启用动画效果，默认无
//                .setAnimationStyle(anim)	//自定义动画
//                .setLocatedCity(new LocatedCity(locationCity, locationProvince, locationCode))  //APP自身已定位的城市，传null会自动定位（默认）
                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        selectCity = data.getName();
                        selectProvince = data.getProvince();
                        selectCode = data.getCode();
                        currentCity.setText(data.getName());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onLocate() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                picker.locateComplete(new LocatedCity(locationCity, locationProvince, locationCode), LocateState.SUCCESS);
                            }
                        }, 1000);
                    }
                });
    }

    private void startLocation() {
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    private void initLocationOption() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                locationCity = aMapLocation.getCity();
                locationProvince = aMapLocation.getProvince();
                locationCode = aMapLocation.getCityCode();
                selectCity = aMapLocation.getCity();
                selectProvince = aMapLocation.getProvince();
                selectCode = aMapLocation.getCityCode();
                currentCity.setText(locationCity);
                locationPoint.setLatitude(aMapLocation.getLatitude());
                locationPoint.setLongitude(aMapLocation.getLongitude());
                searchMapAdapter.setLatLonPoint(locationPoint);
                searchMapAdapter.notifyDataSetChanged();

                String simpleAddress = aMapLocation.getAddress().replace(aMapLocation.getProvince(), "").replace(aMapLocation.getCity(), "");
                currentLocation.setText(aMapLocation.getPoiName());

                location = aMapLocation;
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setOnceLocation(true);
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    private void initView() {
        hList = ((BlackFruitVipApplication) getApplication()).getHistoryCityList();
        if (hList == null) {
            hList = new ArrayList<>();
        }
        adapter = new HistoryCityAdapter(this, hList, new HistoryCityAdapter.HistoryCityListener() {
            @Override
            public void clickListener(View v) {
                HistoryCityAdapter.ViewHolder holder = (HistoryCityAdapter.ViewHolder) v.getTag();
                int index = (int) holder.nameTv.getTag();
                if (mode == Constant.CITY_MODE_FROM_MAIN) {
                    ((BlackFruitVipApplication) getApplication()).saveCityPick(hList.get(index));
                } else {
                    Gson gson = new Gson();
                    Intent intent = new Intent();
                    intent.putExtra("city", gson.toJson(hList.get(index)));
                    setResult(Constant.CITY_MODE_FROM_ADDRESS, intent);
                }
                finish();
            }
        });

        historyList.setAdapter(adapter);

        //监听搜索框
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    sv.setVisibility(View.GONE);
                } else {
                    String searchTxt = charSequence.toString();
                    PoiSearch(searchTxt);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //显示搜索列表
        searchMapAdapter = new SearchMapAdapter(searchMapList);
        searchMapAdapter.setOnItemClickListener(new SearchMapAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                PoiItem item = searchMapList.get(position);
                //存储
                CityBean bean = new CityBean();
                bean.setAddress(item.getTitle());
                bean.setCity(item.getCityName());
                bean.setProvince(item.getProvinceName());
                bean.setCode(item.getCityCode());
                bean.setLatitude(item.getLatLonPoint().getLatitude());
                bean.setLongitude(item.getLatLonPoint().getLongitude());

                if (mode == Constant.CITY_MODE_FROM_MAIN) {
                    ((BlackFruitVipApplication) getApplication()).saveCityPick(bean);
                } else {
                    Gson gson = new Gson();
                    Intent intent = new Intent();
                    intent.putExtra("city", gson.toJson(bean));
                    setResult(Constant.CITY_MODE_FROM_ADDRESS, intent);
                }

                //更新本地历史记录
                addNewHistoryList(bean);
                finish();

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchList.setLayoutManager(layoutManager);
        searchList.setAdapter(searchMapAdapter);
    }


    private void addNewHistoryList(CityBean bean) {
        boolean isSaved = false;
        if (hList.size() != 0) {
            for (int h = 0; h < hList.size(); h++) {
                if (hList.get(h).getAddress().equals(bean.getAddress())) {
                    isSaved = true;
                }
            }
        }

        if (isSaved == false) {
            hList.add(bean);
            ((BlackFruitVipApplication) getApplication()).setHistoryCityList(hList);
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {
            searchMapList.clear();
            List<DistanceBean> sortPoiList = SortPoiList(poiResult.getPois(), locationPoint);

            for (int s = 0; s < sortPoiList.size(); s++) {
                searchMapList.add(sortPoiList.get(s).getItem());
            }

            searchMapAdapter.notifyDataSetChanged();
            if (searchMapList.size() == 0 || search.getText().toString().length() == 0) {
                sv.setVisibility(View.GONE);
            } else {
                sv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void PoiSearch(String searchTxt) {
        query = new PoiSearch.Query(searchTxt, "", selectCity);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    private List<DistanceBean> SortPoiList(List<PoiItem> list, LatLonPoint locationPoint) {
        List<DistanceBean> newList = new ArrayList<>();
        for (int l = 0; l < list.size(); l++) {
            LatLonPoint currentPoint = list.get(l).getLatLonPoint();
            double distance = AMapUtils.calculateLineDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()), new LatLng(locationPoint.getLatitude(), locationPoint.getLongitude()));
            DistanceBean bean = new DistanceBean();
            bean.setItem(list.get(l));
            bean.setDistance(distance);
            newList.add(bean);
        }

        Collections.sort(newList, new Comparator<DistanceBean>() {
            @Override
            public int compare(DistanceBean distanceBean, DistanceBean t1) {
                return (int) (distanceBean.getDistance() - t1.getDistance());
            }
        });

        return newList;
    }
}
