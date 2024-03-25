package rastle.dev.rastle_backend.domain.payment.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment", indexes = {
    @Index(name = "idx_imp_id", columnList = "imp_id", unique = true)
})
@Inheritance(strategy = JOINED)
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "payment_id")
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
    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @Builder
    public Payment(String impId, String paymentMethod, Long couponId, Long couponAmount, Long paymentPrice, LocalDateTime paidAt, OrderDetail orderDetail, Long cancelledSum) {

        this.impId = impId;
        this.paymentMethod = paymentMethod;
        this.couponId = couponId;
        this.couponAmount = couponAmount;
        this.paymentPrice = paymentPrice;
        this.paidAt = paidAt;
        this.orderDetail = orderDetail;
        this.cancelledSum = cancelledSum;
    }

    public void paid(PaymentResponse paymentResponse) {
        this.impId = paymentResponse.getImpUID();
        this.paymentMethod = paymentResponse.getPayMethod();
        this.couponId = paymentResponse.getCouponId();
        this.paymentPrice = paymentResponse.getAmount();
        this.paidAt = paymentResponse.getPaidAt();
    }

    public void updatePaymentPrice(Long paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public void updateCouponAmount(Long couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void addCancelledSum(Long cancelledSum) {
        this.cancelledSum += cancelledSum;
    }
}
