package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


/**
 * 환불 정보를 저장하고, 조회하는 컨트롤러입니다. 환불 정보는 수정되거나 삭제되지 않습니다.
 *
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;


}