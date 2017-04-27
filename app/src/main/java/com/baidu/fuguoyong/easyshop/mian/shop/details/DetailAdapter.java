package com.baidu.fuguoyong.easyshop.mian.shop.details;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/23.
 */

public class DetailAdapter extends PagerAdapter {
    private ArrayList<ImageView> list;

    public DetailAdapter(ArrayList<ImageView> list) {
        this.list = list;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = list.get(position);
        container.addView(imageView);
        // 实现图片点击跳转到展示页
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick();
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // ===================================
    public interface OnItemCilckListener {
        void onItemClick();
    }

    private OnItemCilckListener listener;

    public void setListener(OnItemCilckListener listener) {
        this.listener = listener;
    }


}
