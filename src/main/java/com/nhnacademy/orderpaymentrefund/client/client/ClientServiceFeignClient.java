package com.nhnacademy.orderpaymentrefund.client.client;

import com.nhnacademy.orderpaymentrefund.dto.client.ClientUpdateGradeRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "client", url = "http://localhost:8001")
public interface ClientServiceFeignClient {
    @PutMapping("/api/client/grade")
    ResponseEntity<String> updateClientGrade(@RequestBody ClientUpdateGradeRequestDto clientUpdateGradeRequestDto);
}
