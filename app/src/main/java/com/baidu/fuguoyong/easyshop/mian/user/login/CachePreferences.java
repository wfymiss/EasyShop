package com.baidu.fuguoyong.easyshop.mian.user.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.fuguoyong.easyshop.mian.user.User;

/**
 * 对用户信息本地保存
 */
public class CachePreferences {

    private static final String NAME = CachePreferences.class.getSimpleName();
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PWD = "userPwd";
    private static final String KEY_USER_HX_ID = "userHxID";
    private static final String KEY_USER_TABLE_ID = "userUuid";
    private static final String KEY_USER_HEAD_IMAGE = "userHeadImage";
    private static final String KEY_USER_NICKNAME = "userNickName";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private CachePreferences() {
    }

    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void clearAllData() {
        editor.clear();
        editor.apply();
    }

    public static void setUser(User data) {
        editor.putString(KEY_USER_NAME, data.getName());
        editor.putString(KEY_USER_PWD, data.getPassword());
        editor.putString(KEY_USER_HX_ID, data.getHx_Id());
        editor.putString(KEY_USER_TABLE_ID, data.getTable_Id());
       editor.putString(KEY_USER_HEAD_IMAGE, data.getHead_Image());
       editor.putString(KEY_USER_NICKNAME, data.getNick_name());

        editor.apply();
    }

    public static User getUser() {
        User data = new User();
        data.setName(preferences.getString(KEY_USER_NAME, null));
        data.setPassword(preferences.getString(KEY_USER_PWD, null));
        data.setHx_Id(preferences.getString(KEY_USER_HX_ID, null));
        data.setTable_Id(preferences.getString(KEY_USER_TABLE_ID, null));
       data.setHead_Image(preferences.getString(KEY_USER_HEAD_IMAGE, null));
       data.setNick_name(preferences.getString(KEY_USER_NICKNAME, null));
        return data;
    }

}
