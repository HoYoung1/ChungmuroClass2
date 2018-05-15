package com.chungmuroclass.chungmuroclass.globals.constant;





public enum URLs
{
	// server
	DOMAIN("http://13.124.131.201"),
    // 포트는 8000
	PORTMAIN(":8000"),

    //모든URL
	MAIN_URL(DOMAIN.getValue() + PORTMAIN.getValue()),

    //이미지 스토리지
    STORAGE("https://s3.amazonaws.com/testawsinhyandorid-userfiles-mobilehub-292406316/jsaS3/"),

    // Join
    JOIN(MAIN_URL.getValue() + "/school/students/join/"),

    // 회원가입
    SIGNUP(MAIN_URL.getValue() + "/school/students/"),


	NOTICE_DETAIL(MAIN_URL.getValue() + "/halla/m/notice/detail");

    private String value;

    private URLs(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}