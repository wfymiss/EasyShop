package com.baidu.fuguoyong.easyshop.mian.user.login.Person;

import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.mian.user.UserResult;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PersonPresenter extends MvpNullObjectBasePresenter<PersonView> {


    public  void  setPhoto(File file){
         getView().showbar();
        EasyShopClient.getInstance().setPhoto(file).enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call,String json) {
                  getView().unshowbar();
                getView().showMsg("成功");
                UserResult userResult = new Gson().fromJson(json,UserResult.class);
                User user =userResult.getData();
                CachePreferences.setUser(user);
                //上传成功，触发UI操作（更新头像）
                getView().updataAvatar(userResult.getData().getHead_Image());
            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showMsg("失败");
            }
        });


    }

}
