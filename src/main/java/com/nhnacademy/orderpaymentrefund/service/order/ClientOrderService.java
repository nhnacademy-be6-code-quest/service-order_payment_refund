package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientViewOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientOrderPostResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientViewOrderPostResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientOrderService {
    ClientViewOrderPostResponseDto orderView(long clientId, ClientViewOrderPostRequestDto clientOrderPostRequestDto);
    Page<ClientAllOrderGetResponseDto> getAllOrder(long clientId, Pageable pageable);
    ClientOrderPostResponseDto createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto, HttpServletResponse httpServletResponse);
}
