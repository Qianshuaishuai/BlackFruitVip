package com.heiguo.blackfruitvip.linklayout.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.adapter.ShopDetailAdapter;
import com.heiguo.blackfruitvip.bean.GoodBean;
import com.heiguo.blackfruitvip.ui.order.MenuActivity;

import org.xutils.x;

import java.text.DecimalFormat;
import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-10-15
 * Time: 23:29
 * <br/><br/>
 */
public class SimpleArrayAdapter<T>
        extends RecyclerView.Adapter<SimpleArrayAdapter.ViewHolder>
        implements SectionIndexer {

    private final MenuActivity context;
    private final @LayoutRes
    int mResource;
    private List<T> mObjects;
    private SectionIndexer mRealSectionIndexer; // 代理

    public SimpleArrayAdapter(@NonNull MenuActivity activity, @NonNull List<T> objects, SectionIndexer realSectionIndexer) {
        this.context = activity;
        mResource = R.layout.recycleview_detail;
        mObjects = objects;
        mRealSectionIndexer = realSectionIndexer;
    }

    @Override
    public SimpleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, mResource, null));
    }

    @Override
    public void onBindViewHolder(SimpleArrayAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        GoodBean bean = (GoodBean) mObjects.get(position);

        holder.oldPriceTxt.getPaint().setAntiAlias(true);
        holder.oldPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        DecimalFormat df = new DecimalFormat("#.00");
        holder.nameTxt.setText(bean.getName());
        holder.oldPriceTxt.setText("" + df.format(bean.getoPrice()));
        holder.priceTxt.setText("" + df.format(bean.getPrice()));
        holder.countTxt.setText("" + bean.getCount());
        x.image().bind(holder.picImage, bean.getImage());


        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!context.checkIsLogin()) {
                    return;
                }
                if (!context.checkIsVip()) {
                    return;
                }
                bean.setCount(bean.getCount() + 1);
                holder.countTxt.setText("" + bean.getCount());
                context.updateBuyCarAndTotal();
            }
        });

        holder.reduceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!context.checkIsLogin()) {
                    return;
                }
                if (!context.checkIsVip()) {
                    return;
                }
                if (bean.getCount() > 0) {
                    bean.setCount(bean.getCount() - 1);
                    holder.countTxt.setText("" + bean.getCount());
                    context.updateBuyCarAndTotal();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mObjects == null ? 0 : mObjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picImage, addImg, reduceImg;
        TextView nameTxt, priceTxt, oldPriceTxt, countTxt;

        public ViewHolder(View view) {
            super(view);
            picImage = (ImageView) view.findViewById(R.id.img);
            addImg = (ImageView) view.findViewById(R.id.add);
            reduceImg = (ImageView) view.findViewById(R.id.reduce);
            nameTxt = (TextView) view.findViewById(R.id.name);
            priceTxt = (TextView) view.findViewById(R.id.price);
            oldPriceTxt = (TextView) view.findViewById(R.id.old_price);
            countTxt = (TextView) view.findViewById(R.id.count);
        }

    }

    // 代理
    @Override
    public Integer[] getSections() {
        return (Integer[]) mRealSectionIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mRealSectionIndexer.getPositionForSection(sectionIndex);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mRealSectionIndexer.getSectionForPosition(position);
    }
}