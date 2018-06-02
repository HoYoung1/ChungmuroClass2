package com.chungmuroclass.chungmuroclass;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chungmuroclass.chungmuroclass.fragment.FragmentViewpager;
import com.chungmuroclass.chungmuroclass.globals.Globals;

public class ActivityMain extends AppCompatActivity {

    private TextView txtChungmuro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtChungmuro = (TextView)findViewById(R.id.txtChungmuro);
        //맨위 충무로클래스 텍스트 누르면 새로고침되게!
        txtChungmuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                overridePendingTransition(0,0);

                startActivity(getIntent());
                overridePendingTransition(0,0);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, FragmentViewpager.newInstance()).commit();

        if(Globals.img_url.equals("")){
            Toast.makeText(this, "쿠키가 날라가서 로그인 액티비티로다시돌아감.", Toast.LENGTH_SHORT).show();
        }
    }
}
