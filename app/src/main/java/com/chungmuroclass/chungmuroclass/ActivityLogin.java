package com.chungmuroclass.chungmuroclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chungmuroclass.chungmuroclass.globals.Globals;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;
import com.chungmuroclass.chungmuroclass.model.Student;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityLogin extends AppCompatActivity {

    private Gson gson;
    private Context mContext;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private ImageView imgFB;

    //페이스북 로그인
    private CallbackManager callbackManager;

    //sns 를 위한 로그인
    String snsTOKEN ="";
    String snsNAME ="";
    String snsEMAIL ="";

    //구글 로그인
    private static final int RC_SIGN_IN = 12345;

    private RelativeLayout main_waiting_cover;

    private Student student;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        setContentView(R.layout.activity_login);

        gson = new Gson();
        mContext = this;

        // 레이아웃 설정,findview 집합
        setLayout();

        // 기타 초기화, button 초기화 등
        setInit();
    }

    private void setInit() {
        //구글 로그인을위해서
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //페이스북 로그인을 위해서
        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);



        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("호영아","1");

                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(ActivityLogin.this,
                        Arrays.asList("public_profile", "email"));
                Log.d("호영아","2");
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                snsTOKEN = loginResult.getAccessToken().getToken();
                                Log.d("호영아페북토큰", snsTOKEN);
                                // App code
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                Log.v("호영아LoginActivity", response.toString());
                                                // Application code
                                                try {
                                                    snsTOKEN = object.getString("id");
                                                    snsNAME = object.getString("name");
                                                    snsEMAIL = object.getString("email");

                                                    Log.d("호영아페북id", snsTOKEN);
                                                    Log.d("호영아페북name", snsNAME);
                                                    Log.d("호영아페북email", snsEMAIL);

                                                    try {
                                                        goPostLoginWithSNS(URLs.JOIN.getValue(),snsTOKEN,snsNAME,snsEMAIL);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    //바로 회원가입으로 넘기는것이아니라
                                                    //가입이되어있는지 확인할것!
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,email,gender,birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                Log.e("호영아nCancel", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("호영아onError", "onError " + exception.getLocalizedMessage());
                            }

                        });
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
                //Toast.makeText(mContext, "구글 로그인 아직 미구현", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLayout() {
        imgFB = (ImageView) findViewById(R.id.imgFB);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        main_waiting_cover = findViewById(R.id.main_waiting_cover);
    }

    //구글 로그인
    private void googleSignIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.d("호영아", "일로 들어온건가요..?2");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            Log.d("호영아", "일로 들어온건가요..?3");

            return;
        }


        callbackManager.onActivityResult(requestCode, resultCode, data);


    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("호영아하나씪", account.getEmail());
            Log.d("호영아하나씪", account.getId());
            Log.d("호영아하나씪", account.getDisplayName());
            Log.d("호영아하나씪", account.getFamilyName());
            Log.d("호영아하나씪", account.getIdToken());


            snsTOKEN = account.getId();
            snsNAME = account.getDisplayName();
            snsEMAIL = account.getEmail();

            try {
                goPostLoginWithSNS(URLs.JOIN.getValue(),snsTOKEN,snsNAME,snsEMAIL);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //밑에있는건 바로 ㅅ회원가입인데 회원가입으로 바로시키면안됨.
            //먼저 로그인으로돌리자.






            /*Intent intent = new Intent(mContext, ActivityDupliSignUpForSocialLogin.class);
            intent.putExtra("user_type", user_type);
            intent.putExtra("ID", account.getIdToken());
            intent.putExtra("PW", "");

            intent.putExtra("EMAIL", account.getEmail());
            intent.putExtra("NAME", account.getDisplayName());
            intent.putExtra("user_image", "");

            startActivity(intent);*/
            //테스트임


            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    //페이스북 에서 이메일 가져올때씀
    public void requestUserProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //Log.d("호영아response",response.toString());
                            String email = response.getJSONObject().getString("email").toString();

                            Log.d("호영아EmailResult", email);
                  /*          Log.d("호영아Success", String.valueOf(Profile.getCurrentProfile().getId()));
                            Log.d("호영아Success", String.valueOf(Profile.getCurrentProfile().getName()));*/
/*

                            Profile.getCurrentProfile().getId();
                            Profile.getCurrentProfile().getFirstName();
                            Profile.getCurrentProfile().getLastName();*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("호영아", "여기로들어온것같은데..?");
                        }

                    }
                });
/*        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();*/
    }


    void goPostLoginWithSNS(String url, final String token, final String name, final String email) throws IOException {


        main_waiting_cover.setVisibility(View.VISIBLE);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();


        params.put("student_id", token);

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
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        main_waiting_cover.setVisibility(View.GONE);
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("호영아", "성공^^;");
                Log.d("호영아result", "성공^^;"+result);
                //TO DO
                // akey값 가져와서 sharedpreference 저장
                // 원래는 SignUpNoti로 넘겨야 하나,뒷과정생략이라생각후 ActivityLogin을 실행시킨다.
                //  ActivityLogin에서는 항상 akey존재유무를 따지며 ActivityMain으로 넘길지를 판단한다

                try {
                    //gson으로 객체에넣어버리기

                    student = gson.fromJson(result, Student.class);

                    if(student.getStudent_id().equals("0")){
                        //처음 가입하는 회원임. To do 회원가입 페이지로 넘김.
                        Intent intent= new Intent(mContext, ActivitySignUpFace.class);
                        intent.putExtra("student_id",snsTOKEN);
                        intent.putExtra("userName",snsNAME);
                        startActivity(intent);
                    }else{
                        Globals.id=student.getId();
                        Globals.student_id=student.getStudent_id();
                        Globals.userName=student.getName();
                        Globals.img_url=student.getImg_url();

                        Log.d("호영아Globals",Globals.id+"");
                        Log.d("호영아Globals",Globals.student_id);
                        Log.d("호영아Globals",Globals.userName);
                        Log.d("호영아Globals",Globals.img_url);

                        Intent intent = new Intent(mContext, ActivityMain.class);
                        startActivity(intent);
                    }


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


}
