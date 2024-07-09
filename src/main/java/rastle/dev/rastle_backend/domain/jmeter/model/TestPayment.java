// package rastle.dev.rastle_backend.domain.jmeter.model;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.ColumnDefault;

// import java.time.LocalDateTime;

// @Entity
// @Getter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "test_payment")
// public class TestPayment {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
//     @Column(name = "imp_id")
//     private String impId;
//     @Column(name = "payment_method")
//     private String paymentMethod;
//     @Column(name = "coupon_id")
//     private Long couponId;
//     @ColumnDefault("0")
//     @Column(name = "coupon_amount")
//     private Long couponAmount;
//     @ColumnDefault("0")
//     @Column(name = "payment_price")
//     private Long paymentPrice;
//     @ColumnDefault("0")
//     @Column(name = "cancelled_sum")
//     private Long cancelledSum;
//     @Column(name = "paid_at")
//     private LocalDateTime paidAt;

//     public static TestPayment newPayment() {
//         return TestPayment.builder()
//             .impId("12341234")
//             .paymentMethod("123412341")
//             .couponId(1L)
//             .couponAmount(300L)
//             .paymentPrice(30000L)
//             .cancelledSum(0L)
//             .paidAt(LocalDateTime.now())
//             .build();
//     }
// }
