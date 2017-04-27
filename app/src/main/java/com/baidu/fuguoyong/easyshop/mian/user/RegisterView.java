package com.baidu.fuguoyong.easyshop.mian.user;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/19.
 */

public interface RegisterView extends MvpView{
    // 显示进度条
     void showbar();
    // 隐藏进度条
    void  unshowBar();
    // 注册成功
    void registersucess();
    // 注册失败
    void  registerfail();
    // 帐号或者密码错误
    void showMsg(String msg);



}
