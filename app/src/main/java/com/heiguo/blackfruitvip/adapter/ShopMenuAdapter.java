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
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.bean.TypeBean;

import java.util.List;

public class ShopMenuAdapter extends RecyclerView.Adapter<ShopMenuAdapter.ViewHolder> {

    private List<TypeBean> mList;
    private int selectPosition;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImage;
        TextView nameTxt;
        LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            picImage = (ImageView) view.findViewById(R.id.img);
            nameTxt = (TextView) view.findViewById(R.id.name);
            layout = (LinearLayout) view.findViewById(R.id.layout);
        }

    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public ShopMenuAdapter(List<TypeBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_menu, parent, false);
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

        if (position == selectPosition) {
            holder.layout.setBackgroundColor(Color.parseColor("#f3f3f3"));
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
        }

        holder.nameTxt.setText(mList.get(position).getName());
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
