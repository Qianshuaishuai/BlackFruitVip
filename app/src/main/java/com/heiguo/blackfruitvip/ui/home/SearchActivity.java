package com.heiguo.blackfruitvip.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.SearchResultAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    private SearchResultAdapter adapter;

    @ViewInject(R.id.search)
    private EditText searchEditText;

    @ViewInject(R.id.list)
    private RecyclerView listRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //暂时测试
        initList();
    }

    private void initList() {
        List<ShopBean> testList = new ArrayList<>();
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());

        adapter = new SearchResultAdapter(testList);
        adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRecycleView.setAdapter(adapter);
        listRecycleView.setLayoutManager(layoutManager);
    }
}
