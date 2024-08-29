package com.nhnacademy.orderpaymentrefund.client.payment;

import com.nhnacademy.orderpaymentrefund.service.payment.impl.TossPGServiceStrategy.TossApprovePaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 토스 페이먼츠 API 와 통신하는 클라이언트 역할을 하는 클래스입니다. 결제 승인 요청을 보내는 기능을 제공합니다. 고객이 결제 창에서 화면을 보는 것과 토스 페이먼츠에서
 * 결제를 승인하는 것은 다른 로직입니다. 이 API 에서는 결제가 성공 창으로 이동한 이후에 실제 고객의 돈을 받아 오기 전, 승인 요청을 보냅니다.
 *
 * @author 김채호
 * @version 1.0
 */
@FeignClient(name = "tossPaymentsClient", url = "https://api.tosspayments.com/v1/payments/confirm")
public interface TossPaymentsClient {

    /**
     * 토스 페이먼츠에 결제 승인 요청을 보냅니다.
     *
     * @param tossApprovePaymentRequestDto 토스 페이먼츠 결제 승인 요청에 필요한 정보를 담고 있는 DTO 입니다.
     * @param authorization          토스 페이먼츠 API 인증을 위한 헤더 값입니다. 토스 페이먼츠에서 키를 인코딩 한 값입니다.
     * @return 결제 승인 요청 결과를 나타내는 문자열이 반환됩니다. 이후 서비스에서 파싱합니다.
     */
    @PostMapping
    String approvePayment(@RequestBody TossApprovePaymentRequestDto tossApprovePaymentRequestDto,
        @RequestHeader(name = "Authorization") String authorization);
}

