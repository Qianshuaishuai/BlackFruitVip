package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.MainBean;
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.bean.StoreBean;

import org.xutils.x;

import java.text.DecimalFormat;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<StoreBean> mList;
    private LatLonPoint point;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImage;
        TextView nameTxt, addressTxt, tipTxt, distanceTxt;

        public ViewHolder(View view) {
            super(view);
            picImage = (ImageView) view.findViewById(R.id.img);
            nameTxt = (TextView) view.findViewById(R.id.name);
            addressTxt = (TextView) view.findViewById(R.id.address);
            tipTxt = (TextView) view.findViewById(R.id.tip);
            distanceTxt = (TextView) view.findViewById(R.id.distance);
        }

    }

    public SearchResultAdapter(List<StoreBean> mList) {
        this.mList = mList;
    }


    public void setLatLonPoint(LatLonPoint point) {
        this.point = point;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_search_result, parent, false);
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

        holder.nameTxt.setText(mList.get(position).getName());
        holder.addressTxt.setText(mList.get(position).getAddress());
        holder.tipTxt.setText(mList.get(position).getAnnoun());

        LatLonPoint currentPoint = new LatLonPoint(mList.get(position).getLatitude(),mList.get(position).getLongitude());
        double distance = AMapUtils.calculateLineDistance(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()), new LatLng(point.getLatitude(), point.getLongitude()));
        if (distance < 1000) {
            holder.distanceTxt.setText((int) distance + "m");
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            holder.distanceTxt.setText(df.format(distance / 1000) + "km");
        }

        x.image().bind(holder.picImage,mList.get(position).getImage());
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
