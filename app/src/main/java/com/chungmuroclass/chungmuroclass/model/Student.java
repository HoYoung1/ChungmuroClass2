package com.chungmuroclass.chungmuroclass.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HY on 2018-05-09.
 */

public class Student {

    @SerializedName("id")
    private int id;

    @SerializedName("student_id")
    private String student_id;

    @SerializedName("name")
    private String name;

    @SerializedName("img_url")
    private String img_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
