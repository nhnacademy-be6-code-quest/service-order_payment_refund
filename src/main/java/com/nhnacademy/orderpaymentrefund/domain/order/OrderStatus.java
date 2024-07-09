package com.nhnacademy.orderpaymentrefund.domain.order;

import lombok.Getter;

/**
 * 주문 상태 ENUM
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@Getter
public enum OrderStatus
{
    WAIT_PAYMENT("결제대기", 0), PAYED("결제완료", 1), DELIVERING("배송중", 2), DELIVERY_COMPLETE("배송완료", 3), REFUND("반품", 4), CANCEL("주문취소", 5);

    public final String kor;
    public final int typeNum;

    /**
     * 주문 상태 생성자
     *
     * @param kor 주문상태 한글로
     * @param typeNum 주문상태 타입 번호
     *
     **/
    OrderStatus(String kor, int typeNum){
        this.kor = kor;
        this.typeNum = typeNum;
    }

}