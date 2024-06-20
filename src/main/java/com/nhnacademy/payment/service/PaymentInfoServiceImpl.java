//package com.nhnacademy.payment.service;
//
//import com.nhnacademy.payment.domain.Coupon;
//import com.nhnacademy.payment.dto.PaymentRequestDto;
//import com.nhnacademy.payment.feign.UserServiceClient;
//import com.nhnacademy.payment.feign.CouponServiceClient;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentInfoServiceImpl implements PaymentInfoService {
//
//    private final PointServiceClient pointServiceClient;
//    private final CouponServiceClient couponServiceClient;
//
//    @Autowired
//    public PaymentInfoServiceImpl(PointServiceClient pointServiceClient, CouponServiceClient couponServiceClient) {
//        this.pointServiceClient = pointServiceClient;
//        this.couponServiceClient = couponServiceClient;
//    }
//
//    @Override
//    public PaymentRequestDto getPaymentInfo(PaymentRequestDto paymentRequestDto) {
//        // 사용자의 포인트 정보 가져오기
//        Long userPoint = clientServiceClient.getUserPointByUserId(paymentRequestDto.userId());
//        paymentRequestDto = new PaymentRequestDto(userPoint, paymentRequestDto.couponList());
//
//        // 사용 가능한 쿠폰 리스트 가져오기
//        List<Coupon> availableCoupons = couponServiceClient.getAvailableCouponsByUserId(
//            paymentRequestDto.userId());
//        paymentRequestDto = new PaymentRequestDto(paymentRequestDto.point(), availableCoupons);
//
//        return paymentRequestDto;
//    }
//}
