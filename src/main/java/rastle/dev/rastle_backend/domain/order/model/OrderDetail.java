package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO.PortOnePaymentResponse;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.DeliveryStatus;
import rastle.dev.rastle_backend.global.common.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.JOINED;
import static rastle.dev.rastle_backend.global.common.enums.DeliveryStatus.NOT_STARTED;
import static rastle.dev.rastle_backend.global.common.enums.PaymentStatus.PAID;

/**
 * 주문이 완료된 데이터를 관리하는 객체
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_detail", indexes = {
        @Index(name = "idx_order_number", columnList = "order_number", unique = true)
})
@Inheritance(strategy = JOINED)
public class OrderDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;
    private String tel;
    private String email;
    private String postcode;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    @Column(name = "payment_price")
    private Long paymentPrice;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;
    @Column(name = "imp_id")
    private String impId;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<OrderProduct> orderProduct = new ArrayList<>();

    @Builder
    public OrderDetail(String userName, String tel, String email, String postcode, String deliveryAddress, String orderNumber, DeliveryStatus deliveryStatus, PaymentStatus paymentStatus, Member member, Long paymentPrice, String impId) {

        this.userName = userName;
        this.tel = tel;
        this.email = email;
        this.postcode = postcode;
        this.deliveryAddress = deliveryAddress;
        this.orderNumber = orderNumber;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.member = member;
        this.paymentPrice = paymentPrice;
        this.impId = impId;
    }

    public void paid(PortOnePaymentResponse paymentResponse) {
        this.paymentStatus = PAID;
        this.userName = paymentResponse.getResponse().getBuyer_name();
        this.tel = paymentResponse.getResponse().getBuyer_tel();
        this.email = paymentResponse.getResponse().getBuyer_email();
        this.postcode = paymentResponse.getResponse().getBuyer_postcode();
        this.deliveryAddress = paymentResponse.getResponse().getBuyer_addr();
        this.deliveryStatus = NOT_STARTED;
        this.impId = paymentResponse.getResponse().getImp_uid();
    }

    public void updatePaymentPrice(Long paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public void updateOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
