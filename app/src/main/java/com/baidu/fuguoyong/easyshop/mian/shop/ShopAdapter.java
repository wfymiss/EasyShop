package com.baidu.fuguoyong.easyshop.mian.shop;

import android.content.Context;
import android.os.Binder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopApi;
import com.baidu.fuguoyong.easyshop.modle.GoodsInfo;
import com.baidu.fuguoyong.easyshop.modle.GoodsResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewholder> {

    // 所需数据
    private List<GoodsInfo> list = new ArrayList();
    private Context context;

    // 添加数据
    public void addData(List<GoodsInfo> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    // 清空数据
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        MyViewholder holder = new MyViewholder(LayoutInflater.from(context
        ).inflate(R.layout.item_recycler, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, final int position) {
        // 商品名称
        holder.tvItemName.setText(list.get(position).getName());
        // 获取价格
        String price = context.getString(R.string.goods_money, list.get(position).getPrice());
        holder.tvItemPrice.setText(price);
        // 商品图片
        Picasso.with(context).
                load(EasyShopApi.IMAGE_URL+list.get(position).getPage())
                .error(R.drawable.user_ico)
                .placeholder(R.drawable.user_ico)
                .into(holder.ivItemRecycler);

        //点击图片，跳转到商品详情页
        holder.ivItemRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                   onItemClick.onclick(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_recycler)
        ImageView ivItemRecycler;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_price)
        TextView tvItemPrice;

        public MyViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    // 跳转到详情页的借口
    public interface onItemClick {
        // 点击商品Item ，跳转到详情页
        void onclick(GoodsInfo goodsInfo);
    }

    private onItemClick onItemClick;

    public void setListener(onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
