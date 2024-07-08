package com.nhnacademy.orderpaymentrefund.client.client;

import com.nhnacademy.orderpaymentrefund.client.client.response.BookProductGetResponseDto;
import jakarta.validation.constraints.Min;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product", url = "http://localhost:8001")
public interface ProductServiceFeignClient {
    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(@Min(1) @PathVariable long bookId);
}
