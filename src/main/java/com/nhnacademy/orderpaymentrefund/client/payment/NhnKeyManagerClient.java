package com.nhnacademy.orderpaymentrefund.client.payment;

import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * NHN KeyManager 에서 Toss Payments 에 사용 될 secretKey 를 받아 오기 위해 외부 API 를 호출하는 @FeignClient 클래스입니다.
 * NHN KeyManager 에 IP 가 등록 되어 있지 않다면 값을 가져올 수 없습니다. Toss Payments 에서 승인을 받지 못했다면, NHN KeyManager 에
 * IP 등록이 되어있지 않거나 랜선 연결이 되어있지 않을 수 있습니다.
 *
 * @author 김채호
 * @version 1.0
 */
@FeignClient(name = "nhnKeyManagerClient", url = "https://api-keymanager.nhncloudservice.com/keymanager/v1.0/appkey")
public interface NhnKeyManagerClient {
    /**
     * NHN KeyManager 에서 Toss Payments 에 사용될 secretKey 를 가져오는 메서드입니다.
     *
     * @return Toss Payments 에 사용될 secretKey
     */

    @GetMapping("/{appKey}/secrets/{keyId}")
    JSONObject getTossSecretKey(@PathVariable("appKey") String appKey, @PathVariable("keyId") String keyId);

}