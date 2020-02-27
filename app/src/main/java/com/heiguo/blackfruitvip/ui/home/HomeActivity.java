package com.heiguo.blackfruitvip.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.event.OrderStatusEvent;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.UserInfoResponse;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;
import com.heiguo.blackfruitvip.util.T;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, MainFragment.OnFragmentInteractionListener, MeFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener {

    @ViewInject(R.id.navigation)
    private BottomNavigationView navigation;

    @ViewInject(R.id.layout)
    private FrameLayout layout;

    private MainFragment mainFragment;
    private OrderFragment orderFragment;
    private MeFragment meFragment;
    private Fragment[] fragments;
    private int lastfragment = 0;
    private AlertDialog noLoginDialog;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.CALL_PHONE};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            refreshItemIcon();
            String phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();
            switch (item.getItemId()) {
                case R.id.navigation_main:
//                    item.setTextColor(getResources().getColor(R.color.colorYellow));
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                        item.setIcon(R.mipmap.ic_home_main_selected);
                    }
                    return true;
                case R.id.navigation_order:
                    if (phone == "") {
                        noLoginDialog.show();
                        backItemIcon();
                        return false;
                    }
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                        item.setIcon(R.mipmap.ic_home_order_selected);
                    }
                    return true;
                case R.id.navigation_me:
                    if (phone == "") {
                        noLoginDialog.show();
                        backItemIcon();
                        return false;
                    }
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                        item.setIcon(R.mipmap.ic_home_me_selected);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        initNavigationBar();
        initNoLoginDialog();
        ((BlackFruitVipApplication) getApplication()).updateUserInfo();

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int orderStatus = intent.getIntExtra("order-status", 0);

        if (orderStatus != 0) {
//            switchFragment(lastfragment, 1);
//            refreshItemIcon();
//            MenuItem item2 = navigation.getMenu().findItem(R.id.navigation_order);
//            item2.setIcon(R.mipmap.ic_home_order_selected);
            navigation.setSelectedItemId(R.id.navigation_order);
//            EventBus.getDefault().post(new OrderStatusEvent(orderStatus));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initNavigationBar() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.select_menu_color);
        navigation.setItemTextColor(csl);
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_main);
        item1.setIcon(R.mipmap.ic_home_main_selected);

        Intent intent = getIntent();
        int orderStatus = intent.getIntExtra("order-status", 0);

        //加入fragment
        mainFragment = new MainFragment();
        orderFragment = new OrderFragment();
        meFragment = new MeFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("order-status", orderStatus);
        orderFragment.setArguments(bundle);

        fragments = new Fragment[]{mainFragment, orderFragment, meFragment};

        getSupportFragmentManager().beginTransaction().replace(R.id.layout, mainFragment).show(mainFragment).commit();
    }

    /**
     * 切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.layout, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }


    private void refreshItemIcon() {
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_main);
        item1.setIcon(R.mipmap.ic_home_main_normal);
        MenuItem item2 = navigation.getMenu().findItem(R.id.navigation_order);
        item2.setIcon(R.mipmap.ic_home_order_normal);
        MenuItem item3 = navigation.getMenu().findItem(R.id.navigation_me);
        item3.setIcon(R.mipmap.ic_home_me_normal);
    }

    private void backItemIcon() {
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_main);
        MenuItem item2 = navigation.getMenu().findItem(R.id.navigation_order);
        MenuItem item3 = navigation.getMenu().findItem(R.id.navigation_me);
        switch (lastfragment) {
            case 0:
                item1.setIcon(R.mipmap.ic_home_main_selected);
                break;
            case 1:
                item2.setIcon(R.mipmap.ic_home_order_selected);
                break;
            case 2:
                item3.setIcon(R.mipmap.ic_home_me_selected);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void initNoLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_user_common, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noLoginDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("你还未登录，是否前去登录？");
        Button leftButton = (Button) view.findViewById(R.id.left);
        Button rightButton = (Button) view.findViewById(R.id.right);

        leftButton.setText("继续浏览");
        rightButton.setText("前往登录");

        leftButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noLoginDialog.cancel();
            }
        });

        rightButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                noLoginDialog.cancel();
                startLoginActivity();
            }
        });

        noLoginDialog.setCancelable(false);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[1]);
            if (i != PackageManager.PERMISSION_GRANTED) {
//                showWaringDialog();

//                EasyPermissions.requestPermissions(
//                        new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
//                                .setRationale(R.string.camera_and_location_rationale)
//                                .setPositiveButtonText(R.string.rationale_ask_ok)
//                                .setNegativeButtonText(R.string.rationale_ask_cancel)
//                                .setTheme(R.style.my_fancy_style)
//                                .build());

                EasyPermissions.requestPermissions(this, "您需要允许以下权限，才可以正常使用应用",
                        Constant.REQUEST_PERMISSION_CODE, permissions);
            }
        }
    }

//    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
//    private void methodRequiresTwoPermission() {
//        if (EasyPermissions.hasPermissions(this, permissions)) {
//            // Already have permission, do the thing
//            // ...
//        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
//                    RC_CAMERA_AND_LOCATION, permissions);
//        }
//    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用->黑果会员->权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                        finish();
                    }
                }).show();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("警告！")
                    .setMessage("如拒绝权限将无法正常使用应用！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
