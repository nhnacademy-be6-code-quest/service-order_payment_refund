package com.nhnacademy.orderpaymentrefund.controller.shipping;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response.ShippingPolicyGetResponseDto;
import com.nhnacademy.orderpaymentrefund.service.shipping.ShippingPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingPolicyService shippingPolicyService;

    @PutMapping
    public ResponseEntity<String> updateShippingPolicy(@RequestBody AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto){
        shippingPolicyService.updateShippingPolicy(adminShippingPolicyPutRequestDto);
        return ResponseEntity.ok("배송정책이 수정되었습니다.");
    }

    @GetMapping
    public ResponseEntity<ShippingPolicyGetResponseDto> getShippingPolicy(@RequestParam(name = "type", required = true) String type){
        return ResponseEntity.ok().body(shippingPolicyService.getShippingPolicy(ShippingPolicyType.of(type)));
    }

}
