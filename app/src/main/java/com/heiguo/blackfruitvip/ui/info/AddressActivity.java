package com.heiguo.blackfruitvip.ui.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.AddressAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_address)
public class AddressActivity extends BaseActivity {

    private AddressAdapter adapter;

    @ViewInject(R.id.list)
    private RecyclerView listRecycleView;

    @Event(R.id.layout_add)
    private void add(View view){

    }

    @Event(R.id.back)
    private void back(View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        List<ShopBean> testList = new ArrayList<>();
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());

        adapter = new AddressAdapter(testList);
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRecycleView.setLayoutManager(layoutManager);
        listRecycleView.setAdapter(adapter);
    }
}
