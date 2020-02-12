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
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.UserInfoResponse;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements MainFragment.OnFragmentInteractionListener, MeFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener {

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
                    String phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();
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

        //加入fragment
        mainFragment = new MainFragment();
        orderFragment = new OrderFragment();
        meFragment = new MeFragment();
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
        System.out.println(uri);
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
                showWaringDialog();
            }
        }
    }

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
}
