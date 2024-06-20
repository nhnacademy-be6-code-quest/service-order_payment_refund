package com.nhnacademy.payment.service;

import com.nhnacademy.payment.dto.PaymentRequestDto;

public interface PaymentInfoService {

    /**
     * 결제 과정에서 사용자에게 제공되어야 하는 정보를 가져옵니다. 사용자의 포인트 정보와 사용 가능한 쿠폰 리스트를 반환합니다. PaymentService 에서 추가할
     * 생각도 있었지만, 사용자에게 제공되는 정보는 결제의 CRUD 와는 무관하다고 생각되어 별도의 인터페이스를 만들어 뒀습니다.
     *
     * @param paymentRequestDto 결제 요청 정보
     * @return 사용자 포인트와 쿠폰 리스트
     */
    PaymentRequestDto getPaymentInfo(PaymentRequestDto paymentRequestDto);
}
