package com.nhnacademy.orderpaymentrefund.attributeconverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShippingPolicyTypeAttributeConverterTest {
    private ShippingPolicyTypeAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ShippingPolicyTypeAttributeConverter();
    }

    @Test
    void testConvertToDatabaseColumn() {
        assertEquals(0, converter.convertToDatabaseColumn(ShippingPolicyType.CLIENT_SHIPPING));
        assertEquals(1, converter.convertToDatabaseColumn(ShippingPolicyType.NON_CLIENT_SHIPPING));
    }

    @Test
    void testConvertToEntityAttribute() {
        assertEquals(ShippingPolicyType.CLIENT_SHIPPING, converter.convertToEntityAttribute(0));
        assertEquals(ShippingPolicyType.NON_CLIENT_SHIPPING, converter.convertToEntityAttribute(1));
    }

    @Test
    void testConvertToEntityAttributeInvalidValue() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute(99);
        });
        assertEquals("ShippingPolicyType으로 변환할 수 없습니다", thrown.getMessage());
    }
}
