# service-order_payment_refund API

</br>
</br>

### 주문 관련 회원 api

- 주문시도
    - POST  /client/order
    - 요청DTO
      ``` java
        public class ClientOrderPostRequestDto { // 회원이 주문 시도했을 때 필요한 dto
            private List<OrderItem> orderItemList;
        }
      
        public class OrderItem {
            private long productId;
            private long quantity;
        }
      ```
    - 응답DTO
      ``` java
        public class ClientOrderPostResponseDto { // 주문 페이지 화면에 뿌려질 데이터
            private List<ProductItemDto> productItemDtoList;
            private List<PackageItemDto> packageItemDtoList;
            private long shippingFee;
            private long minPurchasePrice;
            private String shippingPolicyName;
        }
  
        public class ProductItemDto {
            private long id;
            private String imgPath;
            private String name;
            private long price;
            private long quantity;
        }
  
        public class PackageItemDto {
            private long id;
            private String name;
            private long price;
        }
  
      ```
    - 상태코드
        - 성공시
            - OK 200
        - 실패시
            - 인증안된 사용자 접근: Unauthorized 401

- 주문조회
    - GET  /client/order
    - 요청DTO
      없습니다
    - 응답DTO
      ‼️ List<ClientAllOrderGetResponseDto> 응답됩니다 ‼️
      ``` java
        public class ClientAllOrderGetResponseDto {
          private ZonedDateTime orderDate; // 주문날짜
          private String address; // 배송지
          private List<OrderedProductDto> orderedProductDtoList; // 주문한 상품정보들
          private long totalProductPrice; // 상품 총 금액
          private long pointUsageAmount; // 사용 포인트 금액
          private String couponPolicyDescription; // 사용한 쿠폰 정책 이름
          private long couponDiscountAmount; // 쿠폰 할인된 금액
          private long shippingFee; // 배송비
          private long totalPayAmount; // 결제된 금액 or 결제할 금액
      }
   
      public class OrderedProductDto {
          private long productId; // 주문한 상품 아이디
          private String productName; // 주문한 상품 이름
          private String productImagePath; // 주문한 상품 이미지
          private long productPrice; // 주문당시 상품 단품 가격
          private long quantity; // 상품 수량
          private OrderStatus orderStatus; // 주문 상태
          private Long packageId; // 포장지 아이디
          private String packageName; // 포장지 이름
          private Long packagePrice; // 포장지 가격
      }
      ```
    - 상태코드
        - 성공시
            - OK 200
        - 실패시
            - 인증안된 사용자 접근: Unauthorized 401

### 주문 관련 관리자 api
- 주문조회
    - GET  /client/order
    - 요청DTO
      없습니다
    - 응답DTO
      ‼️ List<AdminAllOrdersGetResponseDto> 응답됩니다 ‼️
      ``` java
      public class AdminAllOrdersGetResponseDto {
          private long clientId; // 회원 아이디
          private String clientEmail; // 회원 이메일
          private ZonedDateTime orderDate; // 주문날짜
          private List<OrderedProductDto> orderedProductDtoList; // 주문한 상품정보들
      }
      
      public class OrderedProductDto {
          private long productId; // 주문한 상품 아이디
          private String productName; // 주문한 상품 이름
          private String productImagePath; // 주문한 상품 이미지
          private long productPrice; // 주문당시 상품 단품 가격
          private long quantity; // 상품 수량
          private OrderStatus orderStatus; // 주문 상태
          private Long packageId; // 포장지 아이디
          private String packageName; // 포장지 이름
          private Long packagePrice; // 포장지 가격
      }
      ```
    - 상태코드
        - 성공시
            - OK 200

- 주문수정
    - PUT  /admin/order/{orderId}
    - 요청DTO
      ``` java
      public class AdminOrderPutRequestDto { // 관리자가 주문에 대해 수정할 사항들.
          private OrderStatus orderStatus;
      }
      public enum OrderStatus{
          WAIT_PAYMENT("결제대기", "wait"), DELIVERING("배송중", "delivering"), COMPLETE("배송완료", "complete"), REFUND("반품", "refund"), CANCEL("주문취소", "cancel");
      }
      ```
    - 응답DTO
      String: "주문 정보가 수정되었습니다"
    - 상태코드
        - 성공시
            - OK 200

### 결제 관련 회원 API
- 결제하기
    - POST   /client/order/payment
    - 요청 DTO
    ```java
    public record PaymentCreateRequestPost(
    // 1. 결제할 때 사용할 포인트
    @NotNull
    Long point,

    // 2. 결제할 때 사용할 쿠폰
    Long couponId,
  
    // 3. 결제 방식
        @NotNull
        Long paymentMethodId
    ) {
  
    }
    ```
    - 응답 DTO
    ```java
    @Builder
    public record PaymentResponse(
        Long paymentId,
        Long orderId,
        LocalDateTime payTime,
        Long clientDeliveryAddressId,
        String paymentMethodName,
        Long couponId
    ) {
    
        public static PaymentResponse from(Payment payment) {
            return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getPayTime(),
                payment.getClientDeliveryAddressId(),
                payment.getPaymentMethod().getPaymentMethodName(),
                payment.getCouponId()
            );
        }
    }
    ```
  
### 환불 관련 회원 API
- 주문 취소 및 환불하기
  - POST /client/order/{orderId}/refund
  - 요청 DTO : 미완성입니다.
  - 응답 DTO : 미완성입니다.