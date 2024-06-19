package com.nhnacademy.order.service;

import com.nhnacademy.order.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.order.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.order.dto.order.response.client.ClientOrderPostResponseDto;

import java.util.List;

public interface ClientOrderService {
    ClientOrderPostResponseDto tryOrder(long clientId, ClientOrderPostRequestDto clientOrderPostRequestDto);
    List<ClientAllOrderGetResponseDto> getAllOrder(long clientId);
}
