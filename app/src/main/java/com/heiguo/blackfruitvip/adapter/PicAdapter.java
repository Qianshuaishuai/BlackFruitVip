package com.heiguo.blackfruitvip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.bean.MainBean;

import org.xutils.x;

import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private List<MainBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImage;

        public ViewHolder(View view) {
            super(view);
            picImage = (ImageView) view.findViewById(R.id.img);
        }

    }

    public PicAdapter(List<MainBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_pic, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        x.image().bind(holder.picImage, mList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
