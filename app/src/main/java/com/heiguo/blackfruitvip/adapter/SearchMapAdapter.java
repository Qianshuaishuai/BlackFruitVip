package com.heiguo.blackfruitvip.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.ShopBean;

import java.text.DecimalFormat;
import java.util.List;

public class SearchMapAdapter extends RecyclerView.Adapter<SearchMapAdapter.ViewHolder> {

    private List<PoiItem> mList;
    private LatLonPoint point;
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

    public void setLatLonPoint(LatLonPoint point) {
        this.point = point;
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

        LatLonPoint currentPoint = mList.get(position).getLatLonPoint();
        double distance = AMapUtils.calculateLineDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()), new LatLng(point.getLatitude(), point.getLongitude()));
        if (distance < 1000) {
            holder.distanceTxt.setText((int) distance + "m");
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            holder.distanceTxt.setText(df.format(distance / 1000) + "km");
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
