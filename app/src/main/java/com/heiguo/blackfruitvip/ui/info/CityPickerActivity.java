package com.heiguo.blackfruitvip.ui.info;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_city_picker)
public class CityPickerActivity extends BaseActivity {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;

    private String locationProvince = "北京";
    private String locationCity = "北京";
    private String locationCode = "101010100";

    private CityPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initLocationOption();
        startLocation();
    }

    private void initLocationOption() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                locationCity = aMapLocation.getCity();
                locationProvince = aMapLocation.getProvince();
                locationCode = aMapLocation.getCityCode();


                //定位完成之后更新数据
                picker.locateComplete(new LocatedCity(locationCity, locationProvince, locationCity), LocateState.SUCCESS);
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

    private void startLocation() {
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    private void initView() {
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

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onLocate() {

                    }
                })
                .show();
    }
}
