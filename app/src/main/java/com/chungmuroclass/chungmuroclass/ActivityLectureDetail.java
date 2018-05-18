package com.chungmuroclass.chungmuroclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chungmuroclass.chungmuroclass.globals.Globals;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;
import com.chungmuroclass.chungmuroclass.model.Detail;
import com.chungmuroclass.chungmuroclass.model.Lecture;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;


/**
 * Created by HY on 2018-05-19.
 * 개헬입니다소스코드
 * 표형식으로 데이터를 추가하기위해서 쌩쑈를햇습니다
 *
 * 간단히 설명해보겠습니다
 * 예를들어 학생이 3명있다면(행이3개)
 * 1행 첫번째 값 추가후 2행 첫번째값 3행 첫번째값 -> 1행 두번째값 2행 두번째값 3행 두번째값 -> 1행 세번째값 ...
 * 이런식으로 추가합니다~ 같은 칼럼에 있는 값들을 다넣고 두번째 칼럼으로가는형식입니다.
 *
 * 문제는 지각생의 경우인데 지각생같은 경우에는 값이 없어서 제가임의로 -로 표시되게했는데 이게진짜개헬입니다
 * 이해할려면 해보세요 저는안합니다~
 */

public class ActivityLectureDetail extends AppCompatActivity {

    private ImageView btnBackImage;
    private ImageView imgFaces;
    private Gson gson;
    private Context mContext;
    private TableLayout myTableLayout;
    private RelativeLayout main_waiting_cover;

    private Detail detail;
    private int lec_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        setLayout();
        setInit();

        Intent intent = getIntent();
        lec_id = intent.getIntExtra("id", 0);
        Log.d("호영아", "넘어온 강의 번호 : " + lec_id);

        try {
            goGetLectureDetail(URLs.LECTURE_DETAIL.getValue() + lec_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setInit() {
        gson = new Gson();
        mContext = this;

        btnBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Glide.with(mContext)
                .load(URLs.STORAGE.getValue()+ "KakaoTalk_20180509_022058950.jpg")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        main_waiting_cover.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        main_waiting_cover.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgFaces);
    }

    private void setLayout() {
        btnBackImage = (ImageView) findViewById(R.id.btnBackImage);
        imgFaces = (ImageView) findViewById(R.id.imgFaces);
        myTableLayout = (TableLayout) findViewById(R.id.tlGridTable);
        main_waiting_cover = (RelativeLayout) findViewById(R.id.main_waiting_cover);

    }


    void goGetLectureDetail(String url) throws IOException {

        main_waiting_cover.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        //request object from okhttp NOT from other libs
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("Failure", "happened");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_waiting_cover.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("호영아", result);


                try {
                    //gson으로 객체에넣어버리기

                    detail = gson.fromJson(result, Detail.class);

                    if (detail.getLecchecks().size() != 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addView();
                            }
                        });
                    }
                   /* if (getUserInfo.getIsdata().getResult()) {
                        //true로전달되서왔다면 다음행동 정의하면된다
                        Glide.with(mContext)
                                .load(URLs.STORAGE.getValue() + getUserInfo.getIsdata().getUserInfo().get(0).getUserProfilePhoto())
                                .into(profileImageview);
                        profileName.setText(getUserInfo.getIsdata().getUserInfo().get(0).getUserName());
                        profileCountry.setText(getUserInfo.getIsdata().getUserInfo().get(0).getUserCountry());


                    } else {
                        //false로 전달되서왔다면 정상적으로통신이되지않음.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }*/
                    //객체에 들어온 정보를 가지고 활용가능하다.

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

    private void addView() {
        main_waiting_cover.setVisibility(View.VISIBLE);
        TableRow rowTitle = new TableRow(this);
//        rowColumnName.setGravity(Gravity.CENTER_HORIZONTAL);


        TextView[] txtTitle = new TextView[62];
        txtTitle[0] = new TextView(this);
        txtTitle[1] = new TextView(this);
        txtTitle[0].setText("No.");
        txtTitle[0].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        txtTitle[0].setGravity(Gravity.CENTER);
        txtTitle[0].setTypeface(Typeface.SERIF, Typeface.BOLD);
        txtTitle[0].setPadding(20, 20, 20, 20);

        txtTitle[1].setText("이름");
        txtTitle[1].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        txtTitle[1].setGravity(Gravity.CENTER);
        txtTitle[1].setTypeface(Typeface.SERIF, Typeface.BOLD);
        txtTitle[1].setPadding(20, 20, 20, 20);

        TableRow.LayoutParams params = new TableRow.LayoutParams();


        txtTitle[2] = new TextView(this);
        txtTitle[2].setText("시작");
        txtTitle[2].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtTitle[2].setGravity(Gravity.CENTER);
        txtTitle[2].setTypeface(Typeface.SERIF, Typeface.BOLD);
        txtTitle[2].setPadding(20, 20, 20, 20);
        txtTitle[2].setBackgroundColor(Color.rgb(51, 51, 51));


        //3번째 칼럼부터 62번째칼럼까지(시작~ 60번째까지)
        for (int i = 3; i < 62; i++) {
            txtTitle[i] = new TextView(this);
            txtTitle[i].setText(i - 2 + "분");
            txtTitle[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            txtTitle[i].setGravity(Gravity.CENTER);
            txtTitle[i].setTypeface(Typeface.SERIF, Typeface.NORMAL);
            txtTitle[i].setPadding(20, 20, 20, 20);
        }

        //68개의 텍스트뷰를 row에추가
        for (int i = 0; i < 62; i++) {
            rowTitle.addView(txtTitle[i], params);
        }
        ////////////////////////////////////////////////////////////////////////////////////
        //여기까지 칼럼Title 설정입니다.


        //학생 수 만큼 row생성.
        int numOfStu = detail.getStudents().size();
        TableRow[] rowStu = new TableRow[numOfStu];
        for (int i = 0; i < numOfStu; i++) {
            rowStu[i] = new TableRow(this);
        }

        //이름과 No. 값 넣자
        for (int i = 0; i < numOfStu; i++) {
            TextView txtNo = new TextView(this);
            txtNo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            txtNo.setGravity(Gravity.CENTER);
            txtNo.setPadding(20, 20, 20, 20);

            TextView txtName = new TextView(this);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            txtName.setGravity(Gravity.CENTER);
            txtName.setPadding(20, 20, 20, 20);
            txtNo.setText(String.valueOf(i + 1));
            txtName.setText(detail.getStudents().get(i).getName());
            rowStu[i].addView(txtNo);
            rowStu[i].addView(txtName);

        }


        //checks 수만큼 Textview생성
        int numOfCheck = detail.getLecchecks().size();
        TextView[] txtchk = new TextView[numOfCheck];

        int chFlag = 1;
        int m = 0;
        // m 은 행 번호를 의미합니다
        // chFlag는 칼럼을 의미하기 위해서 사용했습니다.


        //서버에서 준값을 확인후 col_index가 1부터 시작하지 않는다면 수업이 맨처음 시작할때 학생이 아무도 없었다는 얘기가됩니다. -표시를 다 넣어줘야합니다
        if (detail.getLecchecks().get(0).getCol_index() != 1) {
            Log.d("호영아", "##############################################################");
            Log.d("호영아", "첫번째 인덱스가 1이아니다 -표시 시키고시작해야한다.");

            //문제는 1이 아닐때다 전에있는 칼럼을 전부 '-'표시 시켜야하기때문!
            int _count = detail.getLecchecks().get(0).getCol_index() - 1;
            Log.d("호영아3", "_count : " + _count);
            TextView[] txtBlank = new TextView[numOfStu * _count];


            txtBlank = new TextView[numOfStu * _count];
            for (int a = 0; a < _count; a++) {
                for (int b = 0; b < numOfStu; b++) {
                    txtBlank[b] = new TextView(this);
                    txtBlank[b].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    txtBlank[b].setGravity(Gravity.CENTER);
                    txtBlank[b].setPadding(20, 20, 20, 20);
                    txtBlank[b].setText("-");
                    rowStu[b].addView(txtBlank[b]);
                }
                chFlag++;
                Log.d("호영아3", "chFlag : " + chFlag);
            }


        }


        // 서버에서 받은 Check리스트를 가지고 O X 표시를 한다. 지각생이없고 정상적으로 수업이끝났다면 학생수*칼럼수60개(60분) 가 될것이다.
        for (int j = 0; j < numOfCheck; j++) {
            Log.d("호영아 값확인", "numOfCheck : " + numOfCheck);
            txtchk[j] = new TextView(this);
            txtchk[j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            txtchk[j].setGravity(Gravity.CENTER);
            txtchk[j].setTypeface(Typeface.SERIF, Typeface.BOLD);
            txtchk[j].setPadding(20, 20, 20, 20);

            //서버로부터 오는 similarity 값이 80보다 크다면 O로 체크한다.
            if (detail.getLecchecks().get(j).getSimilarity() > 80) {
                txtchk[j].setText("O");
            } else {
                txtchk[j].setText("X");
            }




            if (chFlag == detail.getLecchecks().get(j).getCol_index()) {
                rowStu[m].addView(txtchk[j]);
                m++;
            } else {
                if (numOfStu == m) {
                    //numOfStu == m 의 뜻은 - 표시없이 그냥 다음으로 넘어간다는뜻임. 다음 칼럼이되겠다


                } else if (numOfStu < m) {
                    Toast.makeText(mContext, "syserr , m bigger than numofSTu", Toast.LENGTH_SHORT).show();
                } else {
                    int numLateCount = numOfStu - m;
                    Log.d("호영아 값확인", "numLateCount : " + numLateCount);
                    TextView[] txtForLate = new TextView[numLateCount];
                    for (int n = 0; n < numLateCount; n++) {
                        txtForLate[n] = new TextView(this);
                        txtForLate[n].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        txtForLate[n].setGravity(Gravity.CENTER);
                        txtForLate[n].setTypeface(Typeface.SERIF, Typeface.BOLD);
                        txtForLate[n].setPadding(20, 20, 20, 20);
                        txtForLate[n].setText("-");
                        rowStu[m].addView(txtForLate[n]);
                        m++;
                    }

                }
                m = 0;
                rowStu[m].addView(txtchk[j]);
                m++;
                chFlag++;
                //col_index가 바뀐시점에는 무조껀 연산후 chFlag가 1더해져야함
                Log.d("호영아2", "chFlag : " + chFlag);
            }

        }


        //row들 다 추가
        myTableLayout.addView(rowTitle);
        //밑줄추가
        View v = new View(this);
        v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 4));
        v.setBackgroundColor(Color.rgb(51, 51, 51));
        myTableLayout.addView(v);

        for (int k = 0; k < numOfStu; k++) {
            myTableLayout.addView(rowStu[k]);

            View vv = new View(this);
            vv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 4));
            vv.setBackgroundColor(Color.rgb(51, 51, 51));
            myTableLayout.addView(vv);
        }

        main_waiting_cover.setVisibility(View.GONE);
    }


}
