package com.baidu.fuguoyong.easyshop.mian.user;

import com.google.gson.annotations.SerializedName;

/**
 * "code": 1,                   //结果码
 * "msg": "succeed",                //描述信息
 * "data": {
 * "username": "xc62",
 * "name": "yt59856b15cf394e7b84a7d48447d16098",
 * "uuid": "0F8EC12223174657B2E842076D54C361",
 * "password": "123456"
 * }
 */

public class UserResult {

    private int code;
    @SerializedName("msg")
    private String message;
    private User data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
