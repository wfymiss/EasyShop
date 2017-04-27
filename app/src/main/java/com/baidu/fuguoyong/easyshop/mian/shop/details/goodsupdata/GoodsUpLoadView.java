package com.baidu.fuguoyong.easyshop.mian.shop.details.goodsupdata;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface GoodsUpLoadView extends MvpView {
    void  showBar();
    void  hipeBar();
    void  upDataSuccese();
    void  upDataLoser();
    void showmsg( String msg);
}
