package com.heiguo.blackfruitvip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.MainBean;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<MainBean> list = new ArrayList();
    private MenuClickListener listener;

    public MenuAdapter(Context context, List<MainBean> list,MenuClickListener listener) {
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
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MainBean bean = list.get(i);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_menu, viewGroup,false);
            holder = new ViewHolder();
            holder.menuTxt = (TextView) convertView
                    .findViewById(R.id.title);
            holder.menuImg = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        x.image().bind(holder.menuImg, bean.getImage());
        holder.menuTxt.setText(bean.getName());

        convertView.setOnClickListener(this);
        holder.menuImg.setTag(i);

        return convertView;
    }

    public class ViewHolder {
        public TextView menuTxt;
        public ImageView menuImg;
    }

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MenuClickListener{
        public void clickListener(View v);
    }

    @Override
    public void onClick(View view) {
        listener.clickListener(view);
    }
}
