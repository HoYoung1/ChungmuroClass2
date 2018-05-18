package com.chungmuroclass.chungmuroclass.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chungmuroclass.chungmuroclass.ActivityLectureDetail;
import com.chungmuroclass.chungmuroclass.EndlessRecyclerViewScrollListener;
import com.chungmuroclass.chungmuroclass.Item.ItemLecture;
import com.chungmuroclass.chungmuroclass.Item.RecyclerItemClickListener;
import com.chungmuroclass.chungmuroclass.R;
import com.chungmuroclass.chungmuroclass.adapter.AdapterLecture;
import com.chungmuroclass.chungmuroclass.globals.Globals;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;
import com.chungmuroclass.chungmuroclass.model.IsExist;
import com.chungmuroclass.chungmuroclass.model.Lecture;
import com.chungmuroclass.chungmuroclass.model.Student;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLectures extends Fragment {

    private View v;
    private RecyclerView rv_list;
    private RelativeLayout main_waiting_cover;
    private ArrayList<ItemLecture> listitem;

    private LinearLayoutManager layoutManager;

    private AdapterLecture adapter;
    private Gson gson;
    private int pageNum = 1;
    private Lecture lecture;

    private IsExist isExist;

    public static FragmentLectures newInstance() {
        return new FragmentLectures();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_lectures, container, false);

        setLayout();
        setInit();

        setRV();
        try {
            goGetLecturelist(URLs.LECTURES.getValue() + pageNum);
            //스크롤리스너 할라했는데 그냥 1페이지에 있는 20개만 보여주자
        } catch (IOException e) {
            e.printStackTrace();
        }

        //아이템 클릭시 디테일로 넘어가게
        rv_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv_list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d("호영아 ", "state확인 "+listitem.get(position).getStateLecture());
                if(listitem.get(position).getStateLecture().equals("end")){
                    //만약 종료 상태인 수업을 클릭할 경우 '수업 참가' 기능 없이 바로 디테일 액티비티로 넘어간다.
                    int lecid = listitem.get(position).getId();
                    goLectureDetail(lecid);
                }
                else {
                    try {
                        goPostIsTaken(URLs.STUDENT_ISTAKEN.getValue(), listitem.get(position).getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return v;
    }


    void goPostIsTaken(String url, final int lecture_id) throws IOException {
        main_waiting_cover.setVisibility(View.VISIBLE);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();


        params.put("student_id", Globals.student_id);
        params.put("lecture_id", String.valueOf(lecture_id));

        Log.d("호영아", "뭐쏘는지보자");
        Log.d("호영아student_id:", Globals.student_id);
        Log.d("호영아lecture_id:", String.valueOf(lecture_id));


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
                        Toast.makeText(getActivity(), "서버와 통신실패", Toast.LENGTH_SHORT).show();
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

                    isExist = gson.fromJson(result, IsExist.class);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isExist != null) {
                                if (isExist.getExist() == true) {
                                    //이미 수업 신청한 학생이니까 그냥 바로 detail로 넘긴다
                                    goLectureDetail(lecture_id);

                                } else {
                                    //수업 신청하시겠습니까? 팝업을 띄우고 수업신청을 하게한다.
                                    doAskTake(lecture_id);

                                }
                            }
                        }
                    });


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


    private void doAskTake(final int lecture_id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialogtakeclass, null);

        //다이알로그에있는 버튼
        ConstraintLayout btnTakeClass;

        //다이알로그
        final AlertDialog dialog;


        btnTakeClass = (ConstraintLayout) mView.findViewById(R.id.layout1);

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        //https://stackoverflow.com/questions/28937106/how-to-make-custom-dialog-with-rounded-corners-in-android/28937224
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //for Shape
        dialog.show();

        //size설정을위해
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, (1 * height) / 4);


        //수업 신청을 한다.
        btnTakeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                try {
                    goPostTakeClass(URLs.TAKE_CLASS.getValue(),lecture_id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    void goPostTakeClass(String url,final int lecture_id) throws IOException {
        main_waiting_cover.setVisibility(View.VISIBLE);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();


        params.put("student_id", Globals.student_id);
        params.put("lecture_id", String.valueOf(lecture_id));

        Log.d("호영아", "뭐쏘는지보자");
        Log.d("호영아student_id:", Globals.student_id);
        Log.d("호영아lecture_id:", String.valueOf(lecture_id));


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
                        Toast.makeText(getActivity(), "서버와 통신실패", Toast.LENGTH_SHORT).show();
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

                    isExist = gson.fromJson(result, IsExist.class);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isExist != null) {
                                if (isExist.getExist() == true) {
                                    //신청에성공했다
                                    goLectureDetail(lecture_id);

                                } else {
                                    Toast.makeText(getActivity(), "에러 수업 정상적으로 신청하지못했습니다", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });


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


    private void goLectureDetail(int lecture_id) {
        Intent intent = new Intent(getActivity(), ActivityLectureDetail.class);
        intent.putExtra("id", lecture_id);
        Log.d("호영아id확인", lecture_id + "");
        startActivity(intent);
    }


    private void setInit() {
        gson = new Gson();
    }

    //초기 리사이클러뷰를 설정한다
    private void setRV() {
        //리사이클러뷰설정
        listitem = new ArrayList<>();

        adapter = new AdapterLecture(getActivity(), listitem);


        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(adapter);

        // Adds the scroll listener to RecyclerView

    }


    private void setLayout() {
        rv_list = (RecyclerView) v.findViewById(R.id.rv_list);
        main_waiting_cover = (RelativeLayout) v.findViewById(R.id.main_waiting_cover);
    }


    void goGetLecturelist(String url) throws IOException {

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

                    lecture = gson.fromJson(result, Lecture.class);

                    if (lecture != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addItem();
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

    private void addItem() {
        //서울꺼넣기
        int getsize = lecture.getLectures().size();

        Log.d("호영아getsize", getsize + "");
        for (int i = 0; i < getsize; i++) {

            int id = lecture.getLectures().get(i).getId();
            String professor = lecture.getLectures().get(i).getProfessor();
            String classname = lecture.getLectures().get(i).getClass_name();
            String classstart = lecture.getLectures().get(i).getClass_start();
            String regdate = lecture.getLectures().get(i).getRegDate();
            String state = lecture.getLectures().get(i).getStateLecture();


            ItemLecture item = new ItemLecture(id, professor, classname, classstart, regdate, state);
            //여기서 userName은 서버에서받은값이아니라 액티비티에서 넘어온값으로 설정합니다.
            //서버에서는 이름은 주지않음

            listitem.add(item);
           /* if(Region == "SEOUL"){
                listitemSeoul.add(item);
            }else if(Region == "JEJU"){
                listitemJeju.add(item);
            }*/

            Log.d("호영아사이즈체킹", listitem.size() + "");


        }

        adapter.notifyDataSetChanged();

    }

}
