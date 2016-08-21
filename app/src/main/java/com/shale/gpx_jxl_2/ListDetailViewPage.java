package com.shale.gpx_jxl_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Button;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;


public class ListDetailViewPage extends AppCompatActivity {

    Center data;

    ArrayList<ReviewListviewItem> ReviewListData     = new ArrayList<ReviewListviewItem>();
    ArrayList<Review> AllReviewData = new ArrayList<Review>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_detail);

        Intent intent = getIntent();

        data = (Center) intent.getSerializableExtra("Center");

        TextView centerNameTxt;
        centerNameTxt = (TextView) findViewById(R.id.centerName);
        centerNameTxt.setText(data.getName());

        int centerNum;
        centerNum = data.getCenterNum();

        int img_id = getResources().getIdentifier("center_img"+Integer.toString(centerNum), "drawable", MainActivity.PACKAGE_NAME  );
        ImageView centerImgView = (ImageView) findViewById(R.id.centerImg);
        //  상점 이미지 받아오기

        int tvId[] = new int[6];
        tvId[1] = (R.id.workTitleTxt1);
        tvId[2] = (R.id.workTitleTxt2);
        tvId[3] = (R.id.workTitleTxt3);
        tvId[4] = (R.id.workTitleTxt4);

        TextView tv;
        for(int i=1;i<=4;i++) {
            tv = (TextView) findViewById(tvId[i]);
            tv.setText("");
            tv.setVisibility(View.INVISIBLE);
        }

        int cnt=0;
        for(int i=0;i<=5;i++){
            String tt=Integer.toString(data.getStoreDetailIntArr()[i]);

            if(data.getStoreDetailIntArr()[i]>=1){ //그 업무종류 수행하면
                TextView tvWorkTitle;
                tvWorkTitle = (TextView) findViewById(tvId[++cnt]);
                tvWorkTitle.setText(data.getStoreDetailStrArr()[i]);
                tvWorkTitle.setVisibility(View.VISIBLE);
//                if(cnt==1) tvWorkTitle.setBackgroundColor(2);
            }
        }
        // 설명 찍기

        centerImgView.setImageResource(img_id);

        TextView tvMainWork = (TextView) findViewById(R.id.mainWorkTxt);
        tvMainWork.setText(data.getMainWork());

        TextView tvWorkKind = (TextView) findViewById(R.id.workKindTxt);
        tvWorkKind.setText(data.getWorkKind());

        TextView tvAddress = (TextView) findViewById(R.id.addressTxt);
        tvAddress.setText(data.getAddress());

        TextView tvPay = (TextView) findViewById(R.id.paymentTxt);
        tvPay.setText(data.getPay());

        TextView tvWorkingTime = (TextView) findViewById(R.id.workKindTxt);
        tvAddress.setText(data.getWorkKind());

        TextView tvPhone = (TextView) findViewById(R.id.phoneTxt);
        tvPhone.setText(data.getPhoneNumber());

        String workTime = data.getStartTime() + " ~ "+ data.getEndTime();

        TextView tvWorkTime = (TextView) findViewById(R.id.workingTimeTxt);
        tvWorkTime.setText(workTime);

        Button phoneBtn = (Button) findViewById(R.id.PhoneButton);
        phoneBtn.setOnClickListener(myClickListener);

        int scoreNum = data.getScore();
        int ScoreImgID = getResources().getIdentifier("score"+Integer.toString(scoreNum), "drawable", MainActivity.PACKAGE_NAME  );
        ImageView scoreImgView = (ImageView) findViewById(R.id.scoreImg);
        scoreImgView.setImageResource(ScoreImgID);

        TextView tvScore;
        tvScore = (TextView) findViewById(R.id.scoreTxt);
        tvScore.setText(Integer.toString(scoreNum)+".0점");

        //*************탭생성*************//
        createTab();
    }
    public void createTab()
    {
        TabHost tab_host = (TabHost) findViewById(R.id.tabhost);
        tab_host.setup();

        TabHost.TabSpec ts1 = tab_host.newTabSpec("tab1");
        ts1.setIndicator("업체 정보");
        ts1.setContent(R.id.tab1);
        tab_host.addTab(ts1);
        TextView tab1Text = (TextView) findViewById(R.id.tab1textView);
        tab1Text.setText(data.getStoreDetail());

        TabHost.TabSpec ts2 = tab_host.newTabSpec("tab2");
        ts2.setIndicator("리뷰");
        ts2.setContent(R.id.tab2);
        tab_host.addTab(ts2);
        /* 리뷰 탭 클릭시 이벤트 */
        ReviewTabEvent();

        tab_host.setCurrentTab(0);
    }

    public void GetPrintList() {
        ReadFromFile();
        //파일에서 읽어서

        for (Review reviewItem : AllReviewData) {
            if(reviewItem.getCenterNum() == data.getCenterNum()  ) //큰리스트뷰의 cenrerNumber와 일치할 때만.
            {
                ReviewListviewItem currentReview = new ReviewListviewItem(reviewItem);
                ReviewListData.add(currentReview);
            }
        }
    }
    /* 리뷰 탭 클릭시 이벤트 */
    public void ReadFromFile() //파일에서 받아와서 리뷰 전체 리스트 객체 만듬.
    {
        Workbook workbook = null;
        Sheet sheet = null;

        try {
            try {
                InputStream is = getResources().getAssets().open("dbtable_utf8.xls");
                workbook = Workbook.getWorkbook(is);
            }
            catch(Exception e){}

            if (workbook != null) {
                sheet = workbook.getSheet(1); //리뷰용 - 두번째 시트의 값만 가져옴

                if (sheet != null) {
                    int rowCount = sheet.getRows();                                 //총 행의 수를 가져옴
                    int colCount = sheet.getColumns();                              //총 열의 수를 가져옴

                    if (rowCount <= 0) {
                        throw new Exception("Read 할 데이터가 엑셀에 존재하지 않습니다.");
                    }

                    Integer centerNum,scoreNum;
                    String enrollTime, writerID, content;
                    String centerNumStr, scoreNumStr;

                    for (int i = 1; i < rowCount; i++) { //0번째줄은 목차 이름
                        centerNumStr=sheet.getCell(0, i).getContents();
                        centerNum = Integer.parseInt(centerNumStr);

                        enrollTime = sheet.getCell(1, i).getContents(); //시간이 남아돈다면 이 부분을 다음 지도와 연동. 근데 문자열 처리 때문에 문제 생길 것 같아서.. 일단은 PASS

                        scoreNumStr =sheet.getCell(2, i).getContents();
                        scoreNum = Integer.parseInt( scoreNumStr ) ;
                        writerID = sheet.getCell(3, i).getContents();
                        content = sheet.getCell(4, i).getContents();

                        Review reviewItem = new Review(centerNum, writerID, content, enrollTime, scoreNum);
                        AllReviewData.add(reviewItem);
                    }
                }
                else { //sheet null
                    System.out.println("Sheet is nulll");
                }
            } //end of workbook null
            else {
                System.out.println("WorkBook is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }   //원래 ListViewMain에 들어가야 하지만..

    public void ReviewTabEvent(){
        // 리스트 어댑터랑 연결
        ListView listView; //리스트뷰 생성
        listView=(ListView)findViewById(R.id.reviewListview);
        ReviewListAdapter adapter=new ReviewListAdapter(this,R.layout.listview_review_item, ReviewListData);
        listView.setAdapter(adapter); //어댑터와 연결
        GetPrintList();
    }
    Button.OnClickListener myClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
                case R.id.PhoneButton:
                    String phoneNum = "tel:" + data.getSafePhoneNumber();
                    Log.i("i", phoneNum);

                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNum));
                    startActivity(intentPhone);
            }
        }
    };
}