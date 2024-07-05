package com.nhnacademy.orderpaymentrefund.service.shipping;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response.ShippingPolicyGetResponseDto;

import java.util.List;

public interface ShippingPolicyService {
    void updateShippingPolicy(AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto);
    ShippingPolicyGetResponseDto getShippingPolicy(ShippingPolicyType shippingPolicyType);
    List<ShippingPolicyGetResponseDto> getAllShippingPolicies();
}
