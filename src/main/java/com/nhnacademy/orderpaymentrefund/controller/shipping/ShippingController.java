package com.nhnacademy.orderpaymentrefund.controller.shipping;

import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.service.shipping.ShippingPolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin")
public class ShippingController {

    private ShippingPolicyService shippingPolicyService;

    @PutMapping
    public ResponseEntity<String> updateShippingPolicy(@RequestBody AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto){
        shippingPolicyService.updateShippingPolicy(adminShippingPolicyPutRequestDto);
        return ResponseEntity.ok("배송정책이 수정되었습니다.");
    }

}
