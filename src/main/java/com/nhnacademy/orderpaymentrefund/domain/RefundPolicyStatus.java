package com.nhnacademy.orderpaymentrefund.domain;

import lombok.Getter;

@Getter
public enum RefundPolicyStatus {
    ACTIVATE(0,"활성"),DISABLED(1,"비활성화");
    private final int code;
    private final String value;

    RefundPolicyStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
