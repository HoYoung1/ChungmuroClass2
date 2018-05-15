package com.chungmuroclass.chungmuroclass.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chungmuroclass.chungmuroclass.EndlessRecyclerViewScrollListener;
import com.chungmuroclass.chungmuroclass.Item.ItemLecture;
import com.chungmuroclass.chungmuroclass.Item.RecyclerItemClickListener;
import com.chungmuroclass.chungmuroclass.R;
import com.chungmuroclass.chungmuroclass.adapter.AdapterLecture;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLectures extends Fragment {

    private View v;
    private RecyclerView rv_list;
    private ArrayList<ItemLecture> listitemothersell;

    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    private AdapterLecture adapter;
    private Gson gson;
    private int pageNum = 1;

    public  static FragmentLectures newInstance() {
        return new FragmentLectures();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_fragment_lectures, container, false);

        setLayout();
        setInit();

        setRV();
        try {
            doGetLecturelist(URLs.LECTURES.getValue()+pageNum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //아이템 클릭시 디테일로 넘어가게
        rv_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv_list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(getActivity(), ActivityLectureDetail.class);
                intent.putExtra("id",listitemothersell.get(position).getId());
                Log.d("호영아id확인",listitemothersell.get(position).getId()+"");
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return v;
    }

    private void setInit() {
        gson = new Gson();
    }

    //초기 리사이클러뷰를 설정한다
    private void setRV() {
        //리사이클러뷰설정
        listitemothersell = new ArrayList<>();

        adapter = new AdapterLecture(getActivity(), listitemothersell);


        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("호영아 page, totalItemsCo", page + "" + totalItemsCount);


                try {
                    pageNum = pageNum + 1;
                    doGetLecturelist(URLs.LECTURES.getValue()+pageNum);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("호영아", "제발시작하자마자 작동안되게해주세요");

              /*  RegionURL = "/KR_10";
                try {
                    doGetSalesList(URLs.SALES_LIST_WITH_REGION.getValue()+RegionURL+"/"+pageNumSeoul);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                Log.d("호영아pageNum", pageNum + "확인");

            }
        };
        // Adds the scroll listener to RecyclerView
        rv_list.addOnScrollListener(scrollListener);

    }

    private void setLayout() {
        rv_list = (RecyclerView)v.findViewById(R.id.rv_list);
    }

}
