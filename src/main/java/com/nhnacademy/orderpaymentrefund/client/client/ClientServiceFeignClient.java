package com.nhnacademy.orderpaymentrefund.client.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "client", url = "http://localhost:8001")
public interface ClientServiceFeignClient {

}
