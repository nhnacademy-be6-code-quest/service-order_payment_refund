package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.CommonOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommonOrderServiceImpl implements CommonOrderService {

    private ProductOrderDetailRepository productOrderDetailRepository;
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    @Override
    public void createOrderProductDetailAndOption(Order order, OrderedProductAndOptionProductPairDto productAndOptionProductPair) {
        ProductOrderDetail productOrderDetail = ProductOrderDetail.builder()
                .order(order)
                .productId(productAndOptionProductPair.productId())
                .quantity(productAndOptionProductPair.quantity())
                .pricePerProduct(productAndOptionProductPair.productSinglePrice())
                .build();
        productOrderDetailRepository.save(productOrderDetail);
        ProductOrderDetailOption productOrderDetailOption = ProductOrderDetailOption.builder()
                .productId(productAndOptionProductPair.optionProductId())
                .productOrderDetail(productOrderDetail)
                .optionProductName(productAndOptionProductPair.optionProductName())
                .optionProductPrice(productAndOptionProductPair.optionProductSinglePrice())
                .build();
        productOrderDetailOptionRepository.save(productOrderDetailOption);
    }
}
