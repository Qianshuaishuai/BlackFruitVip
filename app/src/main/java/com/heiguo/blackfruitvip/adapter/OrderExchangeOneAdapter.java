package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.AddressBean;
import com.heiguo.blackfruitvip.bean.ExChangeBean;
import com.heiguo.blackfruitvip.ui.info.AddressActivity;
import com.heiguo.blackfruitvip.ui.order.OrderDetailActivity;
import com.heiguo.blackfruitvip.util.CopyUtil;
import com.heiguo.blackfruitvip.util.T;

import java.util.List;

public class OrderExchangeOneAdapter extends RecyclerView.Adapter<OrderExchangeOneAdapter.ViewHolder> {

    private List<ExChangeBean> mList;
    private OrderDetailActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView exchangeTxt;
        Button copyButton;

        public ViewHolder(View view) {
            super(view);
            exchangeTxt = (TextView) view.findViewById(R.id.exchange);
            copyButton = (Button) view.findViewById(R.id.copy);
        }

    }

    public OrderExchangeOneAdapter(OrderDetailActivity context, List<ExChangeBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_exchange_one, parent, false);
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

        holder.exchangeTxt.setText("兑换" + mList.get(position).getContent());
        holder.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyUtil.copy(context, mList.get(position).getContent().replace("码", "").replace("码", "").replace(":",""));
                T.s("复制成功");
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
