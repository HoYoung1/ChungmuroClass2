package com.chungmuroclass.chungmuroclass;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.chungmuroclass.chungmuroclass.fragment.FragmentViewpager;
import com.chungmuroclass.chungmuroclass.globals.Globals;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, FragmentViewpager.newInstance()).commit();

        if(Globals.img_url.equals("")){
            Toast.makeText(this, "쿠키가 날라가서 로그인 액티비티로다시돌아감.", Toast.LENGTH_SHORT).show();
        }
    }
}
