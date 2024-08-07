// package rastle.dev.rastle_backend.domain.jmeter.model;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.ColumnDefault;
// import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

// import java.time.LocalDateTime;

// import static jakarta.persistence.EnumType.STRING;

// @Entity
// @Getter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "test_order_product")
// public class TestOrderProduct {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
//     @Column(name = "order_status")
//     @Enumerated(STRING)
//     private OrderStatus orderStatus;
//     private String name; // 주문 당시 구매한 상품 이름
//     private String color;
//     private String size;
//     private Long count;
//     @ColumnDefault("0")
//     private Long price;
//     @ColumnDefault("0")
//     @Column(name = "total_price")
//     private Long totalPrice;
//     @Column(name = "tracking_number")
//     private String trackingNumber;
//     @Column(name = "product_order_number")
//     private Long productOrderNumber;
//     @ColumnDefault("0")
//     @Column(name = "cancel_amount")
//     private Long cancelAmount;
//     @ColumnDefault("0")
//     @Column(name = "cancel_request_amount")
//     private Long cancelRequestAmount;
//     @ColumnDefault("0")
//     @Column(name = "return_amount")
//     private Long returnAmount;
//     @ColumnDefault("0")
//     @Column(name = "return_request_amount")
//     private Long returnRequestAmount;
//     @Column(name = "delivery_time")
//     private LocalDateTime deliveryTime;

//     public static TestOrderProduct newOrderProduct() {
//         return TestOrderProduct.builder()
//             .orderStatus(OrderStatus.DELIVERED)
//             .name("qwer1234")
//             .color("!2341234")
//             .size("12341234")
//             .count(2L)
//             .price(1234123L)
//             .totalPrice(1234123L)
//             .trackingNumber("12341234")
//             .productOrderNumber(12341324L)
//             .cancelAmount(12341234L)
//             .cancelRequestAmount(12341234L)
//             .returnAmount(12341234L)
//             .returnRequestAmount(12341234L)
//             .deliveryTime(LocalDateTime.now())
//             .build();
//     }
// }
