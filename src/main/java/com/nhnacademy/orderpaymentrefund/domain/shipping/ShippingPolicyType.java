package com.nhnacademy.orderpaymentrefund.domain.shipping;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static ShippingPolicyType of(String kor){
        if ((kor.startsWith("'") && kor.endsWith("'")) || (kor.startsWith("\"") && kor.endsWith("\""))) {
            kor = kor.substring(1, kor.length() - 1);
        }
        for (ShippingPolicyType shippingPolicyType : ShippingPolicyType.values()) {
            if (shippingPolicyType.kor.equals(kor)) {
                return shippingPolicyType;
            }
        }
        throw new IllegalArgumentException("shippingPolicyType의 value값이 잘못 되었습니다. '도서산간지역' 또는 '도서산간지역외'을 기입하세요");
    }

    @Override
    public String toString() {
        return this.kor;
    }
}
