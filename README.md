# service-order_payment_refund API

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
    ‼️ List&lt;ClientAllOrderGetResponseDto&gt; 응답됩니다 ‼️
    ``` java
      public class ClientAllOrderGetResponseDto {
        private ZonedDateTime orderDate;
        private String address;
        private List<OrderedProductDto> orderedProductDtoList;
        private long totalProductPrice;
        private long pointUsageAmount;
        private String couponPolicyDescription;
        private long couponDiscountAmount;
        private long shippingFee;
        private long totalPayAmount;
    }

    public class OrderedProductDto {
        private long productId;
        private String productName;
        private String productImagePath;
        private long productPrice;
        private long quantity;
        private OrderStatus orderStatus;
        private Long packageId;
        private String packageName;
        private Long packagePrice;
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
    ‼️ List&lt;AdminAllOrdersGetResponseDto&gt; 응답됩니다 ‼️
    ``` java
    public class AdminAllOrdersGetResponseDto {
        private long clientId;
        private String clientEmail;
        private ZonedDateTime orderDate;
        private List<OrderedProductDto> orderedProductDtoList;
    }
    
    public class OrderedProductDto {
        private long productId;
        private String productName;
        private String productImagePath;
        private long productPrice;
        private long quantity;
        private OrderStatus orderStatus;
        private Long packageId;
        private String packageName;
        private Long packagePrice;
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

    
