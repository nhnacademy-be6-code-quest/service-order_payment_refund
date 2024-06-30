package com.nhnacademy.orderpaymentrefund.domain.shipping;

import lombok.Getter;

/**
 * 배송 정책 상태 ENUM
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Getter
public enum ShippingPolicyType {

    ISLAND_MOUNTAIN("도서산간지역", 0), NOT_ISLAND_MOUNTAIN("도서산간지역외", 1);

    public final String kor;
    public final int typeNum;

    ShippingPolicyType(String kor, int typeNum) {
        this.kor = kor;
        this.typeNum = typeNum;
    }

}
