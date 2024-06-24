package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientViewOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientOrderPostResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientViewOrderPostResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
public class ClientOrderController {

    private ClientOrderService clientOrderService;

    @PostMapping("/client/views/order")
    public ResponseEntity<ClientViewOrderPostResponseDto> viewOrder(@RequestBody ClientViewOrderPostRequestDto clientOrderPostRequestDto){

        long clientId = 1L;

        ClientViewOrderPostResponseDto responseDto = clientOrderService.orderView(clientId, clientOrderPostRequestDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @PostMapping("/client/order")
    public ResponseEntity<ClientOrderPostResponseDto> createOrder(@RequestBody ClientOrderPostRequestDto clientOrderPostRequestDto, HttpServletResponse httpServletResponse){
        ClientOrderPostResponseDto responseDto = clientOrderService.createOrder(clientOrderPostRequestDto, httpServletResponse);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(responseDto.orderId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientAllOrderGetResponseDto>> getAllOrders(HttpServletRequest request, Pageable pageable){

//        String headerValue = request.getHeader("id");
//        if(Objects.isNull(headerValue)) throw new NeedToAuthenticationException();
//
//        long clientId = Integer.parseInt(request.getHeader("id"));
        long clientId = 1L;
        Page<ClientAllOrderGetResponseDto> responseDto = clientOrderService.getAllOrder(clientId, pageable);

        return ResponseEntity.ok().body(responseDto);

    }

}
