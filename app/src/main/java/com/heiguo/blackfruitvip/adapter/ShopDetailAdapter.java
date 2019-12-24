package com.heiguo.blackfruitvip.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.ShopBean;

import java.util.List;

public class ShopDetailAdapter extends RecyclerView.Adapter<ShopDetailAdapter.ViewHolder> {

    private List<ShopBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImage,addImg,reduceImg;
        TextView nameTxt,priceTxt,oldPriceTxt,countTxt;

        public ViewHolder(View view) {
            super(view);
            picImage = (ImageView) view.findViewById(R.id.img);
            addImg = (ImageView) view.findViewById(R.id.add);
            reduceImg = (ImageView) view.findViewById(R.id.reduce);
            nameTxt = (TextView) view.findViewById(R.id.name);
            priceTxt = (TextView) view.findViewById(R.id.price);
            oldPriceTxt = (TextView) view.findViewById(R.id.old_price);
            countTxt = (TextView) view.findViewById(R.id.count);
        }

    }

    public ShopDetailAdapter(List<ShopBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_detail, parent, false);
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
                }
            }
        });

        holder.oldPriceTxt.getPaint().setAntiAlias(true);
        holder.oldPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
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
