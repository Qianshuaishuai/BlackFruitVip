package com.heiguo.blackfruitvip.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.OrderBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.response.OrderListResponse;
import com.heiguo.blackfruitvip.ui.home.OrderFragment;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderBean> mList;
    private OrderFragment fragment;
    private int selectPosition;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countTxt, dateTxt, totalTxt, tipTxt;
        Button btOne, btTwo;
        LinearLayout layoutBt;

        public ViewHolder(View view) {
            super(view);
            countTxt = (TextView) view.findViewById(R.id.count);
            dateTxt = (TextView) view.findViewById(R.id.date);
            totalTxt = (TextView) view.findViewById(R.id.total);
            tipTxt = (TextView) view.findViewById(R.id.tip);
            btOne = (Button) view.findViewById(R.id.bt_one);
            btTwo = (Button) view.findViewById(R.id.bt_two);
            layoutBt = (LinearLayout) view.findViewById(R.id.layout_button);
        }

    }

    public OrderAdapter(OrderFragment fragment, List<OrderBean> mList) {
        this.mList = mList;
        this.fragment = fragment;
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
                holder.tipTxt.setText("待支付");
                break;
            case 3:
                holder.tipTxt.setText("等待商家接单");
                holder.layoutBt.setVisibility(View.GONE);
                break;
            case 4:
                if(mList.get(position).getService() == 2){
                    holder.tipTxt.setText("等待商家送货");
                }else{
                    holder.tipTxt.setText("查看取餐码");
                }
                break;
            case 5:
                holder.tipTxt.setText("已完成");
                holder.layoutBt.setVisibility(View.GONE);
                break;
        }

        holder.countTxt.setText("" + mList.get(position).getTotalCount());
        holder.totalTxt.setText("￥" + mList.get(position).getRealPrice());
        holder.dateTxt.setText(mList.get(position).getCreateTime());

        holder.btOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = mList.get(position).getStore().getTel();
                callPhone(tel);
            }
        });

        holder.btTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.completeOrder(mList.get(position).getId());
            }
        });
    }

    private void callPhone(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + tel);
        intent.setData(data);
        fragment.startActivity(intent);
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
