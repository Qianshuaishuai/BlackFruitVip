package com.heiguo.blackfruitvip.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.MainBean;
import com.heiguo.blackfruitvip.bean.TypeBean;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuAnoAdapter extends BaseAdapter implements View.OnClickListener {

    private List<TypeBean> list;
    private int selectPosition;
    private ShopMenuClickListener listener;
    private Context context;

    public ShopMenuAnoAdapter(Context context, List<TypeBean> list,ShopMenuClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
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
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TypeBean bean = list.get(i);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recycleview_menu, viewGroup,false);
            holder = new ViewHolder();
            holder.nameTxt = (TextView) convertView
                    .findViewById(R.id.name);
            holder.picImage = (ImageView) convertView.findViewById(R.id.img);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (i == selectPosition) {
            holder.layout.setBackgroundColor(Color.parseColor("#f3f3f3"));
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
        }

        holder.nameTxt.setText(list.get(i).getName());

        convertView.setOnClickListener(this);
        holder.picImage.setTag(i);

        return convertView;
    }

    public class ViewHolder {
        public ImageView picImage;
        public TextView nameTxt;
        public LinearLayout layout;
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface ShopMenuClickListener{
        public void clickListener(View v);
    }

    @Override
    public void onClick(View view) {
        listener.clickListener(view);
    }
}
