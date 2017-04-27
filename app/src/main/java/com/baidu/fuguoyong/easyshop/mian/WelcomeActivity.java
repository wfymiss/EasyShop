package com.baidu.fuguoyong.easyshop.mian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mActivityUtils = new ActivityUtils(this);

        //判断用户是否登录，自动登录
        if (CachePreferences.getUser().getName() != null) {
            mActivityUtils.startActivity(MainActivity.class);
            finish();
            return;
        }


        Timer timer = new Timer();

         timer.schedule(new TimerTask() {
             @Override
             public void run() {
                //  更新ui
                 mActivityUtils.startActivity(MainActivity.class);
                 finish();
             }
         },1500);

    }
}
