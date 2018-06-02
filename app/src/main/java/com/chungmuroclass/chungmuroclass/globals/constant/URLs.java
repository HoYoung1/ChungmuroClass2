package com.chungmuroclass.chungmuroclass.globals.constant;





public enum URLs
{
	// server
	DOMAIN("http://13.124.131.201"),
    //DOMAIN("http://127.0.0.1"),
    // 포트는 8000
	PORTMAIN(":8000"),

    //모든URL
	MAIN_URL(DOMAIN.getValue() + PORTMAIN.getValue()),

    //이미지 스토리지
    //STORAGE("https://s3.amazonaws.com/testawsinhyandorid-userfiles-mobilehub-292406316/jsaS3/"),
    //STORAGE("https://s3.amazonaws.com/testawsinhyandorid-userfiles-mobilehub-292406316/jsaS3/"),
    STORAGE("https://s3-ap-northeast-1.amazonaws.com/chungmuroclass-userfiles-mobilehub-486279433/"),

    // Join
    JOIN(MAIN_URL.getValue() + "/school/students/join/"),

    // 회원가입
    SIGNUP(MAIN_URL.getValue() + "/school/students/"),

    // 얼굴 이미지 수정
    FACEIMG_CHANGE(MAIN_URL.getValue() + "/school/students/changeimg/"),

    // 강의목록
    LECTURES(MAIN_URL.getValue() + "/school/lectures/?page="),

    // 수업에 참가하기전 참여할껀지 묻는거
    STUDENT_ISTAKEN(MAIN_URL.getValue() + "/school/lectures/istaken/"),

    // 수업에 참여하기
    TAKE_CLASS(MAIN_URL.getValue() + "/school/lectures/take/"),

    // 강의 세부정보
    LECTURE_DETAIL(MAIN_URL.getValue() + "/school/lectures/");

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