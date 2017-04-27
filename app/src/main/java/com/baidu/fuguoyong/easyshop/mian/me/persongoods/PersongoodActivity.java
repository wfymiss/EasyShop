package com.baidu.fuguoyong.easyshop.mian.me.persongoods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.shop.ShopAdapter;
import com.baidu.fuguoyong.easyshop.mian.shop.ShopView;
import com.baidu.fuguoyong.easyshop.mian.shop.details.DetailActivity;
import com.baidu.fuguoyong.easyshop.mian.user.login.Person.PersonActivity;
import com.baidu.fuguoyong.easyshop.modle.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PersongoodActivity extends MvpActivity<ShopView, PersonPresenter> implements ShopView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;
    @BindView(R.id.tv_load_empty)
    TextView tvLoadEmpty;
    @BindView(R.id.activity_person_goods)
    RelativeLayout activityPersonGoods;
    private ShopAdapter adapter;
    private String pageType = "";//商品类型，空值为全部商品
    private ActivityUtils activityUtils = new ActivityUtils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_goods);
        ButterKnife.bind(this);
        adapter = new ShopAdapter();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        initview();
    }

    private void initview() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter.setListener(new ShopAdapter.onItemClick() {
            @Override
            public void onclick(GoodsInfo goodsInfo) {
                // 我的商品详情页
                Intent intent = DetailActivity.getStateIntent(
                        getApplicationContext(), goodsInfo.getUuid(), 1
                );
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        //初始化RefreshLayout
        refreshLayout.setLastUpdateTimeRelateObject(true);
        refreshLayout.setDurationToCloseHeader(1500);
        refreshLayout.setBackgroundResource(R.color.background);
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
    protected void onStart() {
        super.onStart();
        if (adapter.getItemCount() == 0) {
            refreshLayout.autoRefresh();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id
                .home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_goods_type, menu);
        return true;
    }

    Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_household:
                    presenter.refreshData("household");
                    break;
                case R.id.menu_electron:
                    presenter.refreshData("electron");
                    break;
                case R.id.menu_dress:
                    presenter.refreshData("dress");
                    break;
                case R.id.menu_book:
                    presenter.refreshData("book");
                    break;
                case R.id.menu_toy:
                    presenter.refreshData("toy");
                    break;
                case R.id.menu_gift:
                    presenter.refreshData("gift");
                    break;
                case R.id.menu_other:
                    presenter.refreshData("other");
                    break;
            }
            return false;
        }
    };

    @NonNull
    @Override
    public PersonPresenter createPresenter() {
        return new PersonPresenter();
    }

    @Override
    public void showRefresh() {
        tvLoadEmpty.setVisibility(View.GONE);
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        refreshLayout.refreshComplete();
        if (adapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        } else {
            tvLoadError.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showRefreshEnd() {
        refreshLayout.refreshComplete();
        tvLoadEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRefresh() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        adapter.clear();
        if (adapter != null)
            adapter.addData(data);
    }

    @Override
    public void showLoadMoreLoading() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        refreshLayout.refreshComplete();
        if (adapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast("没有更多数据");
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        adapter.addData(data);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
