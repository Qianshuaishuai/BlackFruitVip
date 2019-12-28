package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.bean.ShopBean;

import java.text.DecimalFormat;
import java.util.List;

public class PayGoodAdapter extends RecyclerView.Adapter<PayGoodAdapter.ViewHolder> {

    private List<GoodBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, priceTxt;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.good_name);
            priceTxt = (TextView) view.findViewById(R.id.good_price);
        }

    }

    public PayGoodAdapter(List<GoodBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_pay_good, parent, false);
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

        DecimalFormat df = new DecimalFormat("#.00");

        holder.nameTxt.setText(mList.get(position).getName());
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
