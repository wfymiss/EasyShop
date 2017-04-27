package com.baidu.fuguoyong.easyshop.mian;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.commons.ActivityUtils;
import com.baidu.fuguoyong.easyshop.mian.me.MeFragment;
import com.baidu.fuguoyong.easyshop.mian.shop.ShopFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_toobar)
    Toolbar mainToobar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindViews({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    TextView[] textviews;
    private boolean isExit = false;
    private ActivityUtils activityUtils;
//    private FragmentManager fragentmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);
        // 初始化视图
        initView();
    }

    private void initView() {
        viewpager.setAdapter(mPagerAdapter);

        textviews[0].setSelected(true);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < textviews.length; i++) {
                    textviews[i].setSelected(false);
                }
                //更改title
                mainTitle.setText(textviews[position].getText());
                textviews[position].setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private FragmentStatePagerAdapter mPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                case 1:
                    return new UnLoginFragment();
                case 2:
                    return new UnLoginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    @OnClick({R.id.tv_me, R.id.tv_shop, R.id.tv_mail_list, R.id.tv_message})
    public void onclick(@Nullable TextView tv) {
        for (int i = 0; i < textviews.length; i++) {
            textviews[i].setSelected(false);
            textviews[i].setTag(i);
        }
            tv.setSelected(true);

        viewpager.setCurrentItem((Integer) tv.getTag(),false);
        mainTitle.setText(textviews[(Integer) tv.getTag()].getText());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isExit){
            isExit =true;
            activityUtils.showToast("退出");
            viewpager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit =false;
                }
            },2000);
        }else {
            finish();
        }
    }
}
