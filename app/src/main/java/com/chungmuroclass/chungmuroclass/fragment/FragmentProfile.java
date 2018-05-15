package com.chungmuroclass.chungmuroclass.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chungmuroclass.chungmuroclass.R;
import com.chungmuroclass.chungmuroclass.globals.Globals;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {


    private View v;
    private ImageView imgProfile;
    private TextView txtName;


    public  static FragmentProfile newInstance() {
        return new FragmentProfile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_profile, container, false);


        setLayout();
        setInit();
        return v;
    }

    private void setInit() {
        Glide.with(getActivity()).load(URLs.STORAGE.getValue()+ Globals.img_url).into(imgProfile);
        txtName.setText(Globals.userName);
    }

    private void setLayout() {
        imgProfile=(ImageView)v.findViewById(R.id.imgProfile);
        txtName=(TextView)v.findViewById(R.id.txtName);
    }

}
