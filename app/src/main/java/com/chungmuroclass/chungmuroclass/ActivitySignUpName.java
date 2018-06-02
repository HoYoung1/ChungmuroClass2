package com.chungmuroclass.chungmuroclass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chungmuroclass.chungmuroclass.globals.Globals;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;
import com.chungmuroclass.chungmuroclass.model.Student;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivitySignUpName extends AppCompatActivity {

    private TextView txtComplete;
    private EditText edtName;
    private RelativeLayout main_waiting_cover;

    private Context mContext;
    private String student_id;
    private String userName;
    private String imgURL;

    private Gson gson;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_name);


        Intent intent = getIntent();
        student_id = intent.getStringExtra("student_id");
        userName = intent.getStringExtra("userName");
        imgURL = intent.getStringExtra("imgURL");

        setLayout();
        setInit();
    }

    private void setInit() {
        mContext = this;
        gson = new Gson();

        edtName.setText(userName);
        //sns에서 따온 이름을 hint로 넣자

        txtComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPostNameWithImg();
            }
        });

    }

    private void doPostNameWithImg() {

        //1. 이름 항목검사
        if (edtName.getText().toString().equals("")) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }


        //2. 이름에 공백이있는지 검사
        if (spaceCheck(edtName.getText().toString())){
            Toast.makeText(mContext, "띄어쓰기가있으면 안됩니다 ㅠ", Toast.LENGTH_SHORT).show();
            return;
        }

        //3. 이름 입력했다면 서버로 쏴야지
        try {
            goSignUp(URLs.SIGNUP.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void goSignUp(String url) throws IOException {
        main_waiting_cover.setVisibility(View.VISIBLE);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();


        params.put("student_id", student_id);
        params.put("name", edtName.getText().toString());
        params.put("img_url", imgURL);

        Log.d("호영아", "뭐쏘는지보자");
        Log.d("호영아student_id:", student_id);
        Log.d("호영아name:", edtName.getText().toString());
        Log.d("호영아img_url:", imgURL);


        JSONObject parameter = new JSONObject(params);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("호영아", call.request().body().toString());
                Log.d("호영아", "에러 ㅠ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "서버와 통신실패", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                main_waiting_cover.setVisibility(View.GONE);
                            }
                        });

                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("호영아", "성공^^;");
                Log.d("호영아", result);
                //TO DO
                // akey값 가져와서 sharedpreference 저장
                // 원래는 SignUpNoti로 넘겨야 하나,뒷과정생략이라생각후 ActivityLogin을 실행시킨다.
                //  ActivityLogin에서는 항상 akey존재유무를 따지며 ActivityMain으로 넘길지를 판단한다

                try {
                    //gson으로 객체에넣어버리기

                    student = gson.fromJson(result, Student.class);


                    Globals.id = student.getId();
                    Globals.student_id = student.getStudent_id();
                    Globals.userName = student.getName();
                    Globals.img_url = student.getImg_url();

                    Log.d("호영아Globals",Globals.id+"");
                    Log.d("호영아Globals",Globals.student_id);
                    Log.d("호영아Globals",Globals.userName);
                    Log.d("호영아Globals",Globals.img_url);

                    goMainActivity();



                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            main_waiting_cover.setVisibility(View.GONE);

                        }
                    });


                }


            }


        });


    }

    private void goMainActivity() {
        finish();
        Intent intent = new Intent(mContext,ActivityMain.class);
        startActivity(intent);
    }


    private void setLayout() {

        txtComplete = (TextView) findViewById(R.id.txtComplete);
        edtName = (EditText) findViewById(R.id.edtName);
        main_waiting_cover = (RelativeLayout) findViewById(R.id.main_waiting_cover);
    }

    private boolean spaceCheck(String spaceCheck)
    {
        for(int i = 0 ; i < spaceCheck.length() ; i++)
        {
            if(spaceCheck.charAt(i) == ' ')
                return true;
        }
        return false;
    }

}
