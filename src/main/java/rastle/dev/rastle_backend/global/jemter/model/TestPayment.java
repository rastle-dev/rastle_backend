package rastle.dev.rastle_backend.global.jemter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_payment")
public class TestPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "imp_id", unique = true)
    private String impId;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "coupon_id")
    private Long couponId;
    @ColumnDefault("0")
    @Column(name = "coupon_amount")
    private Long couponAmount;
    @ColumnDefault("0")
    @Column(name = "payment_price")
    private Long paymentPrice;
    @ColumnDefault("0")
    @Column(name = "cancelled_sum")
    private Long cancelledSum;
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}
