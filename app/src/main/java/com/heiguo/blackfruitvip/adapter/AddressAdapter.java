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
import com.heiguo.blackfruitvip.bean.ShopBean;
import com.heiguo.blackfruitvip.ui.info.AddressActivity;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressBean> mList;
    private AddressActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagTxt, addressTxt, namePhoneTxt, detailTxt;
        ImageView editButton;

        public ViewHolder(View view) {
            super(view);
            tagTxt = (TextView) view.findViewById(R.id.tag);
            detailTxt = (TextView) view.findViewById(R.id.detail);
            addressTxt = (TextView) view.findViewById(R.id.address);
            namePhoneTxt = (TextView) view.findViewById(R.id.name_phone);
            editButton = (ImageView) view.findViewById(R.id.edit);
        }

    }

    public AddressAdapter(AddressActivity context, List<AddressBean> mList) {
        this.context = context;
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

        String tagStr = "";
        switch (mList.get(position).getTag()) {
            case 0:
                tagStr = "家";
                break;
            case 1:
                tagStr = "公司";
                break;
            case 2:
                tagStr = "学校";
                break;
        }

        String sexStr = "";
        switch (mList.get(position).getSex()) {
            case 0:
                sexStr = "先生";
                break;
            case 1:
                sexStr = "女士";
        }

        holder.addressTxt.setText(mList.get(position).getAddress());
        holder.namePhoneTxt.setText(mList.get(position).getName() + "(" + sexStr + ")" + mList.get(position).getPhone());
        holder.detailTxt.setText(mList.get(position).getDetail());
        holder.tagTxt.setText(tagStr);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startEditActivity(position, Constant.ADDRESS_MODE_GO_EDIT);
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
