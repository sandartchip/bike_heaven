package com.shale.gpx_jxl_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;

import jxl.biff.drawing.Button;
/* 리스트 요소 하나하나 처리한다. */

public class ListAdapter  extends BaseAdapter {

    /*Adapter란 ListView에 출력할 Data를 보관하는 장소라고 생각 하시면 되는데요.
    실질적으로 List형식의 Data를 Adapter에 넘겨주면 Adapter가 ListView의 Row마다 List형식의 데이터를 출력 할 수 있게 도와 줍니다.*/

    private LayoutInflater inflater;
    private ArrayList<ListviewItem> data;
    private int layout;

    public ListAdapter(Context context, int layout, ArrayList<ListviewItem> data){ //아이템 하나하나를 연결한 ArrayList 집합 가져옴
        this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount(){return data.size();}

    @Override
    public Center getItem(int position){return data.get(position).getCenter();}

    @Override
    public long getItemId(int position){return position;}

    @Override

    /*
  getView() 메소드.. 이것이 가장 중요합니다.
  여기에서 항목 하나를 출력하기 위한 뷰(View)를 생성하여 리턴하는 역할을 합니다.
  매개변수로 받는 첫번째인 position은 항목의 순서값이며,


  두번째인 convertView는 이전에 생성된 View입니다.  처음에는 생성된 View가 없기 때문에 null값입니다.
  따라서 null일때, 우리가 작성한 list_layout.xml 레이아웃을 인플레이트 해서 convertView에 넣어주는 것입니다.
  만약 리스트뷰를 처음 보여주는 화면에 5줄의 항목이 표시된다면 처음 5줄을 생성할 때에는 convertView가 모두 null값이며,
  위와같이 레이아웃을 인플레이트해서 생성해줍니다.\
 */

    // ListView의 뿌려질 한줄의 Row를 설정 합니다.
    //ListActivity에서 호출했는데 걔가 listviewitem.xml을 호출하고 있으니~


    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        } //View(의 하위 클래스인 앞의 Activitiy의 레이아웃)을 가져온다.

        ListviewItem listviewitem = data.get(position);

        /* Center 아이템 요소들 가져옴*/
        Double latitude = listviewitem.getCenter().getLatitude();
        Double longitude = listviewitem.getCenter().getLongitude();

        int centerNum, reviewCnt;
        String name, address, phoneNumber, safePhoneNumber, startTime, endTime;
        String storeDetail, workTitle, mainWork, workKind,  pay;

        Center detailCenter;

        detailCenter = listviewitem.getCenter();
        centerNum = detailCenter.getCenterNum();
        name =           detailCenter.getName();
        address =        detailCenter.getAddress();
        phoneNumber=     detailCenter.getPhoneNumber();
        safePhoneNumber= detailCenter.getSafePhoneNumber();
        startTime=       detailCenter.getStartTime();
        endTime=         detailCenter.getEndTime();
        storeDetail=     detailCenter.getStoreDetail();
        workTitle=       detailCenter.getWorkTitle();
        mainWork=        detailCenter.getMainWork();
        workKind=        detailCenter.getWorkKind();
        pay=             detailCenter.getPay();
        reviewCnt = detailCenter.getReviewCnt();

        TextView tvReviewCnt  = (TextView) convertView.findViewById(R.id.tvReviewCnt);
        tvReviewCnt.setText(Integer.toString(reviewCnt));

        TextView tv_centerName = (TextView) convertView.findViewById(R.id.centerTitleTxt);
        tv_centerName.setText(name);

        TextView tv_address = (TextView)   convertView.findViewById(R.id.addressTxt);
        tv_address.setText(address);

        // position으로 하면 문제가.. 선택 안된 애들까지도
        // ㅡㅡ;; 결국 DB넘버를 INT로 해서 받아와야지.

        int img_id = convertView.getResources().getIdentifier("center_img"+Integer.toString(centerNum), "drawable", MainActivity.PACKAGE_NAME  );
        ImageView centerImgView = (ImageView) convertView.findViewById(R.id.centerImg);
        //  상점 이미지 받아오기

        centerImgView.setImageResource(img_id);

        /***********/



        /* 공식서비스센터 튜닝..찍기*/

        int tvId[] = new int[6];
        tvId[1] = (R.id.workTitleTxt1);
        tvId[2] = (R.id.workTitleTxt2);
        tvId[3] = (R.id.workTitleTxt3);
        tvId[4] = (R.id.workTitleTxt4);

        TextView tv;
        for(int i=1;i<=4;i++) {
            tv = (TextView) convertView.findViewById(tvId[i]);
            tv.setText("");
            tv.setVisibility(View.INVISIBLE);
        }

        int cnt=0;


     //   System.out.println("******");
        for(int i=0;i<=5;i++){
            if(detailCenter.getStoreDetailIntArr()[i]>=1){ //그 업무종류 수행하면

                TextView tvWorkTitle;
                tvWorkTitle = (TextView) convertView.findViewById(tvId[++cnt]);
                tvWorkTitle.setText(detailCenter.getStoreDetailStrArr()[i]);
                tvWorkTitle.setVisibility(View.VISIBLE);
                System.out.println(detailCenter.getStoreDetailStrArr()[i]);
            }
        }

        int scoreNum = detailCenter.getScore();
        int ScoreImgID = convertView.getResources().getIdentifier("small_score"+Integer.toString(scoreNum), "drawable", MainActivity.PACKAGE_NAME  );
        ImageView scoreImgView = (ImageView) convertView.findViewById(R.id.scoreImg);
        scoreImgView.setImageResource(ScoreImgID);


        TextView tvScore;
        tvScore = (TextView) convertView.findViewById(R.id.scoreTxt);
        tvScore.setText(Integer.toString(scoreNum)+".0점");

        //별점 이미지 받아오기

        return convertView;
    }
}
