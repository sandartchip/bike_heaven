package com.shale.gpx_jxl_2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class List_Activity extends AppCompatActivity {

    ImageButton  findBtn;
    ImageView underlineTxtImg;
    TextView tvTxt;
    ImageButton sortBtn, workKindBtn;
    double longi_origin;
    public double lati_origin;
    int clickCnt=0, workBtnclickCnt=0;
    String searchText;
    RadioGroup rg, rg2;
    RadioButton option1, option2, option3, option4, option5, option6, option7, option8, option9;
    ImageButton upArrowSort, upArrowWork, downArrowSort, downArrowWork;

    Workbook workbook = null;
    Sheet sheet = null;
    InputStream is;

    int sortBtnclickCnt=0;
    ArrayList<Center>  AllCenterList    = new ArrayList<Center>();
    ArrayList<ListviewItem> ListData     = new ArrayList<ListviewItem>();
    ArrayList<ListviewItem> sortedListData     = new ArrayList<ListviewItem>();

    ArrayList<ListviewItem> DefaultListData     = new ArrayList<ListviewItem>();

    ArrayList<ListviewItem> searchListData = new ArrayList<>();
    ArrayList<ListviewItem> workTitleListData = new ArrayList<>();

    ListAdapter adapter;
    public ListView listView;
    private EditText searchEditTxt = null;

    Review tempReviewObject;

    public class Review{
        int centerReviewCnt;
        int scoreSum;

        public Review(int cnt, int sum){centerReviewCnt = cnt; scoreSum = sum; }
    }


    /**
     * 주어진 도(degree)값을 라디언으로 변환
     * @param deg
     * @return
     */
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }
    /**
     * 주어진 라디언(radian)값을 도(degree)값으로 변환
     * @param rad
     * @return
     */
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }
    /**
     * 두 점 사이의 거리 구하기
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public double distance(double lat1, double lon1, double lat2, double lon2){
        //두점의 위도 경도가 주어진 경우 두점사이의 거리를 미터단위로 계산하여 되돌림
        double theta, dist;
        theta = lon1 - lon2;

        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;

        return dist;
    }
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**********/

        Intent intent = getIntent();
        longi_origin = intent.getExtras().getDouble("longitude");
        lati_origin   = intent.getExtras().getDouble("latitude");

        String s_lati  = Double.toString(lati_origin);
        String s_longi = Double.toString(longi_origin);
        String locationText = intent.getExtras().getString("locationText");
        underlineTxtImg= (ImageView)findViewById(R.id.etUnderline);
        final TextView tvLocation  = (TextView) findViewById(R.id.locationText);
        tvLocation.setText(locationText);

        /* find 클릭리스너 */

        findBtn = (ImageButton) findViewById(R.id.findImgBtn);
        searchEditTxt = (EditText)findViewById(R.id.findEditTxt);

        upArrowSort= (ImageButton)findViewById(R.id.upArrowSort);
        downArrowSort= (ImageButton)findViewById(R.id.downArrowSort);

        downArrowWork= (ImageButton)findViewById(R.id.downArrowWorkKind);
        upArrowWork= (ImageButton)findViewById(R.id.upArrowWorkKind);

        rg = (RadioGroup) findViewById(R.id.rdGroup);
        rg2 = (RadioGroup) findViewById(R.id.rdGroup2);

        settingListAdapter();

        ReadCenterDataFromFile();
        printDefaultList();
  //      SortButtonEnroll();
        //일단 찍어놓고

        ButtonEnroll();
        radioButtonHandling();
        findBtnHandling();
        editTextHandling();
        //걔들가지고 Handling
    }

    public void editTextHandling()
    {
        View.OnClickListener listener ;

        listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchEditTxt.requestFocus();
            }
        };
        searchEditTxt.setOnClickListener(listener);
    }

    public void ButtonEnroll(){
        sortBtn=(ImageButton)findViewById(R.id.sortBtn);

        View.OnClickListener listener  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortBtnclickCnt%2==0) {
                    rg.setVisibility(View.VISIBLE);
                    upArrowSort.setVisibility(View.INVISIBLE);
                    downArrowSort.setVisibility(View.VISIBLE);
                }

                else    { rg.setVisibility(View.INVISIBLE);

                    downArrowSort.setVisibility(View.INVISIBLE); upArrowSort.setVisibility(View.VISIBLE);}
                sortBtnclickCnt++;
            }
        };

        sortBtn.setOnClickListener(listener);

        workKindBtn = (ImageButton) findViewById(R.id.workKindBtn);
        View.OnClickListener listener2 = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (workBtnclickCnt % 2 == 0) {
                    rg2.setVisibility(View.VISIBLE);
                    upArrowWork.setVisibility(View.INVISIBLE);
                    downArrowWork.setVisibility(View.VISIBLE);
                } else {
                    rg2.setVisibility(View.INVISIBLE);
                    downArrowWork.setVisibility(View.INVISIBLE);
                    upArrowWork.setVisibility(View.VISIBLE);
                }
                workBtnclickCnt++;

            }
        };
        workKindBtn.setOnClickListener(listener2);
    }

    public void settingListAdapter()  {

        listView=(ListView)findViewById(R.id.listview);
        adapter=new ListAdapter(this,R.layout.listviewitem, ListData);
        listView.setAdapter(adapter);
        //어댑터를 셋팅해줌

        /*
        AdapterView<?> parentView
        parent는 클릭된 항목의 부모 뷰인 어댑터 뷰입니다.
        리스트 뷰의 항목을 클릭했다면, parent는 ListView 뷰입니다. */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Center clickedCenter = (Center) parent.getItemAtPosition(position);
                Intent i_detailPage = new Intent(getApplicationContext(), ListDetailViewPage.class);
                i_detailPage.putExtra("Center", clickedCenter );

                startActivity(i_detailPage);

                //인텐트로, ListDetailViewPage로    Center 하나 요소 넘긴다.
            }
        });
    }
    public void radioButtonHandling() {
        RadioButton.OnClickListener optionOnClickListener
                = new RadioButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(option1.isChecked())        sortByScore();
                else if(option2.isChecked())  sortByDistance();
                else if(option3.isChecked()) sortByReviewCnt();
            }
        };

        RadioButton.OnClickListener optionOnClickListener2 = new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(option4.isChecked())  findByWorkTitle(0);
                else if(option5.isChecked()) findByWorkTitle(1);
                else if(option6.isChecked()) findByWorkTitle(2);
                else if(option7.isChecked()) findByWorkTitle(3);
                else if(option8.isChecked()) findByWorkTitle(4);
                else if(option9.isChecked()) findByWorkTitle(5);
            }
        };

        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);

        option1.setOnClickListener(optionOnClickListener);
        option2.setOnClickListener(optionOnClickListener);
        option3.setOnClickListener(optionOnClickListener);

        option4 = (RadioButton) findViewById(R.id.option4);
        option5 = (RadioButton) findViewById(R.id.option5);
        option6 = (RadioButton)findViewById(R.id.option6);
        option7 = (RadioButton)findViewById(R.id.option7);
        option8 = (RadioButton)findViewById(R.id.option8);
        option9 =  (RadioButton)findViewById(R.id.option9);

        option4.setOnClickListener(optionOnClickListener2);
        option5.setOnClickListener(optionOnClickListener2);
        option6.setOnClickListener(optionOnClickListener2);
        option7.setOnClickListener(optionOnClickListener2);
        option8.setOnClickListener(optionOnClickListener2);
        option9.setOnClickListener(optionOnClickListener2);
    }
    public void  findBtnHandling() {


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCnt == 0) {
                    searchEditTxt.setVisibility(View.VISIBLE);
                    underlineTxtImg.setVisibility(View.VISIBLE);
                    searchEditTxt.requestFocus(); //포커스 주자.

                    searchEditTxt.setText("");
                }
                //처음 클릭->뜨게 하고

                else {
                        searchText = searchEditTxt.getText().toString();
                        searchFunction();
                        searchEditTxt.clearFocus(); //포커스 빼자.
                    }
                clickCnt++;
            }
        };
        findBtn.setOnClickListener(listener);
    }

    public void searchFunction() {

        if (searchText.length() == 0) {
            adapter = new ListAdapter(this, R.layout.listviewitem, DefaultListData);
            listView.setAdapter(adapter);
        } else {
            searchListData.clear();

            int chkFind = 0;

            for (ListviewItem list_data : ListData) {
                Center center = list_data.getCenter();
                if (center.getName().contains(searchText)) {
                    chkFind = 1;

                    ListviewItem list_searchedCenter = new ListviewItem(center);
                    searchListData.add(list_searchedCenter);
                }
            }

            if (chkFind == 0) {
                ListData = DefaultListData;
            } else {
                ListData = searchListData; //출력 리스트를 검색된 리스트로 변경
                adapter = new ListAdapter(this, R.layout.listviewitem, ListData);
                listView.setAdapter(adapter);
            }
        }
    }

    public void findByWorkTitle(int i){
        Integer arr[]= new Integer[10];

        workTitleListData.clear();

        for(ListviewItem list_data : DefaultListData){
            Center center = list_data.getCenter();

            arr = center.getStoreDetailIntArr();
            if(arr[i]>=1) workTitleListData.add(list_data);
        }


        adapter=new ListAdapter(this,R.layout.listviewitem, workTitleListData);
        listView.setAdapter(adapter);
        ListData = workTitleListData;

        //for문으로 모든 객체 중에서 뫄뫄의 객체가 1 이상인 걸 찾자.
    }
    public void sortByReviewCnt()
    {
        sortedListData= ListData;
        Comparator<ListviewItem> sort =
                new Comparator<ListviewItem>() {
            public int compare(ListviewItem o1, ListviewItem o2) {
                return o2.getCenter().getReviewCnt().compareTo(o1.getCenter().getReviewCnt());
            }
        };
        Collections.sort(sortedListData, sort); // 위에 설정한 내용대로 정렬(sort!)
        //찍어준다.

        adapter=new ListAdapter(this,R.layout.listviewitem, sortedListData);
        listView.setAdapter(adapter);
    }

    public void sortByScore()
    {
        sortedListData= ListData;

        Comparator<ListviewItem> sort = new Comparator<ListviewItem>() {
            public int compare(ListviewItem o1, ListviewItem o2) {
                return o2.getCenter().getScore().compareTo(o1.getCenter().getScore());
            }
        };
        Collections.sort(sortedListData, sort); // 위에 설정한 내용대로 정렬(sort!)
        //찍어준다.

        adapter=new ListAdapter(this,R.layout.listviewitem, sortedListData);
        listView.setAdapter(adapter);
    }

    public void sortByDistance()
    {
        Comparator<ListviewItem> sort = new Comparator<ListviewItem>() {
            public int compare(ListviewItem o1, ListviewItem o2) {
                Double o1Dist= o1.getCenter().getDistance();
                Integer o1DistInt = o1Dist.intValue();

                Double o2Dist= o2.getCenter().getDistance();
                Integer o2DistInt = o2Dist.intValue();

                return o1DistInt.compareTo(o2DistInt);
            }
        };
        Collections.sort(ListData, sort); // 위에 설정한 내용대로 정렬(sort!)
        //찍어준다.

        adapter=new ListAdapter(this,R.layout.listviewitem, ListData); //어댑터에 새로운 List를
        listView.setAdapter(adapter);
    }
    public void printDefaultList() {

        ListData.clear();
        for (Center center : AllCenterList) {
            double longitude = center.getLongitude();
            double latitude = center.getLatitude();

            Double dist = distance( latitude, longitude, lati_origin, longi_origin );
            String distStr = dist.toString();

            if ( dist < 5000 ) { //지정한 거리 이하면 // 5000m 로 수정할 것.
                center.setDistance(dist);    //그 상점 객체의 거리를 설정
                center.setWorkKindIntArr();  //그 상점 객체의 업무종류 설정

                /*
                // 각 센터별 리뷰 갯수 초기화 시키고
                // Sheet2 열어서 노가다 서치 & 총 리뷰 갯수랑 별점평 평균 들고온다.
                // Center객체에 setting*/

                tempReviewObject = new Review(0,0); //0 0으로 초기화해놓고 함수에서 호출할떄 값 바꾼다.
                int sumOfScore = openSheet2AndGetReview(center.getCenterNum());

                int avgScore=0;
                int cnt = tempReviewObject.centerReviewCnt;

                System.out.println(sumOfScore+" + "+cnt);
                if(tempReviewObject.centerReviewCnt!=0) avgScore= sumOfScore / cnt;
                center.setScore(avgScore);

                center.setReviewCnt(cnt);

                //그 행 전체의 정보를 담아와서 출력할 center 객체에 넣는다.
                ListviewItem list_center = new ListviewItem(center);
                ListData.add(list_center);
            }
            DefaultListData=ListData;
            //착각하지 말자 열, 행이다.   행,열이 아니다.
        }
    }

    public int openSheet2AndGetReview(Integer centerNum) {

        int sumOfScore=0;
        int cntReview=0;

        try {
            try {
                is = getResources().getAssets().open("dbtable_utf8.xls");
                workbook = Workbook.getWorkbook(is);

                WorkbookSettings setting = new WorkbookSettings();
            } catch (Exception e) { }

            if (workbook != null) {
                sheet = workbook.getSheet(1); //첫번째 시트의 값만 가져옴

                if (sheet != null) {
                    int rowCount = sheet.getRows();                                 //총 행의 수를 가져옴
                    int colCount = sheet.getColumns();                              //총 열의 수를 가져옴

                    Log.i("gg", "excel");
                    Log.i(Integer.toString(rowCount), Integer.toString(colCount));

                    for (int i = 1; i < rowCount; i++) {
                        Cell cell_store = sheet.getCell(0, i); //업체번호

                        String storeStr = cell_store.getContents();

                        Integer storeNum = Integer.parseInt(storeStr);
                        //각 센터의 위치 가져온다.
                        if (storeNum == centerNum) {

                            Cell cell_score = sheet.getCell(2, i); //점수
                            String scoreStr = cell_score.getContents();
                            Integer score = Integer.parseInt(scoreStr);
                            cntReview++;
                            sumOfScore += score;
                        }
                    }
                }
            }
        } catch(Exception e){}


        tempReviewObject.centerReviewCnt = cntReview;
        tempReviewObject.scoreSum = sumOfScore;
        return sumOfScore;
    }

    public void ReadCenterDataFromFile() {


        Double longi_center;
        Double lati_center;

        try {
            try {
                is = getResources().getAssets().open("dbtable_utf8.xls");
                workbook = Workbook.getWorkbook(is);

                WorkbookSettings setting = new WorkbookSettings();
            }
            catch(Exception e){}

            if (workbook != null) {
                sheet = workbook.getSheet(0); //첫번째 시트의 값만 가져옴

                if (sheet != null) {
                    int rowCount = sheet.getRows();                                 //총 행의 수를 가져옴
                    int colCount = sheet.getColumns();                              //총 열의 수를 가져옴

                    Log.i(Integer.toString(rowCount), Integer.toString(colCount));
                    if (rowCount <= 0) {
                        throw new Exception("Read 할 데이터가 엑셀에 존재하지 않습니다.");
                    }
                    //엑셀데이터의 위도/ 경도 를 읽어온다.
                    int centerNum;
                    String name, address, phoneNumber, safePhoneNumber, startTime, endTime;
                    String storeDetail, workTitle, mainWork, workKind,  pay; //Center 객체에 들어갈 변수들 선언

                    for (int i = 1; i < rowCount; i++) {
                        Cell cell_longi = sheet.getCell(1, i); //시간이 남아돈다면 이 부분을 다음 지도와 연동. 근데 문자열 처리 때문에 문제 생길 것 같아서.. 일단은 PASS
                        Cell cell_lati = sheet.getCell(2, i);

                        String longi_str = cell_longi.getContents();
                        String lati_str = cell_lati.getContents();
                        longi_center = Double.parseDouble(longi_str);
                        lati_center = Double.parseDouble(lati_str);
                        //각 센터의 위치 가져온다.

                        centerNum = Integer.parseInt(sheet.getCell(0, i).getContents());

                        name = sheet.getCell(3, i).getContents();
                        address = sheet.getCell(4, i).getContents();
                        phoneNumber = sheet.getCell(5, i).getContents();
                        safePhoneNumber = sheet.getCell(6, i).getContents();
                        startTime = sheet.getCell(7, i).getContents();
                        endTime = sheet.getCell(8, i).getContents();
                        storeDetail = sheet.getCell(9, i).getContents(); //매장설명
                        workTitle = sheet.getCell(10, i).getContents(); //업무 타이틀
                        mainWork = sheet.getCell(11, i).getContents(); //주요업무
                        workKind = sheet.getCell(12, i).getContents(); //취급종류
                        pay = sheet.getCell(13, i).getContents(); //결제

                        //각 센터의 디테일한 정보 가져온다.
                        Center center = new Center(centerNum, longi_center, lati_center, name, address, phoneNumber, safePhoneNumber, startTime, endTime, storeDetail, workTitle, mainWork, workKind, pay, 0);  //센터 객체 생성
                        AllCenterList.add(center);
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
}