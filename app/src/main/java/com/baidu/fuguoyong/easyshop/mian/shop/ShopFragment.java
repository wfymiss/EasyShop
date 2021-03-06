package com.baidu.fuguoyong.easyshop.mian.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.shop.details.DetailActivity;
import com.baidu.fuguoyong.easyshop.modle.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by Administrator on 2017/4/16.
 */

public class ShopFragment extends MvpFragment<ShopView, ShopPresenter> implements ShopView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;//刷新加载的控件
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;
    Unbinder unbinder;
    private ActivityUtils activityUtils;
    private ShopAdapter shopAdapter;
    //获取商品时，商品类型，获取全部商品时为空
    private String pageType = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        shopAdapter = new ShopAdapter();
        activityUtils = new ActivityUtils(this);
        return view;
    }


    //  -----------------------------视图----------------------------------------
    @Override
    public ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        //初始化RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(shopAdapter);
        shopAdapter.setListener(new ShopAdapter.onItemClick() {
            @Override
            public void onclick(GoodsInfo goodsInfo) {
                    Intent intent =DetailActivity.getStateIntent(getContext(),
                            goodsInfo.getUuid(),0);
                startActivity(intent);
            }
        });
        //初始化RefreshLayout
        //使用本对象作为key，用来记录上一次刷新的事件，如果两次下拉刷新间隔太近，不会触发刷新方法
        refreshLayout.setLastUpdateTimeRelateObject(this);
        //设置刷新时显示的背景色
        refreshLayout.setBackgroundResource(R.color.background);
        //关闭header所需要消耗的时长
        refreshLayout.setDurationToCloseHeader(1500);
        //实现刷新，加载回调
        refreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //刷新一下数据
        if (shopAdapter.getItemCount() == 0) {
            refreshLayout.autoRefresh();
        }
    }

    @OnClick(R.id.tv_load_error)
    public void onclick() {
        //自动刷新
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //  #################################    视图接口相关实现   ####################
    @Override
    public void showRefresh() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        // 停止刷新
        refreshLayout.refreshComplete();
        // 判断是否拿到数据
        if (shopAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        //显示刷新错误提示
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast(getResources().getString(R.string.refresh_more_end));
        //停止刷新
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        //停止刷新
        refreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        //数据清空
        shopAdapter.clear();
        if (data != null) {
            shopAdapter.addData(data);
        }
    }

    @Override
    public void showLoadMoreLoading() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        //停止刷新 判断是否拿到数据
        refreshLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        //显示加载错误提示
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        shopAdapter.addData(data);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
