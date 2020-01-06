package com.heiguo.blackfruitvip.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.ShopBean;

import org.xutils.x;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<GoodBean> mList;
    private int selectPosition;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImage;
        TextView nameTxt, priceTxt;

        public ViewHolder(View view) {
            super(view);
            picImage = (ImageView) view.findViewById(R.id.img);
            nameTxt = (TextView) view.findViewById(R.id.name);
            priceTxt = (TextView) view.findViewById(R.id.price);
        }

    }

    public OrderDetailAdapter(List<GoodBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_order_detail, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                    selectPosition = position;
                }
            }
        });

        x.image().bind(holder.picImage, mList.get(position).getImage());
        holder.nameTxt.setText(mList.get(position).getName());

        DecimalFormat df = new DecimalFormat("#.00");

        holder.priceTxt.setText("￥" + df.format(mList.get(position).getPrice()) + " × " + mList.get(position).getCount());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
