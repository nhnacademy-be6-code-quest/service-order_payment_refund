package com.nhnacademy.orderpaymentrefund.client.product;

import com.nhnacademy.orderpaymentrefund.dto.client.ProductGetNameAndPriceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "client", url = "http://localhost:8001")
public interface ProductClient {

    @GetMapping("/api/product/single/{productId}")
    ResponseEntity<ProductGetNameAndPriceResponseDto> getSingleProductNameAndPriceSales(
        @RequestHeader HttpHeaders headers, @PathVariable("productId") Long productId
    );

}
