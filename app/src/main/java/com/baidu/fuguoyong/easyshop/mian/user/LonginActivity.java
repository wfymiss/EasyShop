package com.baidu.fuguoyong.easyshop.mian.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.MainActivity;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.login.LoginPresenter;
import com.baidu.fuguoyong.easyshop.mian.user.login.LoginView;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LonginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;
    private ActivityUtils activityUtils;
    private String mUserame;
    private String mPassword;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        //初始化视图
        init();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void init() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //文本区域的监听
            etPwd.addTextChangedListener(mWatcher);
            etUsername.addTextChangedListener(mWatcher);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mUserame = etUsername.getText().toString();
            mPassword = etPwd.getText().toString();
            boolean canRegister = !(TextUtils.isEmpty(mUserame) || TextUtils.isEmpty(mPassword));

            btnLogin.setEnabled(canRegister);
        }
    };

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                showbar();
                 presenter.login(mUserame,mPassword);
                break;
            case R.id.tv_register:
                activityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }

    //----------------------视图--------------------------------------
    @Override
    public void showbar() {

        dialog = ProgressDialog.show(LonginActivity.this, "提示", "正在登录....");
    }


    @Override
    public void unshowBar() {

        if (dialog != null) {
            Log.e("dialog22222222222",""+dialog);
            dialog.dismiss();
        }
    }
    @Override
    public void registersucess() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void registerfail() {
        etUsername.setText("");
    }

    @Override
    public void showMsg(String msg) {
activityUtils.showToast(msg);
    }


}
