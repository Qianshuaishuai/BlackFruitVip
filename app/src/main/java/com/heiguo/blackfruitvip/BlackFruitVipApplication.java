package com.heiguo.blackfruitvip;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.bean.UserBean;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.x;

public class BlackFruitVipApplication extends Application {
    private String TAG = "BlackFruitVipApplication";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Gson gson;

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
    }

    private void initSp() {
        sp = getSharedPreferences("prod", Context.MODE_PRIVATE);
        editor = sp.edit();
        gson = new Gson();
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
}
