package com.shale.gpx_jxl_2;

/**
 * 각 리스트에서 리스트 한 줄 마다 처리하는 클래스
 *
 * 각 센터 정보 하나씩 출력
 */

public class ListviewItem {
    private Center center;

    public Center getCenter() {return center;}

    public ListviewItem(Center center){
        this.center = center; //해당 센터 정보를 객체에 입력
    }

}
