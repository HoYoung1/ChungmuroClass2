package com.chungmuroclass.chungmuroclass.Item;

/**
 * Created by HY on 2018-05-16.
 */

public class ItemLecture {

    public ItemLecture(int id, String professor, String class_name, String class_start, String regDate, String stateLecture) {
        this.id = id;
        this.professor = professor;
        this.class_name = class_name;
        this.class_start = class_start;
        this.regDate = regDate;
        this.stateLecture = stateLecture;
    }

    int id;
    String professor;
    String class_name;
    String class_start;
    String regDate;
    String stateLecture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_start() {
        return class_start;
    }

    public void setClass_start(String class_start) {
        this.class_start = class_start;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getStateLecture() {
        return stateLecture;
    }

    public void setStateLecture(String stateLecture) {
        this.stateLecture = stateLecture;
    }
}
