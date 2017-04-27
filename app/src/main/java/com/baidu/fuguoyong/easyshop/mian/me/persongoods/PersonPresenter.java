package com.baidu.fuguoyong.easyshop.mian.me.persongoods;

import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.shop.ShopView;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.baidu.fuguoyong.easyshop.modle.GoodsDetail;
import com.baidu.fuguoyong.easyshop.modle.GoodsResult;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PersonPresenter extends MvpNullObjectBasePresenter<ShopView> {

    private Call call;
    private int index = 1;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) {
            call.cancel();
        }
    }

    // 加载数据
    public void loadData(String type) {
        getView().showLoadMoreLoading();
        call = EasyShopClient.getInstance().getPersonGood(index, type, CachePreferences.getUser().getName());
        call.enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call, String json) {
                GoodsResult result = new Gson().fromJson(json, GoodsResult.class);
                switch (result.getCode()) {

                    case 1:
                        if (result.getDatas().size() == 0) {
                            getView().showLoadMoreEnd();
                        } else {
                            getView().addMoreData(result.getDatas());
                            getView().showLoadMoreEnd();
                        }
                        index = 2;
                        break;
                }
            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }
        });
    }

    // 刷新数据
    public void refreshData(String type) {
        getView().showRefresh();
        call = EasyShopClient.getInstance().getPersonGood(index, type, CachePreferences.getUser().getName());
        call.enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call, String json) {
                GoodsResult result = new Gson().fromJson(json, GoodsResult.class);
                switch (result.getCode()) {
                    case 1:
                        if (result.getDatas().size() == 0) {
                            getView().showRefreshEnd();
                        } else {
                            getView().addRefreshData(result.getDatas());
                        }
                        index++;
                        break;
                }
            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
                getView().hideRefresh();
            }
        });


    }

}
