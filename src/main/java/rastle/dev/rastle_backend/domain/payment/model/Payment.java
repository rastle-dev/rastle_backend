package rastle.dev.rastle_backend.domain.payment.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

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
    @Column(name = "payment_price")
    private Long paymentPrice;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
    @Builder
    public Payment(String impId, String paymentMethod, Long couponId, Long paymentPrice, OrderDetail orderDetail) {

        this.impId = impId;
        this.paymentMethod = paymentMethod;
        this.couponId = couponId;
        this.paymentPrice = paymentPrice;
        this.orderDetail = orderDetail;
    }

    public void paid(PaymentResponse paymentResponse) {
        this.impId = paymentResponse.getImpUID();
        this.paymentMethod = paymentResponse.getPayMethod();
        this.couponId = paymentResponse.getCouponId();
        this.paymentPrice = paymentResponse.getAmount();
    }

    public void updatePaymentPrice(Long paymentPrice) {
        this.paymentPrice = paymentPrice;
    }
}
