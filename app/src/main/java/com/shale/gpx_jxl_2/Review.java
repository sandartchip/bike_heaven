package com.shale.gpx_jxl_2;

/**
 * Created by Shale on 2016-06-30.
 */
public class Review {
    Integer centerNum; //상점코드
    String userId; //작성자 ID
    String content; //내용
    String enrollTime;//등록시간
    Integer score; //별점 점수

    public Review(Integer centerNum, String userId, String content, String enrollTime, Integer score){//생성자
        this.centerNum=centerNum;
        this.userId=userId;
        this.content=content;
        this.enrollTime=enrollTime;
        this.score=score;
    }
    public Integer getCenterNum(){ return centerNum; }
    public String getUserId()    {return userId;}
    public String getContent()   {return content;}
    public String getEnrollTime(){return enrollTime;}
    public Integer getScore()    { return score; }
}
