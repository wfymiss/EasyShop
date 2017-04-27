package com.baidu.fuguoyong.easyshop.mian.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.me.persongoods.PersongoodActivity;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopApi;
import com.baidu.fuguoyong.easyshop.mian.shop.details.goodsupdata.GoodsUpLoadActivity;
import com.baidu.fuguoyong.easyshop.mian.user.LonginActivity;
import com.baidu.fuguoyong.easyshop.mian.user.RegisterActivity;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.baidu.fuguoyong.easyshop.mian.user.login.Person.PersonActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_person_info)
    TextView tvPersonInfo;
    @BindView(R.id.tv_person_goods)
    TextView tvPersonGoods;
    @BindView(R.id.tv_goods_upload)
    TextView tvGoodsUpload;
    @BindView(R.id.iv_user_head)
    ImageView imageView;
    Unbinder unbinder;
    private ActivityUtils activityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        activityUtils = new ActivityUtils(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CachePreferences.getUser().getName() == null) {

            return;
        }
        if (CachePreferences.getUser().getNick_name() == null) {
            tvLogin.setText("请输入昵称");
        } else {
            tvLogin.setText(CachePreferences.getUser().getNick_name());
        }
        Picasso.with(getContext()).load(EasyShopApi.IMAGE_URL + CachePreferences.getUser().getHead_Image())
                .error(R.drawable.user_ico)
                .placeholder(R.drawable.user_ico)
                .into(imageView);
    }

    @OnClick({R.id.iv_user_head, R.id.tv_login, R.id.tv_person_info, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onclick(View view) {
        //需要判断用户是否登录，从而决定跳转的位置
        if (CachePreferences.getUser().getName() == null) {
            activityUtils.startActivity(LonginActivity.class);
            return;
        }

        switch (view.getId()) {
            case R.id.iv_user_head:
            case R.id.tv_login:
            case R.id.tv_person_info:
                //跳转到个人信息页面
                activityUtils.startActivity(PersonActivity.class);
                break;
            case R.id.tv_person_goods:
                //跳转到我的商品页面
                activityUtils.startActivity(PersongoodActivity.class);
                break;
            case R.id.tv_goods_upload:
                //跳转到商品上传的页面
                activityUtils.startActivity(GoodsUpLoadActivity.class);
                break;


        }
    }
}
