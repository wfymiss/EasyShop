package com.baidu.fuguoyong.easyshop.mian.shop.details;

import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.modle.GoodsDetail;
import com.baidu.fuguoyong.easyshop.modle.GoodsDetailResult;
import com.baidu.fuguoyong.easyshop.modle.GoodsResult;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/23.
 */

public class DetailPresenter extends MvpNullObjectBasePresenter<DetailView> {

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) {
            call.cancel();
        }
    }

    // 获取商品详情数据
    public void getData(String uuid) {
        getView().showbar();
        call = EasyShopClient.getInstance().getDetailDate(uuid);
        call.enqueue(new UiCallback() {

            @Override
            public void onResponseUi(Call call, String json) {
                getView().hidebar();
                GoodsDetailResult result = new Gson().fromJson(json, GoodsDetailResult.class);
                if (result.getCode() == 1) {
                    // 商品详情
                    GoodsDetail detail = result.getDatas();
                    // 用来存放图片路径的集合
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < detail.getPages().size(); i++) {
                        String pages = detail.getPages().get(i).getUri();
                        list.add(pages);
                    }
                    // 图片集合
                    getView().setimagedata(list);
                    getView().setdata(detail, result.getUser());
                } else {
                    getView().showError();
                }

            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hidebar();
                getView().showMessage(e.getMessage());
            }
        });

    }
    // 删除商品
    public  void  delete(String uuid){
        getView().showbar();
            EasyShopClient.getInstance().getDelete(uuid).enqueue(new UiCallback() {
                @Override
                public void onResponseUi(Call call, String json) {
                    getView().hidebar();
                    GoodsResult result = new Gson().fromJson(json,GoodsResult.class);
                     if (result.getCode() == 1){
                         getView().showDelete();
                         getView().showMessage("删除成功");
                     }

                }

                @Override
                public void onFailureUI(Call call, IOException e) {

                }
            });
    }

}
