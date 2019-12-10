package com.heiguo.blackfruitvip;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

public class BlackFruitVipApplication extends Application {
    private  String TAG = "BlackFruitVipApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        if(Constant.DEBUG) {
            Log.d(TAG ,"init Xuitils");
        }
    }
}
