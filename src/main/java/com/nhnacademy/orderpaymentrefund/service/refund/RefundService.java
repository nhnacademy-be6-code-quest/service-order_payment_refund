package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.client.refund.TossPayRefundClient;
import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.Refund;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import com.nhnacademy.orderpaymentrefund.dto.message.PointRewardRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsageRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.RefundCouponMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.product.InventoryIncreaseRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelPaymentCancel;
import com.nhnacademy.orderpaymentrefund.exception.ClientNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.RefundPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundRepository;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {

    private final RefundRepository refundRepository;
    private final RefundPolicyRepository refundPolicyRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final String tossSecretKey;
    private final TossPayRefundClient tossPayRefundClient;
    private final ClientOrderRepository clientOrderRepository;


    @Value("${rabbit.refund.coupon.exchange.name}")
    private String refundCouponExchangeName;
    @Value("${rabbit.refund.coupon.routing.key}")
    private String refundCouponRoutingKey;

    @Value("${rabbit.refund.point.exchange.name}")
    private String refundPointExchangeName;
    @Value("${rabbit.refund.point.routing.key}")
    private String refundPointRoutingKey;

    @Value("${rabbit.usedRefund.point.exchange.name}")
    private String refundUsedPointExchangeName;
    @Value("${rabbit.usedRefund.point.routing.key}")
    private String refundUsedPointRoutingKey;

    @Value("${rabbit.inventory.increase.exchange.name}")
    private String increasesExchange;
    @Value("${rabbit.inventory.increase.routing.key}")
    private String increaseKey;

    private static final String NO_ORDER = "주문이 존재하지 않습니다.";

    @PostConstruct
    public void init() {
        if (tossSecretKey.isEmpty()) {
            log.error("secretKey is empty");
        }
    }

    public RefundSuccessResponseDto saveRefund(RefundRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
            .orElseThrow(() -> new OrderNotFoundException(NO_ORDER));
        long optionProductFee = 0L;
        long refundAmount;
        List<ProductOrderDetail> productOrderDetails = productOrderDetailRepository.findAllByOrder_OrderId(
            order.getOrderId());
        for (ProductOrderDetail productOrderDetail : productOrderDetails) {
            List<ProductOrderDetailOption> productOrderDetailOptions = productOrderDetailOptionRepository.findByProductOrderDetail_ProductOrderDetailId(
                productOrderDetail.getProductOrderDetailId());
            for (ProductOrderDetailOption option : productOrderDetailOptions) {
                optionProductFee =
                    optionProductFee + option.getOptionProductPrice() * option.getQuantity();
            }
        }
        Payment payment = paymentRepository.findByOrder_OrderId(requestDto.getOrderId())
            .orElseThrow(() -> new PaymentNotFoundException("결재 정보를 찾을수없습니다."));
        RefundPolicy refundPolicy = refundPolicyRepository.findById(
                requestDto.getRefundPolicyId())
            .orElseThrow(() -> new RefundPolicyNotFoundException("환불정책이 존재하지 않습니다.")); //나중에바꿔야됨
        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(order.getOrderId()).orElseThrow(()-> new ClientNotFoundException("유저 정보가 존재하지 않습니다."));
        refundAmount = order.getOrderTotalAmount() - clientOrder.getDiscountAmountByPoint()
            - clientOrder.getDiscountAmountByCoupon() -
            optionProductFee - order.getShippingFee()- refundPolicy.getRefundShippingFee();
        Refund refund = new Refund(payment, refundPolicy,
            requestDto.getRefundDetailReason(),refundAmount);
        order.updateOrderStatus(OrderStatus.REFUND_REQUEST);
        orderRepository.save(order);
        refundRepository.save(refund);
        return new RefundSuccessResponseDto(refundAmount);
    }
    public void saveCancel(PaymentCancelRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
            .orElseThrow(() -> new OrderNotFoundException(NO_ORDER));
        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(order.getOrderId()).orElseThrow(()->new ClientNotFoundException("회원 정보가 존재하지 않습니다."));
        List<InventoryIncreaseRequestDto> inventoryIncreaseRequestDtos = new ArrayList<>();

        if (requestDto.getOrderStatus().equals(OrderStatus.PAYED.toString())) {
            order.updateOrderStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
            List<ProductOrderDetail> productOrderDetails = productOrderDetailRepository.findAllByOrder_OrderId(
                order.getOrderId());
            for (ProductOrderDetail productOrderDetail : productOrderDetails) {
                List<ProductOrderDetailOption> productOrderDetailOptions = productOrderDetailOptionRepository.findByProductOrderDetail_ProductOrderDetailId(
                    productOrderDetail.getProductOrderDetailId());
                InventoryIncreaseRequestDto inventoryIncreaseRequestDto1 = new InventoryIncreaseRequestDto(
                    productOrderDetail.getProductId(), productOrderDetail.getQuantity());
                inventoryIncreaseRequestDtos.add(inventoryIncreaseRequestDto1);

                for (ProductOrderDetailOption option : productOrderDetailOptions) {
                    InventoryIncreaseRequestDto inventoryIncreaseRequestDto2 = new InventoryIncreaseRequestDto(
                        option.getProductId(), option.getQuantity());
                    inventoryIncreaseRequestDtos.add(inventoryIncreaseRequestDto2);
                }
            }

            if (!inventoryIncreaseRequestDtos.isEmpty()) {
                rabbitTemplate.convertAndSend(increasesExchange, increaseKey,
                    inventoryIncreaseRequestDtos);
            }

            Long couponId = clientOrder.getCouponId();
            if (couponId != null) {
                rabbitTemplate.convertAndSend(refundCouponExchangeName, refundCouponRoutingKey,
                    new RefundCouponMessageDto(couponId));
            }
            Long payment = order.getOrderTotalAmount() - clientOrder.getDiscountAmountByPoint()
                - clientOrder.getDiscountAmountByCoupon();
            rabbitTemplate.convertAndSend(refundPointExchangeName, refundPointRoutingKey,
                new PointRewardRefundMessageDto(clientOrder.getClientId(), payment,
                    clientOrder.getDiscountAmountByPoint()));
            rabbitTemplate.convertAndSend(refundUsedPointExchangeName, refundUsedPointRoutingKey,
                new PointUsageRefundMessageDto(clientOrder.getClientId(),
                    clientOrder.getDiscountAmountByPoint()));
            //포인트 취소 적립포인트 사용처리 오더에서 사용포인트 돌려주고 적립으로 오더에서 구매금액 도려주고 결제에서
            // 포장지아이디 개수, 상품아이디 개수,

        } else {
            throw new CannotCancelPaymentCancel("주문 취소에 실패하였습니다.");
        }
    }



    //dto 오더아이디 취소이유 가격
    public RefundResultResponseDto refundUser(RefundAfterRequestDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        Payment paymentDto = paymentRepository.findByOrder_OrderId(dto.getOrderId()).orElseThrow(()-> new PaymentNotFoundException("결제 노존재"));
        Refund refund = refundRepository.findByPayment(paymentDto);
        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(order.getOrderId()).orElseThrow(()-> new OrderNotFoundException("존재하지 않습니다."));
        List<InventoryIncreaseRequestDto> inventoryIncreaseRequestDtos = new ArrayList<>();

        if (order.getOrderStatus().equals(OrderStatus.REFUND_REQUEST)) {

            List<ProductOrderDetail> productOrderDetails = productOrderDetailRepository.findAllByOrder_OrderId(
                order.getOrderId());
            for (ProductOrderDetail productOrderDetail : productOrderDetails) {
                InventoryIncreaseRequestDto inventoryIncreaseRequestDto = new InventoryIncreaseRequestDto(
                    productOrderDetail.getProductId(), productOrderDetail.getQuantity());
                inventoryIncreaseRequestDtos.add(inventoryIncreaseRequestDto);
            }

            // 모든 ProductOrderDetail을 처리한 후 한 번만 전송
            if (!inventoryIncreaseRequestDtos.isEmpty()) {
                rabbitTemplate.convertAndSend(increasesExchange, increaseKey,
                    inventoryIncreaseRequestDtos);
            }

            Long couponId = clientOrder.getCouponId();
            if (couponId != null) {
                rabbitTemplate.convertAndSend(refundCouponExchangeName, refundCouponRoutingKey,
                    new RefundCouponMessageDto(couponId));
            }
            Long payment = refund.getRefundAmount();
            rabbitTemplate.convertAndSend(refundPointExchangeName, refundPointRoutingKey,
                new PointRewardRefundMessageDto(clientOrder.getClientId(), payment,
                    clientOrder.getDiscountAmountByPoint()));
            rabbitTemplate.convertAndSend(refundUsedPointExchangeName, refundUsedPointRoutingKey,
                new PointUsageRefundMessageDto(clientOrder.getClientId(),
                    clientOrder.getDiscountAmountByPoint()));
            order.updateOrderStatus(OrderStatus.REFUND);
            orderRepository.save(order);
        }
        return new RefundResultResponseDto(refund.getRefundDetailReason(), paymentDto.getPaymentMethodType().getPaymentMethodTypeName());
    }
    public PaymentMethodResponseDto findPayMethod (long orderId){
        Payment payment = paymentRepository.findByOrder_OrderId(orderId).orElseThrow(()->new PaymentNotFoundException("결재가 존재하지 않습니다."));
        return PaymentMethodResponseDto.builder()
            .methodType(payment.getPaymentMethodType().getPaymentMethodTypeName()).build();
    }
}
