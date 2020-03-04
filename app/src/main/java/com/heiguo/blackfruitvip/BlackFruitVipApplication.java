package com.heiguo.blackfruitvip;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heiguo.blackfruitvip.bean.CityBean;
import com.heiguo.blackfruitvip.bean.LocationBean;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.bean.event.UpdateInfoEvent;
import com.heiguo.blackfruitvip.response.ShareResponse;
import com.heiguo.blackfruitvip.response.UserInfoResponse;
import com.heiguo.blackfruitvip.util.T;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class BlackFruitVipApplication extends Application {
    private String TAG = "BlackFruitVipApplication";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Gson gson;


    //微信支付客户端
    private IWXAPI wxApi;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;

    //定位后相关数据
    private LocationBean locationBean;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        T.init(this);
        if (Constant.DEBUG) {
            Log.d(TAG, "init Xuitils");
        }

        initSp();

        //进入应用就进行定位
        initCityBean();
        initUserInfo();
        initLocationOption();
        startLocation();

        initUMConfig(this, Constant.UMENG_APP_KEY, Constant.UMENG_APP_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);

        //初始化注册
        wxApi = WXAPIFactory.createWXAPI(this, Constant.WeChatAppId);

        //初始化获取分享内容
        updateShareDetail();
    }

    private void initUMConfig(Context context, String appkey, String channel, int deviceType, String pushSecret) {
        UMConfigure.init(context, appkey, channel, deviceType, pushSecret);
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        UMConfigure.setLogEnabled(true);
    }

    public IWXAPI getWxApi() {
        return wxApi;
    }

    private void initUserInfo() {
        if (getUserInfo() == null) {
            UserBean bean = new UserBean();
            bean.setPhone("未登录用户");
            bean.setBalance(0);
            bean.setSaveCount(0);
            bean.setVipDay(0);
            saveUserInfo(bean);
        }
    }

    private void initCityBean() {
        if (getCityPick() == null || getCityPick().getAddress().equals("")) {
            CityBean bean = new CityBean();
            bean.setLongitude(116.46);
            bean.setLatitude(39.92);
            bean.setCity("北京");
            bean.setProvince("北京");
            bean.setCode("0000");
            bean.setAddress("北京");
            saveCityPick(bean);
        }
    }

    public void updateShareDetail() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_SHARE);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ShareResponse response = gson.fromJson(result, ShareResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    saveShareDetail(response.getF_data());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void updateUserInfo() {
        String phone = getLoginPhone();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_GET);
        params.addQueryStringParameter("phone", phone);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserInfoResponse response = gson.fromJson(result, UserInfoResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    saveUserInfo(response.getF_data());
                    EventBus.getDefault().post(new UpdateInfoEvent());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

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
                if (getCityPick().getLatitude() == 39.92) {
                    CityBean bean = new CityBean();
                    bean.setCity(aMapLocation.getCity());
                    bean.setCode(aMapLocation.getCityCode());
                    bean.setProvince(aMapLocation.getProvince());
                    bean.setAddress(aMapLocation.getAddress());
                    bean.setLatitude(aMapLocation.getLatitude());
                    bean.setLongitude(aMapLocation.getLongitude());
                    saveCityPick(bean);
                }
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

    public LocationBean getLocationBean() {
        return locationBean;
    }


    private void initSp() {
        sp = getSharedPreferences("prod", Context.MODE_PRIVATE);
        editor = sp.edit();
        gson = new Gson();
    }

    public void saveShareDetail(String detail) {
        editor.putString("share-detail", detail);
        editor.commit();
    }

    public String getShareDetail() {
        return sp.getString("share-detail", "");
    }


    public void saveLoginPhone(String phone) {
        editor.putString("phone", phone);
        editor.commit();
    }

    public String getLoginPhone() {
        return sp.getString("phone", "");
    }

    public void saveUserInfo(UserBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("info", beanString);
        editor.commit();
    }

    public UserBean getUserInfo() {
        return gson.fromJson(sp.getString("info", ""), UserBean.class);
    }

    public void saveCityPick(CityBean city) {
        editor.putString("city", gson.toJson(city));
        editor.commit();
    }

    public CityBean getCityPick() {
        return gson.fromJson(sp.getString("city", ""), CityBean.class);
    }

    public void saveFirstGo(int mode) {
        editor.putInt("first", mode);
        editor.commit();
    }

    public int getFirstGo() {
        return sp.getInt("first", 0);
    }

    public List<CityBean> getHistoryCityList() {
        return gson.fromJson(sp.getString("history-city", ""), new TypeToken<List<CityBean>>() {
        }.getType());
    }

    public void setHistoryCityList(List<CityBean> list) {
        String beanString = gson.toJson(list);
        editor.putString("history-city", beanString);
        editor.commit();
    }

    public boolean isCurrentVip() {
        UserBean bean = getUserInfo();
        return bean.getVipDay() > -1;
    }
}
