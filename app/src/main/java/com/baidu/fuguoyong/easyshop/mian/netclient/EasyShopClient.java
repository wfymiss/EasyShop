package com.baidu.fuguoyong.easyshop.mian.netclient;

import com.baidu.fuguoyong.easyshop.R;
import com.baidu.fuguoyong.easyshop.mian.user.User;
import com.baidu.fuguoyong.easyshop.mian.user.login.CachePreferences;
import com.baidu.fuguoyong.easyshop.modle.GoodsUpLoad;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/4/17.
 */

public class EasyShopClient {
    private static EasyShopClient easyShopClient;
    private final OkHttpClient okHttpClient;

    private EasyShopClient() {
        //初始化日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设置拦截级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                //添加日志拦截器
                .addInterceptor(httpLoggingInterceptor)
                .build();

    }

    public static EasyShopClient getInstance() {
        if (easyShopClient == null) {
            easyShopClient = new EasyShopClient();
        }
        return easyShopClient;
    }
    // 登录
    public Call login(String username, String paw) {

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", paw)
                .build();
        Request request = new Request.Builder()
                .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=login")
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    // 注册
    public Call Register(String username, String paw) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", paw)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=register")
                .build();
        return okHttpClient.newCall(request);
    }

    // 更新头像
    public Call setPhoto(File file) {

        Gson gson = new Gson();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //传一个用户类JSON字符串格式
                .addFormDataPart("user", gson.toJson(CachePreferences.getUser()))
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file))
                .build();

        Request request = new Request.Builder()
                .post(requestBody)
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .build();
        return okHttpClient.newCall(request);

    }

    //  获取数据
    public Call getData(int pageNo, String type) {
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNo))
                .add("type", type)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(EasyShopApi.BASE_URL + EasyShopApi.GETGOODS)
                .build();
        return okHttpClient.newCall(request);
    }

    // 更改呢称
    public Call upNickname(User user) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", new Gson().toJson(user))
                .build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    public Call getDetailDate(String uuid) {
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid", uuid)
                .build();
        //获取商品详情页
        Request request = new Request.Builder()
                .post(requestBody)
                .url(EasyShopApi.BASE_URL + EasyShopApi.DETAIL)
                .build();
        return okHttpClient.newCall(request);
    }

    // 获取个人数据
    public Call getPersonGood(int pageNo, String type, String master) {

        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo", String.valueOf(pageNo))
                .add("master", master)
                .add("type", type)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GETGOODS)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

    //删除
    public Call getDelete(String uuid) {

        RequestBody requestBody = new FormBody.Builder()
                .add("uuid", uuid)
                .build();
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.DELETE)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }
  //  上传图片
    public Call UpGoods(GoodsUpLoad goodsUpLoad, ArrayList<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("good", new Gson().toJson(goodsUpLoad));
        //将所有图片文件添加进来
        for (File file : files) {
            builder.addFormDataPart("image", file.getName(),
                    RequestBody.create(MediaType.parse("image/png"), file));
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }

}
