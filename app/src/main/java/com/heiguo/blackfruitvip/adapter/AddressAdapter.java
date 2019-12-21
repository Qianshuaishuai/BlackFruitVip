package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.ShopBean;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<ShopBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagTxt, addressTxt, namePhoneTxt;
        ImageView editButton;

        public ViewHolder(View view) {
            super(view);
            tagTxt = (TextView) view.findViewById(R.id.tag);
            addressTxt = (TextView) view.findViewById(R.id.address);
            namePhoneTxt = (TextView) view.findViewById(R.id.name_phone);
            editButton = (ImageView) view.findViewById(R.id.edit);
        }

    }

    public AddressAdapter(List<ShopBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_address, parent, false);
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
