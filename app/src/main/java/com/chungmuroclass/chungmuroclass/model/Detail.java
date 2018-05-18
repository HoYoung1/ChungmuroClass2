package com.chungmuroclass.chungmuroclass.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HY on 2018-05-18.
 */

public class Detail {
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

    @SerializedName("students")
    private List<Student> students;

    @SerializedName("lecchecks")
    private List<Lecchecks> lecchecks;

    public class Lecchecks{
        @SerializedName("id")
        private int id;

        @SerializedName("similarity")
        private int similarity;

        @SerializedName("col_index")
        private int col_index;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSimilarity() {
            return similarity;
        }

        public void setSimilarity(int similarity) {
            this.similarity = similarity;
        }

        public int getCol_index() {
            return col_index;
        }

        public void setCol_index(int col_index) {
            this.col_index = col_index;
        }
    }


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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Lecchecks> getLecchecks() {
        return lecchecks;
    }

    public void setLecchecks(List<Lecchecks> lecchecks) {
        this.lecchecks = lecchecks;
    }
}
