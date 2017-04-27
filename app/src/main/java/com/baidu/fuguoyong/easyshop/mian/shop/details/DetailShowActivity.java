package com.baidu.fuguoyong.easyshop.mian.shop.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class DetailShowActivity extends AppCompatActivity {
    private static final String IMAGE = "image";
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    private List<String> list_uri = new ArrayList<>();
    private ArrayList<ImageView> list;
    private DetailAdapter adapter;

    public static Intent getStartIntent(Context context, ArrayList<String> list_uri) {
        Intent intent = new Intent(context, DetailShowActivity.class);
        intent.putExtra(IMAGE, list_uri);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail_info);
        list_uri = getIntent().getStringArrayListExtra(IMAGE);
        list = new ArrayList<ImageView>();

        adapter = new DetailAdapter(list);
        adapter.setListener(new DetailAdapter.OnItemCilckListener() {
            @Override
            public void onItemClick() {
                finish();
            }
        });
        initview();
        viewpager.setAdapter(adapter);
        indicator.setViewPager(viewpager);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    private void initview() {

        for (int i = 0; i < list_uri.size(); i++) {
            ImageView view = new ImageView(this);
            Picasso.with(this)
                    .load(EasyShopApi.IMAGE_URL + list_uri.get(i))
                    .error(R.drawable.user_ico)
                    .placeholder(R.drawable.user_ico).
                    into(view);
            list.add(view);
            adapter.notifyDataSetChanged();

        }
    }
}
