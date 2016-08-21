package com.shale.gpx_jxl_2;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

/**
 * Created by Shale on 2016-06-15.
 */
public class Center implements Serializable{

    private double longitude; //위도
    private double latitude;  //경도
    private int  centerNum;   //상점 번호

    private String  name; //상호
    private String address; //주소
    private String phoneNumber;//전화번호
    private String safePhoneNumber;//안전번호
    private String startTime; //시작
    private String endTime; //종료
    private String storeDetail; //매장설명
    private String workTitle; //업무 타이틀
    private String mainWork; //주요업무
    private String workKind; //취급종류
    private String pay; //결제
    private Integer storeDetailIntArr[] = new Integer[10]; //공식서비스센터=0 출장수리=1 보험수리=2..
    private String storeDetailStrArr[] = new String[10];
    private Double distance;//현재 위치와의 거리
    private Integer score; //평균점수
    private Integer reviewCnt; //리뷰갯수

    public Center(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.address  = address;
    }

    public Center(int centerNum, double longitude, double latitude, String  name, String address,String phoneNumber,String safePhoneNumber,
      String startTime, String endTime,
      String storeDetail, String workTitle, String mainWork, String workKind, String pay, double distance) {
        this.centerNum = centerNum;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.address  = address;
        this.phoneNumber = phoneNumber;
        this.safePhoneNumber= safePhoneNumber;
        this.startTime  = startTime;
        this.endTime = endTime;
        this.storeDetail = storeDetail;
        this.workTitle = workTitle;
        this.mainWork = mainWork;
        this.workKind  = workKind;
        this.pay = pay;
        this.storeDetailStrArr[0]="공식서비스센터";
        this.storeDetailStrArr[1]="출장수리";
        this.storeDetailStrArr[2]="스쿠터전문";
        this.storeDetailStrArr[3]="보험처리";
        this.storeDetailStrArr[4]="튜닝";
        this.storeDetailStrArr[5]="정비";
    }

    public int getCenterNum(){ return centerNum; }
    public double getLongitude(){
        return longitude;
    }
    public double getLatitude(){
        return latitude;
    }
    public String getName()      {   return name; }
    public String getAddress()   {   return address; }
    public String getPhoneNumber() {return phoneNumber; }
    public String getSafePhoneNumber() {return safePhoneNumber;}
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStoreDetail(){ return storeDetail; }
    public String getWorkTitle(){return workTitle;  }
    public String getMainWork(){  return mainWork;   }
    public String getWorkKind(){ return workKind;   }
    public String getPay() { return pay; }
    public Integer getScore(){ return score; }
    public Integer getReviewCnt(){ return reviewCnt;}
    public double getDistance(){ return distance; }
    public Integer[] getStoreDetailIntArr() {return storeDetailIntArr; }
    public String[] getStoreDetailStrArr() {return storeDetailStrArr; }

    public void setWorkKindIntArr() {
        //문자열 잘라서
        for (int i = 0; i < 10; i++) storeDetailIntArr[i] = 0;

        StringTokenizer tokens = new StringTokenizer(workTitle, ",");

        for (int x = 1; tokens.hasMoreElements(); x++) {
            String tokenStr = tokens.nextToken();
            for (int j = 0; j <= 5; j++) {
                if (tokenStr.equals(storeDetailStrArr[j]) == true) {
                    storeDetailIntArr[j]++;
                }
            }
        }
    }
    public void setReviewCnt(Integer reviewCnt){this.reviewCnt = reviewCnt;}
    public void setScore(Integer score){this.score = score; }

    public void setDistance(double dist){this.distance = dist; }
    public void set_longi(double longi){
        longitude = longi;
    }
    public void set_lati(double lati){
        latitude = lati;
    }
}

