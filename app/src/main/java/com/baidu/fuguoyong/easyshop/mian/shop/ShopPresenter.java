package com.baidu.fuguoyong.easyshop.mian.shop;

import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.modle.GoodsResult;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ShopPresenter extends MvpNullObjectBasePresenter<ShopView> {
    private int indext = 1;
    private Call call;

    //  刷新数据
    void refreshData(String type) {
        getView().showRefresh();
        call = EasyShopClient.getInstance().getData(1, type);
        call.enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call, String json) {
                GoodsResult result = new Gson().fromJson(json, GoodsResult.class);
                switch (result.getCode()) {
                    //成功
                    case 1:
                        // 没有商品
                        if (result.getDatas().size() == 0) {
                            getView().showRefreshEnd();
                        } else {
                            getView().addRefreshData(result.getDatas());
                            getView().showRefreshEnd();
                        }
                        indext = 2;
                        break;
                }
            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showMessage(e.getMessage());
            }
        });
    }

    //加载更多
    public void loadData(String type) {
        getView().showLoadMoreLoading();
        Call call = EasyShopClient.getInstance().getData(indext, type);
        call.enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call, String json) {
                GoodsResult result = new Gson().fromJson(json, GoodsResult.class);
                if (result.getDatas().size() == 0) {
                    getView().showLoadMoreEnd();
                } else {
                    getView().addMoreData(result.getDatas());
                    getView().showLoadMoreEnd();
                }
                indext++;
            }
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showMessage(e.getMessage());
            }
        });

    }


}
