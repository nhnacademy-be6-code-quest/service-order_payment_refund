package com.nhnacademy.orderpaymentrefund.domain.order;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
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

    OrderStatus(String kor, int typeNum){
        this.kor = kor;
        this.typeNum = typeNum;
    }

    public static OrderStatus of(String kor){
        if ((kor.startsWith("'") && kor.endsWith("'")) || (kor.startsWith("\"") && kor.endsWith("\""))) {
            kor = kor.substring(1, kor.length() - 1);
        }
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.kor.equals(kor)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("OrderStatus의 value값이 잘못 되었습니다. '결제대기', '결제완료', '배송중', '반품', '배송완료' 중 하나를 기입하세요");
    }

    @Override
    public String toString() {
        return this.kor;
    }

}