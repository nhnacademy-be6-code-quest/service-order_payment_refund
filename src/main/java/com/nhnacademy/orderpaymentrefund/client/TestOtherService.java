package com.nhnacademy.orderpaymentrefund.client;

import com.nhnacademy.orderpaymentrefund.exception.*;
import org.springframework.stereotype.Service;

@Service
public class TestOtherService {

    // 전처리 ----------------------------------------------------------------------

    // 재고확인
    public void checkStock(boolean res){
        if(!res){
            throw new FailToPreProcessing("해당 상품의 재고가 없습니다");
        }
    }

    // 쿠폰 요효성 검사
    public void checkCouponAvailability(boolean res){
        if(!res){
            throw new FailToPreProcessing("쿠폰 사용 조건에 맞지 않거나 유효하지 않는 쿠폰입니다");
        }
    }

    // 포인트 유효성 검사
    public void checkPointAvailability(boolean res){
        if(!res){
            throw new FailToPreProcessing("포인트를 사용할 수 없습니다");
        }
    }

    // 적립금 유효성 검사
    public void checkAccumulatePointAvailability(boolean res){
        if(!res){
            throw new FailToPreProcessing("적립 금액이 유효하지 않습니다");
        }
    }

    // 후처리 ----------------------------------------------------------------------

    public void processDroppingStock(boolean res){
        if(!res){
            throw new FailToPostProcessing("재고 감소에 실패하였습니다");
        }
    }

    public void processUsedCoupon(boolean res){
        if(!res){
            throw new FailToPostProcessing("쿠폰 사용 처리에 실패하였습니다");
        }
    }

    public void processUsedPoint(boolean res){
        if(!res){
            throw new FailToPostProcessing("포인트 사용 처리에 실패하였습니다");
        }
    }

    public void processAccumulatePoint(boolean res){
        if(!res){
            throw new FailToPostProcessing("포인트 적립에 실팽하였습니다");
        }
    }


}
