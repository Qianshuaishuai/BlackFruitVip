package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.ExChangeBean;
import com.heiguo.blackfruitvip.ui.order.OrderDetailActivity;
import com.heiguo.blackfruitvip.util.CopyUtil;
import com.heiguo.blackfruitvip.util.T;

import java.util.List;

public class OrderExchangeTwoAdapter extends RecyclerView.Adapter<OrderExchangeTwoAdapter.ViewHolder> {

    private List<ExChangeBean> mList;
    private OrderDetailActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView exchangeCardTxt, exchangePassTxt;
        Button copyCardButton, copyPassButton;

        public ViewHolder(View view) {
            super(view);
            exchangeCardTxt = (TextView) view.findViewById(R.id.exchange_card);
            exchangePassTxt = (TextView) view.findViewById(R.id.exchange_pass);
            copyCardButton = (Button) view.findViewById(R.id.copy_card);
            copyPassButton = (Button) view.findViewById(R.id.copy_pass);
        }

    }

    public OrderExchangeTwoAdapter(OrderDetailActivity context, List<ExChangeBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_exchange_two, parent, false);
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

        String[] dataTxt = mList.get(position).getContent().split(",");
        holder.exchangeCardTxt.setText(dataTxt[0]);
        holder.exchangePassTxt.setText(dataTxt[1]);

        holder.copyCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyUtil.copy(context, dataTxt[0].replace("卡号:", ""));
                T.s("复制成功");
            }
        });

        holder.copyPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyUtil.copy(context, dataTxt[1].replace("密码:", ""));
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
