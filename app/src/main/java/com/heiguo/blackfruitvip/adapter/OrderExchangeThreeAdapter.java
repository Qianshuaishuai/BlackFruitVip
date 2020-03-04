package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.ExChangeBean;
import com.heiguo.blackfruitvip.ui.order.OrderDetailActivity;
import com.heiguo.blackfruitvip.util.CopyUtil;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.x;

import java.util.List;

public class OrderExchangeThreeAdapter extends RecyclerView.Adapter<OrderExchangeThreeAdapter.ViewHolder> {

    private List<ExChangeBean> mList;
    private OrderDetailActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout topLayout, detailLayout;
        TextView exchangeTipTxt, exchangeControlTipTxt;
        ImageView detailIcon, exchangeControlIcon;

        public ViewHolder(View view) {
            super(view);
            topLayout = (LinearLayout) view.findViewById(R.id.layout_top);
            detailLayout = (LinearLayout) view.findViewById(R.id.layout_detail);
            exchangeTipTxt = (TextView) view.findViewById(R.id.exchange_tip);
            exchangeControlTipTxt = (TextView) view.findViewById(R.id.exchange_control_tip);
            detailIcon = (ImageView) view.findViewById(R.id.detail_icon);
            exchangeControlIcon = (ImageView) view.findViewById(R.id.exchange_control_icon);
        }

    }

    public OrderExchangeThreeAdapter(OrderDetailActivity context, List<ExChangeBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_exchange_three, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        String tip = "兑换码" + (position + 1) + "(共" + mList.size() + "个兑换码)";
        x.image().bind(holder.detailIcon, mList.get(position).getContent());
        holder.exchangeTipTxt.setText(tip);
        if (position == 0) {
            holder.exchangeControlTipTxt.setText("收起");
            holder.exchangeControlIcon.setImageResource(R.mipmap.ic_common_up);
            holder.detailLayout.setVisibility(View.VISIBLE);
        } else {
            holder.exchangeControlTipTxt.setText("展开");
            holder.exchangeControlIcon.setImageResource(R.mipmap.ic_common_down);
            holder.detailLayout.setVisibility(View.GONE);
        }

        holder.topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.exchangeControlTipTxt.getText().toString().equals("收起")) {
                    holder.exchangeControlTipTxt.setText("展开");
                    holder.exchangeControlIcon.setImageResource(R.mipmap.ic_common_down);
                    holder.detailLayout.setVisibility(View.GONE);
                } else {
                    holder.exchangeControlTipTxt.setText("收起");
                    holder.exchangeControlIcon.setImageResource(R.mipmap.ic_common_up);
                    holder.detailLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.detailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.showScanDetailDialog(mList.get(position).getContent());
            }
        });
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
