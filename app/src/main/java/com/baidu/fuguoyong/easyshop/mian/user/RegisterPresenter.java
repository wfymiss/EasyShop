package com.baidu.fuguoyong.easyshop.mian.user;

import android.util.Log;
import android.widget.Toast;

import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/19.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    public void Register(String username, String paw) {
        // 展示进度条
        getView().showbar();
        EasyShopClient.getInstance().Register(username, paw).enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call,String json) {
                // 隐藏进度条
                getView().unshowBar();
                //  注册成功获取到的数据
                UserResult userResult = new Gson().fromJson(json, UserResult.class);
                if (userResult.getCode() == 1) {
                    getView().showMsg("注册成功");
                    getView().registersucess();
                    // 保存到仓库
                    User user = userResult.getData();
                    CachePreferences.setUser(user);
                } else if (userResult.getCode() == 2) {
                    getView().showMsg("注册失败");
                    getView().registerfail();
                }
            }

            @Override
            public void onFailureUI(Call call, IOException e) {

                Log.e("========", "=========");

//                getView().showMsg("失败");
            }
        });
    }

}
