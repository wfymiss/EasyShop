package com.baidu.fuguoyong.easyshop.mian.user;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.commons.RegexUtils;
import com.baidu.fuguoyong.easyshop.mian.MainActivity;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpActivity;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwdAgain)
    EditText etPwdAgain;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.activity_register)
    RelativeLayout activityRegister;
    private String mPwdAgain;
    private String mPassword;
    private String mUsername;
    private ActivityUtils activityUtils;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        init();
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    private void init() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        etPwd.addTextChangedListener(mWatcher);
        etUsername.addTextChangedListener(mWatcher);
        etPwdAgain.addTextChangedListener(mWatcher);
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {
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
            mUsername = etUsername.getText().toString();
            mPassword = etPwd.getText().toString();
            mPwdAgain = etPwdAgain.getText().toString();

            boolean canreister = !(TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mUsername));
            btnRegister.setEnabled(canreister);

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_register)
    public void getRegister() {
        if (RegexUtils.verifyUsername(mUsername) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast(R.string.username_rules);
            return;
        } else if (RegexUtils.verifyPassword(mPassword) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast(R.string.password_rules);
            return;
        } else if (!TextUtils.equals(mPassword, mPwdAgain)) {
            Log.e("mPassword-------",mPassword);
            Log.e("mPwdAgain--------",mPwdAgain);
            activityUtils.showToast(R.string.username_equal_pwd);
            return;
        }

            presenter.Register(mUsername,mPassword);

    }
    // -----------------------------视图实现----------------------------------

    @Override
    public void showbar() {
        progressDialog = ProgressDialog.show(this, "提示", "正在注册中，请稍后~");
    }

    @Override
    public void unshowBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void registersucess() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void registerfail() {

    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }
}
