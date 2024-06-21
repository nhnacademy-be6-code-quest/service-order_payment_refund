package com.nhnacademy.order.controller.order;

import com.nhnacademy.order.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.order.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.order.dto.order.response.client.ClientOrderPostResponseDto;
import com.nhnacademy.order.service.ClientOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/order")
@AllArgsConstructor
public class ClientOrderController {

    private ClientOrderService clientOrderService;

    @PostMapping
    public ResponseEntity<ClientOrderPostResponseDto> tryOrder(@RequestBody ClientOrderPostRequestDto clientOrderPostRequestDto){

//        String headerValue = request.getHeader("id");
//        if(Objects.isNull(headerValue)) throw new NeedToAuthenticationException();
//
//        long clientId = Integer.parseInt(headerValue);
        long clientId = 1L;
        ClientOrderPostResponseDto responseDto = clientOrderService.tryOrder(clientId, clientOrderPostRequestDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @GetMapping
    public ResponseEntity<List<ClientAllOrderGetResponseDto>> getAllOrders(HttpServletRequest request){

//        String headerValue = request.getHeader("id");
//        if(Objects.isNull(headerValue)) throw new NeedToAuthenticationException();
//
//        long clientId = Integer.parseInt(request.getHeader("id"));
        long clientId = 1L;
        List<ClientAllOrderGetResponseDto> responseDto = clientOrderService.getAllOrder(clientId);

        return ResponseEntity.ok().body(responseDto);

    }

}
