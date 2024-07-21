package com.nhnacademy.orderpaymentrefund.domain.shipping;

import lombok.Getter;

/**
 * 배송 정책 상태 ENUM
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Getter
public enum ShippingPolicyType {

    CLIENT_SHIPPING("회원배송비", 0), NON_CLIENT_SHIPPING("비회원배송비", 1);

    public final String kor;
    public final int typeNum;

    ShippingPolicyType(String kor, int typeNum) {
        this.kor = kor;
        this.typeNum = typeNum;
    }

    public static ShippingPolicyType of(String kor){
        if ((kor.startsWith("'") && kor.endsWith("'")) || (kor.startsWith("\"") && kor.endsWith("\""))) {
            kor = kor.substring(1, kor.length() - 1);
        }
        for (ShippingPolicyType shippingPolicyType : ShippingPolicyType.values()) {
            if (shippingPolicyType.kor.equals(kor)) {
                return shippingPolicyType;
            }
        }
        throw new IllegalArgumentException("shippingPolicyType의 value값이 잘못 되었습니다. '회원배송비' 또는 '비회원배송비'을 기입하세요");
    }

    @Override
    public String toString() {
        return this.kor;
    }
}
