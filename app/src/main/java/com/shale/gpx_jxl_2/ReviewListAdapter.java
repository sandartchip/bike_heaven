package com.shale.gpx_jxl_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shale on 2016-06-30.
 */
public class ReviewListAdapter  extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ReviewListviewItem> data;
    private int layout;


    public ReviewListAdapter(Context context, int layout, ArrayList<ReviewListviewItem> data){ //아이템 하나하나를 연결한 ArrayList 집합 가져옴
        this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount(){return data.size();}

    @Override
    public Review getItem(int position){
        return data.get(position).getReview();
    }

    @Override
    public long getItemId(int position){return position;}

    @Override

    public View getView(int position, View convertView, ViewGroup parent){

        int centerNum,scoreNum;
        String enrollTime, writerName, content;

        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        } //View(의 하위 클래스인 앞의 Activitiy의 레이아웃)을 가져온다.

        ReviewListviewItem listviewitem = data.get(position);
        Review currentReview = listviewitem.getReview();
        /* Review 아이템 요소들 가져옴*/
        centerNum  = currentReview.getCenterNum();
        scoreNum = currentReview.getScore();

        enrollTime= currentReview.getEnrollTime();
        writerName=currentReview.getUserId();
        content=currentReview.getContent();

        TextView tvEnrollment = (TextView) convertView.findViewById(R.id.enrollTimeTxt);
        TextView tvScore  = (TextView) convertView.findViewById(R.id.scoreTxt);
        TextView tvWriterName = (TextView) convertView.findViewById(R.id.idTxt);
        TextView tvContent =(TextView) convertView.findViewById(R.id.contentTxt);

        tvEnrollment.setText(enrollTime);
        tvWriterName.setText(writerName);
        tvContent.setText(content);

//        int img_id = convertView.getResources().getIdentifier("user_icon", "drawable", MainActivity.PACKAGE_NAME  );
//        ImageView centerImgView = (ImageView) convertView.findViewById(R.id.userIcon);
//        centerImgView.setImageResource(img_id);
        //별점 이미지 받아오기

        return convertView;
    }
}
