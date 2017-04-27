package com.baidu.fuguoyong.easyshop.mian.user.login.Person;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.MainActivity;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopApi;
import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends MvpActivity<PersonView, PersonPresenter> implements PersonView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    @BindView(R.id.activity_person)
    RelativeLayout activityPerson;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;//用户头像
    private ActivityUtils activityUtils;
    private List<ItemShow> list = new ArrayList<>();
    private ProgressDialog show;
    private PersonAdapter adapter;
    private PicWindow picWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        adapter = new PersonAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        updataAvatar(CachePreferences.getUser().getHead_Image());
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        init();
        adapter.notifyDataSetChanged();
    }

    private void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow("用户名", user.getName()));
        list.add(new ItemShow("昵称", user.getNick_name()));
        list.add(new ItemShow("环信ID", user.getHx_Id()));
    }

    //   返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {

                case 0:
                    activityUtils.showToast(getResources().getString(R.string.username_update));
                    break;
                case 1:
                    activityUtils.startActivity(NicknameActivity.class);
                    break;
                case 2:
                    activityUtils.showToast(getResources().getString(R.string.id_update));
                    break;


            }
        }
    };

    // -----------------------------------------------------------------


    @NonNull
    @Override
    public PersonPresenter createPresenter() {
        return new PersonPresenter();
    }

    @Override
    public void showbar() {
        show = ProgressDialog.show(this, "提示", "正在上传");
    }

    @Override
    public void unshowbar() {
        show.dismiss();
    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void updataAvatar(String url) {
        Picasso.with(this).load(EasyShopApi.IMAGE_URL + url)
                .error(R.drawable.user_ico)
                .placeholder(R.drawable.user_ico)
                .into(ivUserHead);
    }

    @OnClick({R.id.btn_login_out, R.id.activity_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_out:
                //清空本地配置
                CachePreferences.clearAllData();
                // 清除所有旧activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.activity_person:
                // 头像来源于相册或者 相机
                if (picWindow == null) {
                    picWindow = new PicWindow(this, listener);
                }
                if (picWindow.isShowing()) {
                    picWindow.dismiss();
                    return;
                }
                picWindow.show();
                break;
        }
    }

    //图片来源选择弹窗的监听
    PicWindow.Listener listener = new PicWindow.Listener() {
        @Override
        public void toGallery() {
            //从相册中选择
            //清空裁剪的缓存
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);


        }

        @Override
        public void toCamera() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);

        }
    };
    //图片裁剪的handler
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //图片裁剪结束后
            //通过uri拿到图片文件
            File file = new File(uri.getPath());
            //业务类，上传头像
            presenter.setPhoto(file);
        }

        @Override
        public void onCropCancel() {
//停止裁剪触发
        }

        @Override
        public void onCropFailed(String message) {
            //裁剪失败
        }

        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 400;
            cropParams.aspectY = 400;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return PersonActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_login_out)
    public void onViewClicked() {
        CachePreferences.clearAllData();
        System.exit(0);
    }
}
