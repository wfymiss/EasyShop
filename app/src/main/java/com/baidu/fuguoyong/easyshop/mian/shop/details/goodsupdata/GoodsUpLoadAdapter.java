package com.baidu.fuguoyong.easyshop.mian.shop.details.goodsupdata;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.modle.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/25.
 */

public class GoodsUpLoadAdapter extends RecyclerView.Adapter {
    // 适配器的数据
    private ArrayList<ImageItem> list = new ArrayList<>();
    private LayoutInflater inflater;

    public GoodsUpLoadAdapter(ArrayList<ImageItem> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    //#########################    逻辑：模式选择 #####################################
    //编辑器模式 1 为右图 2为 无图
    public static final int MODE_NORMAL = 1;
    public static final int MODE_MULTI_SELECT = 2;
    // 代表选择模式
    public int mode;

    //用枚举辨识 有图或者无图
    public enum ITEM_TYPE {
        ITEM_NORMAL, ITEM_ADD
    }

    public void changeMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public int getMode() {
        return mode;
    }

    // ############################  外部调用的相关方法  start ##################
    // 添加图片
    public void add(ImageItem imageItem) {
        list.add(imageItem);
    }

    public int getsize() {
        return list.size();
    }

    //获得数据
    public ArrayList<ImageItem> getList() {
        return list;
    }

    public void notifyData() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //当position与图片数量相同时，则为加号布局
        if (position == list.size()) return ITEM_TYPE.ITEM_ADD.ordinal();
        return ITEM_TYPE.ITEM_NORMAL.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断当前显示item的类型，有图或者无图，从而选择不同的ViewHoler（不同的布局）
        if (viewType == ITEM_TYPE.ITEM_NORMAL.ordinal()) {
            //有图的viewholder
            return new ItemSelectViewHolder(inflater.inflate(R.layout.layout_item_recyclerview, parent, false));
        } else {
            //无图，显示加号的ViewHolder
            return new ItemAddViewHolder(inflater.inflate(R.layout.layout_item_recyclerviewlast, parent, false));

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //判断当前的vh是不是ItemSelectViewHolder的实例
        if (holder instanceof ItemSelectViewHolder) {
            //当前数据
            ImageItem imageItem = list.get(position);
            //拿到当前vh(因为已经判断是vh的实例，所以强转)
            final ItemSelectViewHolder selectViewHolder = (ItemSelectViewHolder) holder;
            selectViewHolder.photo = imageItem;
            //判断模式（正常）或者（编辑）
            if (mode == MODE_MULTI_SELECT) {
                // 可选框可见、
                selectViewHolder.checkBox.setVisibility(View.INVISIBLE);
                selectViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //imageitem中选择状态改变
                        list.get(position).setIsCheck(isChecked);
                    }
                });

                selectViewHolder.checkBox.setChecked(imageItem.isCheck());
            } else if (mode == MODE_NORMAL) {
                // //可选框隐藏
                selectViewHolder.checkBox.setVisibility(View.GONE);
            }
            //图片设置
            selectViewHolder.ivPhoto.setImageBitmap(imageItem.getBitmap());
            //单击图片跳转到图片展示页
            selectViewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到图片详情页
                    if (listener != null) {
                        listener.onPhotoClicked(selectViewHolder.photo, selectViewHolder.ivPhoto);
                    }
                }
            });
            // 长摁图片改为可删除模式
            selectViewHolder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 模式改为删除模式
                    mode = MODE_MULTI_SELECT;
                    //更新
                    notifyDataSetChanged();
                    //执行长按的监听事件
                    if (listener != null) {
                        listener.onLongClicked();
                    }
                    return false;
                }
            });
        } else if (holder instanceof ItemAddViewHolder) {
            ItemAddViewHolder item_add = (ItemAddViewHolder) holder;
            //最多八张图
            if (position == 8) {
                item_add.id_add.setVisibility(View.GONE);
            } else {
                item_add.id_add.setVisibility(View.VISIBLE);
            }
            //点击添加图片
            item_add.id_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加图片的监听
                    if (listener != null) {
                        listener.onAddClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //最多八张图
        return Math.min(list.size() + 1, 8);
    }

    // 显示添加按钮的
    public static class ItemAddViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ib_recycle_add)
        ImageButton id_add;

        public ItemAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //显示有图片的
    public static class ItemSelectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.cb_check_photo)
        CheckBox checkBox;
        ImageItem photo;//用来控制checkbox的选择属性

        public ItemSelectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //############################  监听借口####################################
    public interface OnItemClickedListener {
        //无图，点击添加图片
        void onAddClicked();

        //有图，点击跳转到图片展示页
        void onPhotoClicked(ImageItem photo, ImageView imageView);

        //有图，长按执行删除相关操作
        void onLongClicked();
    }

    private OnItemClickedListener listener;

    public void setList(OnItemClickedListener listener) {
        this.listener = listener;
    }

}

