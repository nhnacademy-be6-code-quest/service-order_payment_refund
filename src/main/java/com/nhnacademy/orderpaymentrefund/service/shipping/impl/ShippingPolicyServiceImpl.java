package com.nhnacademy.orderpaymentrefund.service.shipping.impl;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response.ShippingPolicyGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.shipping.ShippingPolicyRepository;
import com.nhnacademy.orderpaymentrefund.service.shipping.ShippingPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingPolicyServiceImpl implements ShippingPolicyService {

    private final ShippingPolicyRepository shippingPolicyRepository;

    @Override
    public void updateShippingPolicy(AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto) {

        ShippingPolicy shippingPolicy = shippingPolicyRepository.findByShippingPolicyType(adminShippingPolicyPutRequestDto.shippingPolicyType()).orElseThrow(ShippingPolicyNotFoundException::new);

        shippingPolicy.updateShippingPolicy(adminShippingPolicyPutRequestDto.description()
                , adminShippingPolicyPutRequestDto.shippingFee()
                , adminShippingPolicyPutRequestDto.minPurchaseAmount());

        shippingPolicyRepository.save(shippingPolicy);

    }

    @Override
    public ShippingPolicyGetResponseDto getShippingPolicy(ShippingPolicyType shippingPolicyType) {

        ShippingPolicy shippingPolicy = shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType).orElseThrow(ShippingPolicyNotFoundException::new);

        return ShippingPolicyGetResponseDto.builder()
                .shippingFee(shippingPolicy.getShippingFee())
                .description(shippingPolicy.getDescription())
                .minPurchaseAmount(shippingPolicy.getMinPurchaseAmount())
                .shippingPolicyType(shippingPolicy.getShippingPolicyType())
                .build();

    }

    @Override
    public List<ShippingPolicyGetResponseDto> getAllShippingPolicies() {
        return shippingPolicyRepository.findAll().stream()
            .map(shippingPolicy -> ShippingPolicyGetResponseDto.builder()
                .description(shippingPolicy.getDescription())
                .shippingFee(shippingPolicy.getShippingFee())
                .minPurchaseAmount(shippingPolicy.getMinPurchaseAmount())
                .shippingPolicyType(shippingPolicy.getShippingPolicyType())
                .build())
            .toList();
    }
}
