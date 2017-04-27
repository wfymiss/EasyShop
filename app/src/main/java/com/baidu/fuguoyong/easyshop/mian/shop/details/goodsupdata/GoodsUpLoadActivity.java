package com.baidu.fuguoyong.easyshop.mian.shop.details.goodsupdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.commons.ImageUtils;
import com.baidu.fuguoyong.easyshop.commons.MyFileUtils;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.baidu.fuguoyong.easyshop.mian.user.login.Person.PicWindow;
import com.baidu.fuguoyong.easyshop.modle.GoodsUpLoad;
import com.baidu.fuguoyong.easyshop.modle.ImageItem;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsUpLoadActivity extends MvpActivity<GoodsUpLoadView, GoodsUpLoadPresenter> implements GoodsUpLoadView {


    @BindView(R.id.tv_goods_delete)
    TextView tvGoodsDelete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_goods_name)
    EditText etGoodsName;
    @BindView(R.id.et_goods_price)
    EditText etGoodsPrice;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.btn_goods_type)
    Button btnGoodsType;
    @BindView(R.id.et_goods_describe)
    EditText etGoodsDescribe;
    @BindView(R.id.btn_goods_load)
    Button btnGoodsLoad;
    private GoodsUpLoadAdapter adapter;
    private PicWindow picWindow;
    private final String[] goods_type = {"家用", "电子", "服饰", "玩具", "图书", "礼品", "其它"};
    /*商品种类为自定义*/
    private final String[] goods_type_num = {"household", "electron", "dress", "toy", "book", "gift", "other"};
    private ActivityUtils activityUtils;
    private String str_goods_name;//商品名
    private String str_goods_price;//商品价格
    private String str_goods_type = goods_type_num[0];//商品类型（默认家用)
    private String str_goods_describe;//商品描述
    // 模式一为普通
    public static final int MODE_DONE = 1;
    // 模式2为删除
    public static final int MODE_DELETE = 2;
    private int title_mode = MODE_DONE;
    private ArrayList<ImageItem> list = new ArrayList<>();
    private ProgressDialog show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initview();
    }

    private void initview() {

        picWindow = new PicWindow(this, mListener);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        // 设置默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //获取缓存文件夹中的文件
        list = getFilePhoto();
        adapter = new GoodsUpLoadAdapter(list, this);
        adapter.setList(mlistener);
        recyclerView.setAdapter(adapter);
        //商品名称，价格，描述输入的监听
        etGoodsDescribe.addTextChangedListener(mWatcher);
        etGoodsName.addTextChangedListener(mWatcher);
        etGoodsPrice.addTextChangedListener(mWatcher);
    }

    //商品名称，价格，描述输入的监听
    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String mGoodsPrice = etGoodsPrice.getText().toString();
            String mGoodsName = etGoodsName.getText().toString();
            String mGoodsDescribe = etGoodsDescribe.getText().toString();
            boolean canUp = !(TextUtils.isEmpty(mGoodsPrice)
                    || TextUtils.isEmpty(mGoodsName)
                    || TextUtils.isEmpty(mGoodsDescribe));
            btnGoodsLoad.setEnabled(canUp);
        }


    };
    GoodsUpLoadAdapter.OnItemClickedListener mlistener = new GoodsUpLoadAdapter.OnItemClickedListener() {
        @Override
        public void onAddClicked() {
            //无图，单击，添加图片
            if (picWindow != null && picWindow.isShowing()) {
                picWindow.dismiss();
            } else if (picWindow != null) {
                picWindow.show();
            }
        }

        @Override
        public void onPhotoClicked(ImageItem photo, ImageView imageView) {
            //有图，单击，跳转到图片展示页
            Intent intent = new Intent(GoodsUpLoadActivity.this, GoodsShowActivity.class);
            intent.putExtra("images", photo.getBitmap());
            intent.putExtra("width", imageView.getWidth());
            intent.putExtra("height", imageView.getHeight());
            startActivity(intent);
        }

        @Override
        public void onLongClicked() {
            //有图，长摁，执行删除相关代码
            title_mode = MODE_DELETE;
            // 删除的tv 可见
            tvGoodsDelete.setVisibility(View.VISIBLE);

        }
    };
    //图片选择弹窗内的监听事件
    PicWindow.Listener mListener = new PicWindow.Listener() {
        // 来自相册
        @Override
        public void toGallery() {
            CropHelper.clearCachedCropFile(mHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(mHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);

        }

        //  来自照相机
        @Override
        public void toCamera() {
            CropHelper.clearCachedCropFile(mHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(mHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };
    // 图片裁剪的handler
    CropHandler mHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //需求：裁剪完成后，吧图片保存为bitmap，并且保存到sd中，并且展示出来
            //文件名：就是用系统当前时间，不重复
            String Filename = String.valueOf(System.currentTimeMillis());
            //拿到BitMap（ImageUtils）
            Bitmap bitmap = ImageUtils.readDownsampledImage(uri.getPath(), 1080, 1920);
            // 保存到sd卡中
            MyFileUtils.saveBitmap(bitmap, Filename);
            // 展示出来
            ImageItem imageItem = new ImageItem();
            imageItem.setImagePath(Filename + ".JPEG");
            imageItem.setBitmap(bitmap);
            adapter.add(imageItem);
            adapter.notifyData();
        }

        @Override
        public void onCropCancel() {

        }

        @Override
        public void onCropFailed(String message) {

        }

        @Override
        public CropParams getCropParams() {
            CropParams mPatams = new CropParams();
            mPatams.aspectX = 400;
            mPatams.aspectY = 400;
            return mPatams;
        }

        @Override
        public Activity getContext() {
            return GoodsUpLoadActivity.this;
        }
    };

    public ArrayList<ImageItem> getFilePhoto() {
        ArrayList<ImageItem> list = new ArrayList<>();
        // 拿到所有文件的图片
        File[] files = new File(MyFileUtils.SD_PATH).listFiles();
        if (files != null) {
            for (File file : files) {
                //解码file拿到bitmap
                Bitmap mBitmap = BitmapFactory.decodeFile(MyFileUtils.SD_PATH + file.getName());
                ImageItem item = new ImageItem();
                item.setImagePath(file.getName());
                item.setBitmap(mBitmap);
                list.add(item);
            }
        }
        return list;
    }
    //重写返回方法，实现点击返回改变模式


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (title_mode == MODE_DONE) {
            //删除缓存
            deleteCache();
            finish();
        } else if (title_mode == MODE_DELETE) {
            //转变模式--改为普通模式
            changeModeActivity();
        }
    }

    //转变模式--改为普通模式
    private void changeModeActivity() {
        //判断，根据adapter判断当前模式是否是可删除模式
        if (adapter.getMode() == GoodsUpLoadAdapter.MODE_MULTI_SELECT) {
            // 删除tv不了见
            tvGoodsDelete.setVisibility(View.GONE);
            //activity 模式改变
            title_mode = MODE_DONE;
            //adapter模式改变
            adapter.changeMode(GoodsUpLoadAdapter.MODE_NORMAL);
            for (int i = 0; i < adapter.getList().size(); i++) {
                adapter.getList().get(i).setIsCheck(false);
            }
        }
    }

    // 点击删除， 商品类型，上传监听
    @OnClick({R.id.tv_goods_delete, R.id.btn_goods_type, R.id.btn_goods_load})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_delete:
                //点击删除
                ArrayList<ImageItem> del_list = adapter.getList();
                int num = del_list.size();
                for (int i = num - 1; i >= 0; i--) {
                    // 删除
                    if (del_list.get(i).isCheck()) {
                        MyFileUtils.delFile(del_list.get(i).getImagePath());
                        del_list.remove(i);
                    }

                }
                this.list = del_list;
                adapter.notifyData();
                title_mode = MODE_DONE;
                break;
            //点击商品类别
            case R.id.btn_goods_type:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("商品类型");
                alertDialog.setItems(goods_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //显示商品类型
                        tvGoodsType.setText(goods_type[which]);
                        //拿到商品类型的英文描述（用于上传）
                        str_goods_type = goods_type_num[which];

                    }
                });
                alertDialog.create().show();

                break;
            //点击上传
            case R.id.btn_goods_load:
                if (adapter.getsize() == 0) {
                    activityUtils.showToast("最少有一张商品图片");
                    return;
                }
                presenter.UpData(setGoodsInfo(), list);
                break;


        }
    }

    private GoodsUpLoad setGoodsInfo() {
        GoodsUpLoad goodsLoad = new GoodsUpLoad();
        goodsLoad.setName(str_goods_name);
        goodsLoad.setPrice(str_goods_price);
        goodsLoad.setDescribe(str_goods_describe);
        goodsLoad.setType(str_goods_type);
        goodsLoad.setMaster(CachePreferences.getUser().getName());
        return goodsLoad;
    }

    //删除缓存(删除缓存文件夹中的文件)
    private void deleteCache() {
        for (int i = 0; i < adapter.getList().size(); i++) {
            MyFileUtils.delFile(adapter.getList().get(i).getImagePath());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CropHelper帮助我们处理结果
        CropHelper.handleResult(mHandler, requestCode, resultCode, data);
    }

    @NonNull
    @Override
    public GoodsUpLoadPresenter createPresenter() {
        return new GoodsUpLoadPresenter();
    }

    //############################视图################################

    @Override
    public void showBar() {
        show = ProgressDialog.show(this, "提示", "正在上传...");
    }

    @Override
    public void hipeBar() {
        if (show != null) {
            show.dismiss();
        }
    }

    @Override
    public void upDataSuccese() {
        finish();
    }

    @Override
    public void upDataLoser() {
        activityUtils.showToast("滚");
    }

    @Override
    public void showmsg(String msg) {
        activityUtils.showToast(msg);
    }
}
