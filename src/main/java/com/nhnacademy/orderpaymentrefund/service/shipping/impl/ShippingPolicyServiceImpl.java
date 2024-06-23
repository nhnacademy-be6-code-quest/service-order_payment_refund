package com.nhnacademy.orderpaymentrefund.service.shipping.impl;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.shipping.ShippingPolicyRepository;
import com.nhnacademy.orderpaymentrefund.service.shipping.ShippingPolicyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShippingPolicyServiceImpl implements ShippingPolicyService {

    private ShippingPolicyRepository shippingPolicyRepository;

    @Override
    public void updateShippingPolicy(AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto) {

        ShippingPolicy shippingPolicy = getShippingPolicy();

        shippingPolicy.updatePost(adminShippingPolicyPutRequestDto.getDescription()
                , adminShippingPolicyPutRequestDto.getFee()
                , adminShippingPolicyPutRequestDto.getLowerBound());

        shippingPolicyRepository.save(shippingPolicy);

    }

    @Override
    public ShippingPolicy getShippingPolicy() {

        ShippingPolicy shippingPolicy = shippingPolicyRepository.findAll().getFirst();

        if(shippingPolicy == null) throw new ShippingPolicyNotFoundException();

        return shippingPolicy;

    }
}
