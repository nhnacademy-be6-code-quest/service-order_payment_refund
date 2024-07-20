package com.nhnacademy.orderpaymentrefund.service.payment.impl;//package com.nhnacademy.codequestweb.service.payment;
//
//import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
//import com.nhnacademy.codequestweb.request.payment.PaymentOrderValidationRequestDto;
//import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
//import jakarta.annotation.PostConstruct;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.ParseException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Service;
//
///**
// *
// */
//@Service
//public interface PaymentService {
//
//    /**
//     * PostConstruct : 의존성 주입 완료 후 실행해야 하는 메서드에 적용하는 애너테이션입니다. 이 메서드는 IP가 NHN KeyManager 에 등록되어 있지
//     * 않는 등의 사유로 토스 시크릿 키가 없다면 시크릿 키가 없다는 에러 로그를 띄웁니다.
//     */
//    @PostConstruct
//    void init();
//
//    /**
//     * 결제 관련 정보를 DB에 저장하기 위한 메서드입니다.
//     *
//     * @param orderId                 주문 아이디
//     * @param tossPaymentsResponseDto 토스 페이먼츠에서 넘어 오는 값들을 파싱한 Dto
//     */
//    void savePayment(long orderId, TossPaymentsResponseDto tossPaymentsResponseDto);
//
//    /**
//     * 사용자가 결제 관련 로직을 수행한 이후, 토스 페이먼츠에 승인을 보내기 전, 사용자가 금액 등을 조작하지는 않았는지 검증하는 메서드입니다.
//     *
//     * @param paymentOrderValidationRequestDto 조작 여부를 확인하기 위해, 주문에서 금액, 토스 주문 아이디 등을 받아 옵니다.
//     * @param tossOrderId                      토스에서 제공하는 오더 아이디입니다. 주문에서 받아온 값과 비교하는 데에 사용됩니다.
//     * @param amount                           토스에서 제공하는 결제 금액입니다. 주문에서 받아온 값과 비교하는 데에 사용됩니다.
//     * @return 유효하다면 true, 그렇지 않다면 false 값을 반환합니다.
//     */
//    boolean isValidTossPayment(PaymentOrderValidationRequestDto paymentOrderValidationRequestDto,
//        String tossOrderId, long amount);
//
//    /**
//     * 토스 페이먼츠에 결제 승인을 요청하는 메서드입니다.
//     *
//     * @param tossOrderId 토스 주문 아이디입니다. 토스 페이먼츠에 결제 승인을 요청하기 위해 보내야 하는 값입니다.
//     * @param amount      결제 총 금액입니다. 토스 페이먼츠에 결제 승인을 요청하기 위해 보내야 하는 값입니다.
//     * @param paymentKey  결제 키 입니다. 토스 페이먼츠에 결제 승인을 요청하기 위해 보내야 하는 값입니다.
//     * @return 토스 페이먼츠에서 준 JSON 값을 받아 옵니다.
//     * @throws ParseException 토스 페이먼츠에서 준 값을 JSON 을 String 으로 받은 후, 다시 JSON 으로 바꾸기 때문에 필요합니다. (Dto
//     *                        생성 및 에러 피하기 위해 )
//     */
//    JSONObject approvePayment(String tossOrderId, long amount, String paymentKey)
//        throws ParseException;
//
//    /**
//     * 토스 페이먼츠에서 받아 온 응답을 파싱하는 메서드입니다.
//     *
//     * @param jsonObject 토스 페이먼츠에서 응답으로 제공되는 값입니다. 매우 다양한 값이 들어 있습니다.
//     *                   [링크](https://docs.tosspayments.com/reference#payment-%EA%B0%9D%EC%B2%B4)
//     * @return Dto 에 토스 페이먼츠에서 응답으로 제공하는 값을 추가하여 반환합니다.
//     */
//    TossPaymentsResponseDto parseJSONObject(JSONObject jsonObject);
//
//    /**
//     * 토스 결제창에 정확한 값을 보여 주기 위해서 주문 서비스에 PaymentOrderRequestDto 를 요청하는 메서드입니다.
//     *
//     * @param orderId PathVariable 을 통해 가져온 주문 아이디입니다.
//     * @return 토스 결제창에 정확한 값을 보여 주기 위한 Dto 입니다.
//     */
//    PaymentOrderRequestDto findPaymentOrderRequestDtoByOrderId(long orderId);
//
//    /**
//     * 토스 결제창에 사용자의 이름을 추가하기 위해서 회원 서비스에 회원의 이름을 요청하는 메서드입니다.
//     *
//     * TODO JAVADOC
//     * @return
//     */
//    String findClientNameByHttpHeaders(HttpHeaders httpHeaders);
//}
