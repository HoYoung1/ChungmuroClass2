package com.chungmuroclass.chungmuroclass.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HY on 2018-05-16.
 */

public class Lecture {

    @SerializedName("results")
    private List<Lectures> lectures;

    public List<Lectures> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lectures> lectures) {
        this.lectures = lectures;
    }

    public class Lectures {
        @SerializedName("id")
        private int id;

        @SerializedName("professor")
        private String professor;

        @SerializedName("class_name")
        private String class_name;

        @SerializedName("class_start")
        private String class_start;

        @SerializedName("regDate")
        private String regDate;

        @SerializedName("stateLecture")
        private String stateLecture;

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
}
