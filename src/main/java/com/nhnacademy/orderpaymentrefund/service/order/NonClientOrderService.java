package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.UpdateNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * 비회원 주문 서비스 인터페이스
 *
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

public interface NonClientOrderService {

    void saveNonClientTemporalOrder(HttpHeaders headers, NonClientOrderForm requestDto);

    void saveNonClientOrder(Order order, NonClientOrderForm nonClientOrderForm);

    NonClientOrderForm getNonClientTemporalOrder(HttpHeaders headers, String orderCode);

    /**
     * findNonClientOrderId : 비회원이 주문 번호를 잃어버렸을 때, 주문번호를 찾을 수 있는 서비스
     *
     * @param findNonClientOrderIdRequestDto 주문번호를 찾기 위해 필요한 데이터
     **/
    List<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(HttpHeaders headers,
        FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto);

    /**
     * getOrder : 비회원 주문 단건 조회
     **/
    NonClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId, String orderPassword);

    /**
     * getOrder : 비회원 주문 비밀번호 수정
     *
     * @param orderId     비밀번호를 바꿀 주문 아이디
     * @param updateNonClientOrderPasswordRequestDto 새로운 비밀번호를 적용하기 위한 데이터
     **/
    void updateNonClientOrderPassword(HttpHeaders headers, long orderId,
        UpdateNonClientOrderPasswordRequestDto updateNonClientOrderPasswordRequestDto);

}
