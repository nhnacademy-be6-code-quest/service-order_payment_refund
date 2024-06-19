package com.nhnacademy.order.controller.order;

import com.nhnacademy.order.dto.order.request.admin.AdminOrderPutRequestDto;
import com.nhnacademy.order.dto.order.response.admin.AdminAllOrdersGetResponseDto;
import com.nhnacademy.order.service.AdminOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
@AllArgsConstructor
public class AdminOrderController {

    private AdminOrderService adminOrderService;

    @GetMapping
    // 회원이 주문한 모든 주문건 확인하기
    public ResponseEntity<List<AdminAllOrdersGetResponseDto>> getAllOrders(){
        List<AdminAllOrdersGetResponseDto> adminAllOrdersGetResponseDto = adminOrderService.getAllOrders();
        return ResponseEntity.ok().body(adminAllOrdersGetResponseDto);
    }

    @PutMapping("/{orderId}")
    // 관리자가 회원의 주문 건에 대해 '배송중'으로 상태 바꾸기
    public ResponseEntity<String> updateOrder(@PathVariable long orderId, @RequestBody AdminOrderPutRequestDto adminOrderPutRequestDto){
        adminOrderService.updateOrder(orderId, adminOrderPutRequestDto);
        return ResponseEntity.ok().body("주문 정보가 수정되었습니다");
    }

}
