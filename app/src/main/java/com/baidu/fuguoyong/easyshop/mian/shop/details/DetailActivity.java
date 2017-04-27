package com.baidu.fuguoyong.easyshop.mian.shop.details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopApi;
import com.baidu.fuguoyong.easyshop.mian.user.LonginActivity;
import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.baidu.fuguoyong.easyshop.modle.GoodsDetail;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends MvpActivity<DetailView, DetailPresenter> implements DetailView {
    private static final String UUID = "uuid";
    private static final String STATE = "state";
    @BindView(R.id.tv_goods_delete)
    TextView tvGoodsDelete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.tv_detail_name)
    TextView tvDetailName;
    @BindView(R.id.tv_detail_price)
    TextView tvDetailPrice;
    @BindView(R.id.tv_detail_master)
    TextView tvDetailMaster;
    @BindView(R.id.tv_detail_describe)
    TextView tvDetailDescribe;
    @BindView(R.id.btn_detail_message)
    Button btnDetailMessage;
    @BindView(R.id.tv_goods_error)
    TextView tvGoodsError;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    private DetailAdapter adapter;
    private ActivityUtils activityUtils;
    private String str_uuid;
    private ProgressDialog show;
    private User goods_user;
    private int with;

    public static Intent getStateIntent(Context context, String uuid, int State) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(UUID, uuid);
        intent.putExtra(STATE, State);
        return intent;
    }

    // 存放图片的
    private ArrayList<ImageView> list;
    //存放图片路径的
    private ArrayList<String> list_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
        // 获取手机的宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        with = dm.widthPixels;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        list = new ArrayList<>();
        list_uri = new ArrayList<>();
        adapter = new DetailAdapter(list);
        adapter.setListener(new DetailAdapter.OnItemCilckListener() {
            @Override
            public void onItemClick() {
               Intent intent =DetailShowActivity.getStartIntent(getApplicationContext(),list_uri);
              startActivity(intent);
            }
        });
        viewpager.setAdapter(adapter);
        initView();
    }

    private void initView() {
        // 拿到UUid
        str_uuid = getIntent().getStringExtra(UUID);
        // 来自于那个页面
        int btn_show = getIntent().getIntExtra(STATE, 0);
        // 如果1 来自我的页面
        if (btn_show == 1) {
            tvGoodsDelete.setVisibility(View.VISIBLE);// 显示删除
            btnDetailMessage.setVisibility(View.GONE);// 隐藏发送消息
        }
        presenter.getData(str_uuid); // 获取商品详情
    }
    @NonNull
    @Override
    public DetailPresenter createPresenter() {
        return new DetailPresenter();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showbar() {
        show = ProgressDialog.show(this, "提示", "正在获取数据");
    }

    @Override
    public void hidebar() {
        show.dismiss();
    }



    @Override
    public void setimagedata(ArrayList<String> arrayList) {
        list_uri = arrayList;
        // 加载图片
        for (int i = 0; i < list_uri.size(); i++) {
            ImageView view = new ImageView(this);
            Picasso.with(this).load(EasyShopApi.IMAGE_URL + list_uri.get(i))
                    .error(R.drawable.user_ico)
                    .placeholder(R.drawable.user_ico)
                    .into(view);
            // 添加到图片list集合中
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(with, ViewGroup.LayoutParams.WRAP_CONTENT)));
            list.add(view);
            adapter.notifyDataSetChanged();
            //确定viewpager显示页面数量后，创建圆的指示器
            indicator.setViewPager(viewpager);
        }
    }

    @Override
    public void setdata(GoodsDetail data, User goods_user) {
        this.goods_user =goods_user;
        tvDetailDescribe.setText(data.getDescription());


    }

    @Override
    public void showError() {
        tvGoodsError.setText(View.VISIBLE);
        toolbar.setTitle("商品过期");
    }

    @Override
    public void showMessage(String msg) {
          activityUtils.showToast(msg);
    }

    @Override
    public void showDelete() {
         finish();

    }

    // 点击事件
    @OnClick({R.id.tv_goods_delete, R.id.btn_detail_message})
    public void onViewClicked(View view) {
        //判断状态
        if (CachePreferences.getUser().getName() == null) {
            activityUtils.startActivity(LonginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_goods_delete: //执行删除操作
                //弹出一个警告，询问用户是否要删除
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.goods_title_delete);
                builder.setMessage(R.string.goods_info_delete);
                //设置确认按钮，点击删除
                builder.setPositiveButton(R.string.goods_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         presenter.delete(str_uuid);
                    }
                });
                //设置取消按钮
                builder.setNegativeButton(R.string.popu_cancle,null);
                builder.create().show();


                break;
            case R.id.btn_detail_message:
                // TODO: 2017/4/21 0021 跳转到环信发消息的页面
                activityUtils.showToast("跳转到环信发消息的页面,待实现");
                break;
        }
    }
}
