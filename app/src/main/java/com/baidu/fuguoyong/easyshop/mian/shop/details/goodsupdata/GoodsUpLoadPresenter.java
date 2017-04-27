package com.baidu.fuguoyong.easyshop.mian.shop.details.goodsupdata;

import android.util.Log;

import com.baidu.fuguoyong.easyshop.commons.MyFileUtils;
import com.baidu.fuguoyong.easyshop.mian.netclient.EasyShopClient;
import com.baidu.fuguoyong.easyshop.mian.user.UiCallback;
import com.baidu.fuguoyong.easyshop.modle.GoodsUpLoad;
import com.baidu.fuguoyong.easyshop.modle.GoodsUpLoadResult;
import com.baidu.fuguoyong.easyshop.modle.ImageItem;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/25.
 */

public class GoodsUpLoadPresenter extends MvpNullObjectBasePresenter<GoodsUpLoadView> {
    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null){
            call.cancel();
        }
    }

    //上传 商品

    public  void  UpData(GoodsUpLoad goodsUpLoad, List<ImageItem>list){
       getView().showBar();
        call = EasyShopClient.getInstance().UpGoods(goodsUpLoad,getFiles(list));
        call.enqueue(new UiCallback() {
            @Override
            public void onResponseUi(Call call, String json) {
                 getView().hipeBar();
                GoodsUpLoadResult result = new Gson().fromJson(json,GoodsUpLoadResult.class);
                Log.e("222222222","--------"+result.getCode()) ;


                getView().showmsg(result.getMessage());
                if (result.getCode()== 1){
                    getView().upDataSuccese();
                }
            }

            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hipeBar();
                getView().showmsg(e.getMessage());
            }
        });

    }


    //根据imageItem（图片路径）获取图片文件
    private ArrayList<File> getFiles(List<ImageItem> list){
        ArrayList<File> files = new ArrayList<>();
        for (ImageItem imageItem : list){
            //根据图片路径，拿到图片文件
            File file = new File(MyFileUtils.SD_PATH + imageItem.getImagePath());
            files.add(file);
        }
        return files;
    }






}
