package com.heiguo.blackfruitvip.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.ShopBean;

import java.util.List;

public class SearchMapAdapter extends RecyclerView.Adapter<SearchMapAdapter.ViewHolder> {

    private List<PoiItem> mList;
    private int selectPosition;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, addressTxt, distanceTxt;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            addressTxt = (TextView) view.findViewById(R.id.address);
            distanceTxt = (TextView) view.findViewById(R.id.distance);
        }

    }

    public SearchMapAdapter(List<PoiItem> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_search, parent, false);
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

        holder.nameTxt.setText(mList.get(position).getTitle());
        holder.addressTxt.setText(mList.get(position).getCityName() + mList.get(position).getAdName() + mList.get(position).getSnippet());

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
