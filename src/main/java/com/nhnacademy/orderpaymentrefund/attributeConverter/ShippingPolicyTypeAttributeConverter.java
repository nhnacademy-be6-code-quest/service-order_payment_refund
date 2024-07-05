package com.nhnacademy.orderpaymentrefund.attributeConverter;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

@Component
public class ShippingPolicyTypeAttributeConverter implements AttributeConverter<ShippingPolicyType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ShippingPolicyType shippingPolicyType) {
        return shippingPolicyType.typeNum;
    }

    @Override
    public ShippingPolicyType convertToEntityAttribute(Integer integer) {
        for(ShippingPolicyType shippingPolicyType : ShippingPolicyType.values()) {
            if(shippingPolicyType.typeNum == integer) {
                return shippingPolicyType;
            }
        }
        throw new IllegalArgumentException("ShippingPolicyType으로 변환할 수 없습니다");
    }
}
