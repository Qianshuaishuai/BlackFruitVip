package com.heiguo.blackfruitvip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.CityBean;
import com.heiguo.blackfruitvip.bean.MainBean;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class HistoryCityAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<CityBean> list = new ArrayList();
    private HistoryCityListener listener;

    public HistoryCityAdapter(Context context, List<CityBean> list, HistoryCityListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(list.get(i).getCode());
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CityBean bean = list.get(i);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_city, viewGroup, false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView
                    .findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTv.setText(bean.getAddress());
        convertView.setOnClickListener(this);
        holder.nameTv.setTag(i);

        return convertView;
    }

    public class ViewHolder {
        public TextView nameTv;
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface HistoryCityListener {
        public void clickListener(View v);
    }

    @Override
    public void onClick(View view) {
        listener.clickListener(view);
    }
}
