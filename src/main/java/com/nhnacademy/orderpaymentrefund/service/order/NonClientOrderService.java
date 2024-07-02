package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

/**
 * 비회원 주문 서비스 인터페이스
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

public interface NonClientOrderService {

    /**
     * tryCreateOrder : 주문 생성을 위한 메서드. preprocessing(), createOrder(), postprocessing()이 호출됨.
     * @param requestDto 비회원 주문을 생성하기 위한 요청 dto
     **/
    URI tryCreateOrder(CreateNonClientOrderRequestDto requestDto);

    /**
     * preprocessing : 주문 생성 전 '전처리'를 위한 메서드
     **/
    void preprocessing();

    /**
     * postprocessing : 주문 생성 전 '전처리'를 위한 메서드
     **/
    void postprocessing();

    /**
     * createOrder : 주문생성 메서드
     * @param requestDto 주문 생성에 필요한 데이가 담긴 요청 Dto
     **/
    void createOrder(CreateNonClientOrderRequestDto requestDto);

    /**
     * findNonClientOrderByOrderId : 비회원이 입력한 OrderId에 대해 주문 조회 결과를 반환하는 메서드 - 단건
     * @param orderId 비회원이 조회하고자 하는 주문 아이디
     * @param orderPassword 주문 아이디에 해당하는 주문 비밀번호
     **/
    FindNonClientOrderResponseDto findNonClientOrder(long orderId, String orderPassword);

    /**
     * findNonClientOrderId : 비회원이 주문 번호를 잃어버렸을 때, 주문번호를 찾을 수 있는 서비스
     * @param findNonClientOrderIdRequestDto 주문번호를 찾기 위해 필요한 데이터
     **/
    Page<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable);

    /**
     * findNonClientOrderPassword : 비회원이 주문 비밀번호를 잃어버렸을 때, 주문 비밀번호를 찾을 수 있는 서비스
     * @param findNonClientOrderPasswordRequestDto 주문 비밀번호르 찾기 위해 필요한 데이터
     **/
    String findNonClientOrderPassword(FindNonClientOrderPasswordRequestDto findNonClientOrderPasswordRequestDto);
}
