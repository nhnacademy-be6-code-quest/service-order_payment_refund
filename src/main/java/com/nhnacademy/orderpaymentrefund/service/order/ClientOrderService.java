package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientViewOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientViewOrderPostResponseDto;

import java.util.List;

public interface ClientOrderService {
    ClientViewOrderPostResponseDto viewOrderPage(long clientId, ClientViewOrderPostRequestDto clientOrderPostRequestDto);
    List<ClientAllOrderGetResponseDto> getAllOrder(long clientId);
    void createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto);
}
