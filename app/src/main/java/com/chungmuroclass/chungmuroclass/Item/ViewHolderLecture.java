package com.chungmuroclass.chungmuroclass.Item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chungmuroclass.chungmuroclass.R;
import com.chungmuroclass.chungmuroclass.adapter.AdapterLecture;

/**
 * Created by HY on 2018-02-24.
 */

public class ViewHolderLecture extends RecyclerView.ViewHolder {


    private AdapterLecture mAdater;

    public TextView txtState,txtLectureName,txtProfessor,txtTime;


    public ViewHolderLecture(View itemView, AdapterLecture adapterLecture) {
        super(itemView);
        txtState = (TextView)itemView.findViewById(R.id.txtState);
        txtLectureName = (TextView)itemView.findViewById(R.id.txtLectureName);
        txtProfessor = (TextView)itemView.findViewById(R.id.txtProfessor);
        txtTime = (TextView)itemView.findViewById(R.id.txtTime);
        mAdater = adapterLecture;


        //chatLastMsg.setOnClickListener(this);
        //chatLastMsgTime.setOnClickListener(this);
    }

    /*@Override
    public void onClick(View view) {

        int position = getAdapterPosition();
        switch (view.getId()){
            case R.id.chatLastMsg:
                mAdater.onLikeClicked(position);
                break;
            case R.id.chatLastMsgTime:
                break;
        }

    }*/
}
