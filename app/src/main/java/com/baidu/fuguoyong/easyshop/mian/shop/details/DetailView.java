package com.baidu.fuguoyong.easyshop.mian.shop.details;

import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.modle.GoodsDetail;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/23.
 */

public interface DetailView extends MvpView {
    void showbar();

    void hidebar();

    void setimagedata(ArrayList<String> arrayList);

    void setdata(GoodsDetail data, User goods_user);

    void showError();

    void showMessage(String msg);

    void showDelete();
}
