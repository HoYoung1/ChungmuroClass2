package com.chungmuroclass.chungmuroclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.chungmuroclass.chungmuroclass.Item.ItemLecture;
import com.chungmuroclass.chungmuroclass.Item.ViewHolderLecture;
import com.chungmuroclass.chungmuroclass.R;

import java.util.ArrayList;

/**
 * Created by HY on 2018-02-24.
 */

public class AdapterLecture extends RecyclerView.Adapter<ViewHolderLecture> {

    private Context mContext;
    private ArrayList<ItemLecture> lectureItems;

    public AdapterLecture(Context context, ArrayList<ItemLecture> listItem) {
        mContext = context;
        lectureItems = listItem;
    }

    @Override


    public ViewHolderLecture onCreateViewHolder(ViewGroup parent, int viewType) {


        View baseView = View.inflate(mContext, R.layout.item_lecture,null);

        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
         int width = windowManager.getDefaultDisplay().getWidth();
        int height = width/4;
//        //int height = windowManager.getDefaultDisplay().getHeight();
        baseView.setLayoutParams(new RecyclerView.LayoutParams(width, height));



        ViewHolderLecture othersellViewHolder = new ViewHolderLecture(baseView, this);
        return othersellViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderLecture holder, int position) {
        ItemLecture item = lectureItems.get(position);


        holder.txtLectureName.setText(item.getClass_name());
        holder.txtProfessor.setText(item.getProfessor());
        holder.txtTime.setText("시작시간 : " +item.getClass_start());

        //end,on있음.
        if(item.getStateLecture().equals("on")){
            holder.txtState.setText("참여");
            holder.txtState.setBackgroundResource(R.color.colorLectureON);
        }



    }

    @Override
    public int getItemCount() {
        return lectureItems.size();
    }
}
