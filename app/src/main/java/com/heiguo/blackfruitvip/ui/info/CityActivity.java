package com.heiguo.blackfruitvip.ui.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.HistoryCityAdapter;
import com.heiguo.blackfruitvip.adapter.MenuAdapter;
import com.heiguo.blackfruitvip.adapter.SearchMapAdapter;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_city)
public class CityActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private HistoryCityAdapter adapter;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;

    private SearchMapAdapter searchMapAdapter;
    private List<PoiItem> searchMapList = new ArrayList<>();

    @ViewInject(R.id.history)
    private GridView historyList;

    @ViewInject(R.id.search_list)
    private RecyclerView searchList;

    @ViewInject(R.id.search)
    private EditText search;

    @ViewInject(R.id.search_layout)
    private LinearLayout layoutSearch;

    @ViewInject(R.id.sv)
    private ScrollView sv;

    @Event(R.id.back)
    private void back(View view) {

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
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());
        testList.add(new ShopBean());

        adapter = new HistoryCityAdapter(this, testList, new HistoryCityAdapter.HistoryCityListener() {
            @Override
            public void clickListener(View v) {
                HistoryCityAdapter.ViewHolder holder = (HistoryCityAdapter.ViewHolder) v.getTag();
                System.out.println(holder.nameTv.getTag());
            }
        });

        historyList.setAdapter(adapter);

        //监听搜索框
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    layoutSearch.setVisibility(View.GONE);
                } else {
                    String searchTxt = charSequence.toString();
                    PoiSearch(searchTxt);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //显示搜索列表
        searchMapAdapter = new SearchMapAdapter(searchMapList);
        searchMapAdapter.setOnItemClickListener(new SearchMapAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                System.out.println(position);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchList.setLayoutManager(layoutManager);
        searchList.setAdapter(searchMapAdapter);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {
            searchMapList.clear();
            for (int s = 0; s < poiResult.getPois().size(); s++) {
                searchMapList.add(poiResult.getPois().get(s));
            }
            searchMapAdapter.notifyDataSetChanged();
            if (searchMapList.size() == 0) {
                layoutSearch.setVisibility(View.GONE);
            } else {
                layoutSearch.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void PoiSearch(String searchTxt) {
        query = new PoiSearch.Query(searchTxt, "", "珠海");
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }
}
