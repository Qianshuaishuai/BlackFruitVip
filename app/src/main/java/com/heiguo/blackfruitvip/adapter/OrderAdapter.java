package com.heiguo.blackfruitvip.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.OrderBean;
import com.heiguo.blackfruitvip.bean.ShopBean;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderBean> mList;
    private int selectPosition;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countTxt, dateTxt, totalTxt;
        Button btOne, btTwo;
        LinearLayout layoutBt;

        public ViewHolder(View view) {
            super(view);
            countTxt = (TextView) view.findViewById(R.id.count);
            dateTxt = (TextView) view.findViewById(R.id.date);
            totalTxt = (TextView) view.findViewById(R.id.total);
            btOne = (Button) view.findViewById(R.id.bt_one);
            btTwo = (Button) view.findViewById(R.id.bt_two);
            layoutBt = (LinearLayout) view.findViewById(R.id.layout_button);
        }

    }

    public OrderAdapter(List<OrderBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_order, parent, false);
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

        switch (mList.get(position).getStatus()) {
            case 2:
                holder.layoutBt.setVisibility(View.GONE);
                break;
        }
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
