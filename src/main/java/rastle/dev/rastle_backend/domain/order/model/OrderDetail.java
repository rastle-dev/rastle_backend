package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.DeliveryStatus;
import rastle.dev.rastle_backend.global.common.enums.PaymentStatus;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

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
    @Column(name = "delivery_price")
    private Long deliveryPrice;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;
    @Column(name = "imp_id")
    private String impId;
    @NotNull
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "delivery_msg")
    private String deliveryMsg;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<OrderProduct> orderProduct = new ArrayList<>();

    @Builder
    public OrderDetail(String userName, String tel, String email, String postcode, String deliveryAddress, String orderNumber, DeliveryStatus deliveryStatus, PaymentStatus paymentStatus, Member member, Long paymentPrice, String impId, Long deliveryPrice, String devlieryMsg) {

        this.userName = userName;
        this.tel = tel;
        this.email = email;
        this.postcode = postcode;
        this.deliveryAddress = deliveryAddress;
        this.orderNumber = orderNumber;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus.toString();
        this.member = member;
        this.paymentPrice = paymentPrice;
        this.impId = impId;
        this.deliveryPrice = deliveryPrice;
        this.deliveryMsg = devlieryMsg;
    }

    public void paid(PaymentResponse paymentResponse) {
        this.paymentStatus = PAID.toString();
        this.userName = paymentResponse.getBuyerName();
        this.tel = paymentResponse.getBuyerTel();
        this.email = paymentResponse.getBuyerEmail();
        this.postcode = paymentResponse.getBuyerPostCode();
        this.deliveryAddress = paymentResponse.getBuyerAddress();
        this.deliveryStatus = NOT_STARTED;
        this.impId = paymentResponse.getImpUID();
    }

    public void updatePaymentPrice(Long paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public void updateOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus.toString();
    }

    public void updateDeliveryPrice(Long deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
