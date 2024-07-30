package com.nhnacademy.orderpaymentrefund.domain.payment;

import com.nhnacademy.orderpaymentrefund.domain.RefundPolicyStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RefundPolicyStatusTest {

    @Test
    void testActivateStatus() {
        RefundPolicyStatus status = RefundPolicyStatus.ACTIVATE;

        assertEquals(0, status.getCode(), "ACTIVATE 상태의 코드 값이 올바르지 않습니다.");
        assertEquals("활성", status.getValue(), "ACTIVATE 상태의 문자열 값이 올바르지 않습니다.");
    }

    @Test
    void testDisabledStatus() {
        RefundPolicyStatus status = RefundPolicyStatus.DISABLED;

        assertEquals(1, status.getCode(), "DISABLED 상태의 코드 값이 올바르지 않습니다.");
        assertEquals("비활성화", status.getValue(), "DISABLED 상태의 문자열 값이 올바르지 않습니다.");
    }

    @Test
    void testEnumValues() {
        // 모든 열거형 값들이 올바르게 정의되어 있는지 확인합니다.
        RefundPolicyStatus[] statuses = RefundPolicyStatus.values();

        assertEquals(2, statuses.length, "열거형 값의 수가 올바르지 않습니다.");
        assertEquals(RefundPolicyStatus.ACTIVATE, statuses[0], "ACTIVATE 상태가 첫 번째 위치에 있어야 합니다.");
        assertEquals(RefundPolicyStatus.DISABLED, statuses[1], "DISABLED 상태가 두 번째 위치에 있어야 합니다.");
    }

    @Test
    void testEnumName() {
        assertEquals("ACTIVATE", RefundPolicyStatus.ACTIVATE.name(), "ACTIVATE 상태의 이름이 올바르지 않습니다.");
        assertEquals("DISABLED", RefundPolicyStatus.DISABLED.name(), "DISABLED 상태의 이름이 올바르지 않습니다.");
    }
}
