package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

/**
 * 비회원 주문 서비스 인터페이스
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

public interface NonClientOrderService {

    void saveNonClientTemporalOrder(HttpHeaders headers, NonClientOrderForm requestDto);

    NonClientOrderForm getNonClientTemporalOrder(HttpHeaders headers, String tossOrderId);

    /**
     * findNonClientOrderId : 비회원이 주문 번호를 잃어버렸을 때, 주문번호를 찾을 수 있는 서비스
     * @param findNonClientOrderIdRequestDto 주문번호를 찾기 위해 필요한 데이터
     **/
    Page<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(HttpHeaders headers, FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable);

    /**
     * findNonClientOrderPassword : 비회원이 주문 비밀번호를 잃어버렸을 때, 주문 비밀번호를 찾을 수 있는 서비스
     * @param findNonClientOrderPasswordRequestDto 주문 비밀번호르 찾기 위해 필요한 데이터
     **/
    String findNonClientOrderPassword(HttpHeaders headers, FindNonClientOrderPasswordRequestDto findNonClientOrderPasswordRequestDto);

    /**
     * getOrder : 비회원 주문 단건 조회
     **/
    NonClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId, String orderPassword);

    void paymentCompleteOrder(HttpHeaders headers, long orderId);

    void cancelOrder(HttpHeaders headers, long orderId);

    void refundOrder(HttpHeaders headers, long orderId);
}
