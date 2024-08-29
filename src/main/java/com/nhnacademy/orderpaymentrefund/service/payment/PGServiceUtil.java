package com.nhnacademy.orderpaymentrefund.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderForm;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGServiceUtil {

    private static final String REDIS_ORDER_KEY = "order";

    private final ClientHeaderContext clientHeaderContext;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public String getOrderHistoryTitle(OrderForm orderForm) {
        if(orderForm == null){
            throw new OrderNotFoundException();
        }

        StringBuilder orderHistoryTitle = new StringBuilder();
        orderHistoryTitle.append(orderForm.getOrderItemList().getFirst().getProductName());

        if(orderForm.getOrderDetailSize() > 1) {
            orderHistoryTitle.append(String.format("외 %d개", orderForm.getOrderDetailSize() - 1));
        }

        return orderHistoryTitle.toString();
    }

    public OrderForm getOrderForm(String orderCode) {
        Object data = redisTemplate.opsForHash().get(REDIS_ORDER_KEY, orderCode);
        OrderForm orderForm = null;

        if(clientHeaderContext.isClient()){
            orderForm = objectMapper.convertValue(data, ClientOrderCreateForm.class);
        }
        else{
            orderForm = objectMapper.convertValue(data, NonClientOrderForm.class);
        }
        return orderForm;
    }
}
