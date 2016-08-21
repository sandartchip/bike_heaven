package com.shale.gpx_jxl_2;

/**
 * Created by Shale on 2016-06-30.
 */
public class ReviewListviewItem {

    private Review review;
    Integer centerNum; //상점코드
    String userId; //작성자 ID
    String content; //내용
    String enrollTime;//등록시간
    Integer score; //별점 점수

    public Review getReview() {return review;}

    public ReviewListviewItem(Review review){
        this.review = review; //해당 센터 정보를 객체에 입력
    }
}
