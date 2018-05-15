package com.chungmuroclass.chungmuroclass.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chungmuroclass.chungmuroclass.R;
import com.chungmuroclass.chungmuroclass.adapter.AdapterVP;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewpager extends Fragment {

    View v;
    private ViewPager mViewpager;
    Fragment[] arrFragments;

    public  static FragmentViewpager newInstance() {
        return new FragmentViewpager();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_viewpager, container, false);

        TabLayout mTabs = v.findViewById(R.id.mTabs);
        mViewpager = (ViewPager) v.findViewById(R.id.mViewpager);

        try{
            arrFragments = new Fragment[3];
            arrFragments[0] = FragmentProfile.newInstance();
            arrFragments[1] = FragmentLectures.newInstance();
            arrFragments[2] = FragmentSetting.newInstance();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        AdapterVP adapter = new AdapterVP(getChildFragmentManager(), arrFragments);

        mViewpager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewpager);

        /*mTabs.getTabAt(0).setIcon(R.drawable.baseline_perm_identity_black_18dp);
        mTabs.getTabAt(1).setIcon(R.drawable.baseline_list_black_18dp);
        mTabs.getTabAt(2).setIcon(R.drawable.baseline_check_circle_black_18dp);*/


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewpager.setCurrentItem(1);
    }
}
