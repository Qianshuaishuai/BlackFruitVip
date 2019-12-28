package com.heiguo.blackfruitvip.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.ui.order.GoodDetailActivity;
import com.heiguo.blackfruitvip.ui.order.MenuActivity;

import java.text.DecimalFormat;
import java.util.List;

public class GoodBuyCarAdapter extends RecyclerView.Adapter<GoodBuyCarAdapter.ViewHolder> {

    private List<GoodBean> mList;
    private GoodDetailActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView addImg,reduceImg;
        TextView nameTxt,priceTxt,oldPriceTxt,countTxt;

        public ViewHolder(View view) {
            super(view);
            addImg = (ImageView) view.findViewById(R.id.add);
            reduceImg = (ImageView) view.findViewById(R.id.reduce);
            nameTxt = (TextView) view.findViewById(R.id.name);
            priceTxt = (TextView) view.findViewById(R.id.price);
            oldPriceTxt = (TextView) view.findViewById(R.id.old_price);
            countTxt = (TextView) view.findViewById(R.id.count);
        }

    }

    public GoodBuyCarAdapter(GoodDetailActivity context, List<GoodBean> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_buy_car, parent, false);
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

        DecimalFormat df = new DecimalFormat("#.00");
        holder.nameTxt.setText(mList.get(position).getName());
        holder.oldPriceTxt.setText("" + df.format(mList.get(position).getoPrice()));
        holder.priceTxt.setText("" + df.format(mList.get(position).getPrice()));
        holder.countTxt.setText("" + mList.get(position).getCount());


        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.get(position).setCount(mList.get(position).getCount() + 1);
                holder.countTxt.setText("" + mList.get(position).getCount());
                context.updateBuyCarAndTotal();
            }
        });

        holder.reduceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.get(position).getCount() > 0) {
                    mList.get(position).setCount(mList.get(position).getCount() - 1);
                    holder.countTxt.setText("" + mList.get(position).getCount());
                    context.updateBuyCarAndTotal();
                }
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
