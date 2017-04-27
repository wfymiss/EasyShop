package com.baidu.fuguoyong.easyshop.mian.user.login;

import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.mian.user.UserResult;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {


    public void login(String username, String paw) {
        getView().showbar();
        EasyShopClient.getInstance().login(username, paw).enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call,String json) {

                UserResult result = new Gson().fromJson(json, UserResult.class);
                if (result.getCode() == 1) {
                    getView().unshowBar();
                    getView().showMsg("登录成功");
                    User user = result.getData();
                    CachePreferences.setUser(user);
                    getView().registersucess();
                } else {    

                    getView().showMsg("2222222");
                    getView().unshowBar();
                }
            }
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().unshowBar();
                getView().showMsg("登录失败");
            }
        });


    }
}
