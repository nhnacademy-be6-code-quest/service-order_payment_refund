//package com.nhnacademy.orderpaymentrefund.order.controller;
//
//import com.nhnacademy.orderpaymentrefund.controller.order.ClientOrderController;
//import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
//import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(ClientOrderController.class)
//public class ClientOrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private ClientOrderService clientOrderService;
//
//    private ClientOrderCreateForm clientOrderCreateForm;
//
//    @BeforeEach
//    public void setup(){
//
//        ClientOrderCreateForm.OrderDetailDtoItem item1 = ClientOrderCreateForm.OrderDetailDtoItem.builder()
//            .productId(1L)
//            .productName("탕후루")
//            .quantity(2L)
//            .categoryIdList(new ArrayList<>(List.of(3L, 9L)))
//            .packableProduct(true)
//            .productSinglePrice(10000L)
//            .build();
//
//        ClientOrderCreateForm.OrderDetailDtoItem item2 = ClientOrderCreateForm.OrderDetailDtoItem.builder()
//            .productId(3L)
//            .productName("탕비실")
//            .quantity(1L)
//            .categoryIdList(new ArrayList<>(List.of(1L, 5L)))
//            .packableProduct(true)
//            .productSinglePrice(14000L)
//            .build();
//
//        clientOrderCreateForm = ClientOrderCreateForm.builder()
//            .couponId(1L)
//            .shippingFee(3000)
//            .productTotalAmount(24000L)
//            .payAmount(2L)
//            .couponDiscountAmount(1000L)
//            .usedPointDiscountAmount(1000L)
//            .orderedPersonName("박희원")
//            .phoneNumber("01012341234")
//            .addressNickname("")
//            .addressZipCode()
//            .deliveryAddress()
//            .useDesignatedDeliveryDate()
//            .designatedDeliveryDate()
//            .paymentMethod()
//            .accumulatePoint()
//            .tossOrderId()
//            .build();
//
//        clientOrderCreateForm.addOrderDetailDtoItem(item1);
//        clientOrderCreateForm.addOrderDetailDtoItem(item2);
//
//    }
//
//}
