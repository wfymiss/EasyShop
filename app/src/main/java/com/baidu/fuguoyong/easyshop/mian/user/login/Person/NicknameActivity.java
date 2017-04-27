package com.baidu.fuguoyong.easyshop.mian.user.login.Person;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.commons.RegexUtils;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.mian.user.UserResult;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/21.
 */

public class NicknameActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.tv_nickname_error)
    TextView tvNicknameError;
    @BindView(R.id.btn_save)
    Button btnSave;
    private ActivityUtils activityUtils;
    private String nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
        // 设置toobar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() ==android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.btn_save)
    public  void  onclick(){
        nickname = etNickname.getText().toString();
        if (RegexUtils.verifyNickname(nickname) != RegexUtils.VERIFY_SUCCESS){
             activityUtils.showToast("不符合规则");
            return;
        }

        init();
    }
  // 修改昵称
    private void init() {
       // 从本地仓库拿到数据
        User user = CachePreferences.getUser();
             user.setNick_name(nickname);
        Call call = EasyShopClient.getInstance().upNickname(user);
        call.enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call, String json) {
                UserResult userResult = new Gson().fromJson(json,UserResult.class);
                if (userResult.getCode() != 1){
                    activityUtils.showToast("失败");
                    return;
                }
                CachePreferences.setUser(userResult.getData());
                activityUtils.showToast("修改成功");
                finish();
            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                     activityUtils.showToast(e.getMessage());
            }
        });
    }
}
