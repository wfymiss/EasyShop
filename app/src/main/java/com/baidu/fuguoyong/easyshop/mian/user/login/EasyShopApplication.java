package com.baidu.fuguoyong.easyshop.mian.user.login;

import android.app.Application;

/**
 * Created by Administrator on 2017/4/20.
 */

public class EasyShopApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CachePreferences.init(this);
    }
}
