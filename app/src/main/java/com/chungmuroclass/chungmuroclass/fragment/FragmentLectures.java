package com.chungmuroclass.chungmuroclass.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chungmuroclass.chungmuroclass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLectures extends Fragment {

    private View v;


    public  static FragmentLectures newInstance() {
        return new FragmentLectures();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_lectures, container, false);
    }

}
