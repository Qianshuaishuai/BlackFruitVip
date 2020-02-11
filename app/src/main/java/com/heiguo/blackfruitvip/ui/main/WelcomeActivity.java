package com.heiguo.blackfruitvip.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.ui.home.HomeActivity;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;


@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    private final String TAG = "WelcomeActivity";

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.CALL_PHONE};

    private int skipTimeCount = 3;
    private Timer timer;
    private TimerTask timerTask;

    @ViewInject(R.id.skip)
    private Button skipButton;

    @Event(R.id.skip)
    private void skip(View view) {
        skip();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initPermission();
        initSkipTime();
    }

    private void initSkipTime() {
        switch (((BlackFruitVipApplication) getApplication()).getFirstGo()) {
            case 0:
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                skipButton.setText(skipTimeCount + " " + "跳过");
                                skipTimeCount--;
                                if (skipTimeCount == -1) {
                                    timer.cancel();
                                    skip();
                                }
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 0, 1000);
                break;
            case 1:
                String phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();
                if (phone == "") {
                    Intent newIntent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(newIntent);
                    finish();
                } else {
                    Intent newIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(newIntent);
                    finish();
                }

                break;
        }

    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[1]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                showWaringDialog();
            }
        }
    }

    private void skip() {
        Intent newIntent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(newIntent);
        finish();
        //保存状态
        ((BlackFruitVipApplication) getApplication()).saveFirstGo(1);
    }

    private void requestPermission() {

        Log.i(TAG, "requestPermission");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "checkSelfPermission");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Log.i(TAG, "shouldShowRequestPermissionRationale");
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        permissions,
                        Constant.STATUS_CODE_PERMISSION_REQUEST);

            } else {
                Log.i(TAG, "requestPermissions");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        permissions,
                        Constant.STATUS_CODE_PERMISSION_REQUEST);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.STATUS_CODE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.i(TAG, "onRequestPermissionsResult denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showWaringDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用->PermissionDemo->权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish();
                    }
                }).show();
    }
}
