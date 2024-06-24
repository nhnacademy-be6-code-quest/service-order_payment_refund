package com.nhnacademy.orderpaymentrefund.service.shipping;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.AdminShippingPolicyPutRequestDto;

public interface ShippingPolicyService {
    void updateShippingPolicy(AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto);
    ShippingPolicy getShippingPolicy();
}
