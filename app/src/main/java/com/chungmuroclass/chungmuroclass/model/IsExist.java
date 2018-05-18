package com.chungmuroclass.chungmuroclass.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HY on 2018-05-16.
 */

public class IsExist {
    @SerializedName("isExist")
    private Boolean isExist;

    @SerializedName("msg")
    private String msg;


    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
