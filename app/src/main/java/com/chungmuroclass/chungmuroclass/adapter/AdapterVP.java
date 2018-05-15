package com.chungmuroclass.chungmuroclass.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import com.chungmuroclass.chungmuroclass.R;

/**
 * Created by HY on 2018-05-10.
 */

public class AdapterVP extends FragmentPagerAdapter {

    private Fragment[] arrFragments;

    public AdapterVP(FragmentManager fm, Fragment[] arrFragments) {
        super(fm);
        this.arrFragments = arrFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return arrFragments[position];
    }

    @Override
    public int getCount() {
        return arrFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return "이미지";
            case 1:
                return "강의목록";
            case 2:
                return "설정";
            default:
                return "";
        }
    }
}
