package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientOrderPostResponseDto;

import java.util.List;

public interface ClientOrderService {
    ClientOrderPostResponseDto tryOrder(long clientId, ClientOrderPostRequestDto clientOrderPostRequestDto);
    List<ClientAllOrderGetResponseDto> getAllOrder(long clientId);
}
