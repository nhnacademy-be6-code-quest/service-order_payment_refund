package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientViewOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientViewOrderPostResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class ClientOrderController {

    private ClientOrderService clientOrderService;

    @PostMapping("/views/order")
    public ResponseEntity<ClientViewOrderPostResponseDto> viewOrderPage(@RequestBody ClientViewOrderPostRequestDto clientOrderPostRequestDto){

        long clientId = 1L;

        ClientViewOrderPostResponseDto responseDto = clientOrderService.orderView(clientId, clientOrderPostRequestDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody ClientOrderPostRequestDto clientOrderPostRequestDto){
        clientOrderService.createOrder(clientOrderPostRequestDto);
        return ResponseEntity.ok("주문이 생성되었습니다");
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
