package com.heiguo.blackfruitvip.ui.home;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements MainFragment.OnFragmentInteractionListener, MeFragment.OnFragmentInteractionListener {

    @ViewInject(R.id.navigation)
    private BottomNavigationView navigation;

    @ViewInject(R.id.layout)
    private FrameLayout layout;

    private MainFragment mainFragment;
    private MainFragment orderFragment;
    private MeFragment meFragment;
    private Fragment[] fragments;
    private int lastfragment = 0;

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

        initNavigationBar();
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
        orderFragment = new MainFragment();
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri);
    }
}
