package com.baidu.fuguoyong.easyshop.mian.user.login.Person;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/4/20.
 */

public interface PersonView extends MvpView {

    void  showbar();

    void  unshowbar();

    void  showMsg(String msg);
    //用来更新头像
    void updataAvatar(String url);

}
